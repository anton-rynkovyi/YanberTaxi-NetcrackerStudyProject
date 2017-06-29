package com.netcracker.project.study.vaadin.driver.components.tabs;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;

import com.netcracker.project.study.vaadin.client.events.RefreshClientViewEvent;
import com.netcracker.project.study.vaadin.client.events.SendClientMessage;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.page.DriverPage;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.addons.Toast;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;
import org.vaadin.spring.events.EventBus;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ViewScope
@SpringComponent
public class NewOrdersTab extends CustomComponent {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DriverService driverService;

    @Autowired
    UserFacade userFacade;

    @Autowired
    AdminService adminService;

    @Autowired
    private EventBus.ApplicationEventBus appEventBus;

    private Toastr toastr;

    private OrdersViewForDrivers view;

    private Grid<OrderInfo> ordersGrid;
    private HorizontalLayout componentLayout;
    private List<Order> ordersList;
    private Button acceptOrderButton;

    private Panel ordersHistoryPanel;

    private Window window;

    private Driver driver;

    private Panel currentOrderPanel;

    private OrderInfo currentOrder;

    private Button startPerformingButton;
    private Button finishPerformingButton;
    private Button goHomeButton;
    private long startkm;
    private long finishkm;

    public void init() {
        initRootLayout();

        toastr = new Toastr();
        VerticalLayout ordersGridLayout = generateOrdersGrid();

        setTakeOrderButton();

        ordersHistoryPanel = getFilledHighPanel(ordersGridLayout);
        ordersHistoryPanel.setIcon(VaadinIcons.CLIPBOARD_TEXT);

        initStartPerformingButton();
        initFinishPerformingButton();

        currentOrderPanel = getCurrentorderPanel();
        currentOrderPanel.setIcon(VaadinIcons.CLIPBOARD_USER);
        setButtonsEnabled();

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(ordersHistoryPanel, currentOrderPanel);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(acceptOrderButton);
        buttonsLayout.setSpacing(true);
        verticalLayout.addComponents(horizontalSplitPanel, buttonsLayout);
        verticalLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);

        componentLayout.addComponent(verticalLayout);
        componentLayout.setSizeFull();

        setCompositionRoot(componentLayout);
    }

    public void setAcceptButtonEnabled(boolean value) {
        acceptOrderButton.setEnabled(value);
    }

    private void setButtonsEnabled() {
        if (currentOrder == null) {
            startPerformingButton.setEnabled(false);
            finishPerformingButton.setEnabled(false);
        } else {
            if (currentOrder.getStatus().equals("Accepted")) {
                startPerformingButton.setEnabled(true);
                finishPerformingButton.setEnabled(false);
            }
            if (currentOrder.getStatus().equals("Performing")) {
                startPerformingButton.setEnabled(false);
                finishPerformingButton.setEnabled(true);
            }
        }

    }



    private HorizontalLayout getOrderInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout orderLayout = new VerticalLayout();

        HorizontalLayout clientLayout = new HorizontalLayout();
        Label clientIcon = new Label();
        Label client = new Label("<b>Client:</b> " + currentOrder.getClientName(), ContentMode.HTML);
        clientIcon.setIcon(VaadinIcons.MALE);
        clientLayout.addComponents(clientIcon, client);

        HorizontalLayout statusLayout = new HorizontalLayout();
        Label statusIcon = new Label();
        statusIcon.setIcon(VaadinIcons.RHOMBUS);
        Label status = new Label("<b>Status:</b> " + currentOrder.getStatus(), ContentMode.HTML);
        statusLayout.addComponents(statusIcon, status);

        HorizontalLayout costLabel = new HorizontalLayout();
        Label costIcon = new Label();
        costIcon.setIcon(VaadinIcons.CASH);
        Label cost = new Label("<b>Cost:</b> " + currentOrder.getCost(), ContentMode.HTML);
        costLabel.addComponents(costIcon, cost);

        HorizontalLayout distanceLayout = new HorizontalLayout();
        Label distanceIcon = new Label();
        distanceIcon.setIcon(VaadinIcons.ARROWS_LONG_H);
        Label distance = new Label("<b>Distance:</b> " + currentOrder.getDistance(), ContentMode.HTML);
        distanceLayout.addComponents(distanceIcon, distance);
        orderLayout.addComponents(clientLayout, statusLayout, costLabel, distanceLayout);

        List<Label> labels = getRoutesLayout();
        VerticalLayout routeLayout = new VerticalLayout();

        if (labels.size() == 0) {
            Label noRouteLabel = new Label("No route provided");
            routeLayout.addComponent(noRouteLabel);
        } else {
            for (Label label : labels) {
                routeLayout.addComponent(label);
            }
        }

        horizontalLayout.addComponents(orderLayout, routeLayout);

        return horizontalLayout;
    }

    private List<Label> getRoutesLayout() {
        List<Route> routes = orderService.getRoutes(currentOrder.getObjectId());
        List<Label> labels = new ArrayList<>();

        int i = 0;
        for (Route route : routes) {
            Label label = new Label("<b>Address " + i + ": </b>" + route.getCheckPoint(), ContentMode.HTML);
            labels.add(label);
            label.setIcon(VaadinIcons.MAP_MARKER);
            i++;
        }

        return labels;
    }


    private Panel getCurrentorderPanel() {
        List<Order> orders = orderService.getCurrentOrderByDriverId(driver.getObjectId());
        Panel panel = new Panel("Current order");
        VerticalLayout verticalLayout = new VerticalLayout();
        if (orders.size() != 0) {
            List<OrderInfo> info = orderService.getOrdersInfo(orders);
            currentOrder = info.get(info.size() - 1);
            HorizontalLayout horizontalLayout = new HorizontalLayout(startPerformingButton, finishPerformingButton);
            horizontalLayout.setComponentAlignment(startPerformingButton, Alignment.MIDDLE_CENTER);
            horizontalLayout.setComponentAlignment(finishPerformingButton, Alignment.MIDDLE_CENTER);
            verticalLayout.addComponents(getOrderInfoLayout(), horizontalLayout);
            verticalLayout.setSizeFull();
            panel.setContent(verticalLayout);
        } else {
            Label label = new Label("You have no order performing");
            verticalLayout.addComponent(label);
            verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
            verticalLayout.setSizeFull();
            panel.setContent(label);
        }
        return panel;

    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    private void initRootLayout() {
        componentLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(true);
    }

    private Panel getFilledHighPanel(VerticalLayout verticalLayout) {
        Panel panel = new Panel("Available orders");
        panel.setSizeFull();
        panel.setContent(verticalLayout);
        return panel;
    }

    private void refreshCurrentOrderPanel() {
        List<Order> orders = orderService.getCurrentOrderByDriverId(driver.getObjectId());
        if (orders.size() != 0) {
            List<OrderInfo> info = orderService.getOrdersInfo(orders);
            currentOrder = info.get(info.size() - 1);
            VerticalLayout verticalLayout = new VerticalLayout();
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponents(startPerformingButton, finishPerformingButton);
            verticalLayout.addComponents(getOrderInfoLayout(), horizontalLayout);
            currentOrderPanel.setContent(verticalLayout);
        } else {
            Label label = new Label("You have no order performing", ContentMode.HTML);
            currentOrderPanel.setContent(label);
        }

    }

    public void refreshContent() {
        refreshGrid();
        acceptOrderButton.setEnabled(false);
        ((DriverPage)getUI()).refreshUI();
       // view.Refresh();
        refreshCurrentOrderPanel();
        setButtonsEnabled();
    }

    private void initFinishPerformingButton() {
        finishPerformingButton = new Button("Finish Performing");
        finishPerformingButton.addClickListener(event -> {

            TextField textField = new TextField("Count of km");
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(textField);
            Label errorLabel = new Label();
            Button ok = new Button("OK");
            ok.addClickListener(newEvent -> {
                try {
                    finishkm = Long.parseLong(textField.getValue());
                    long distance = finishkm - startkm;

                    orderService.setDistance(currentOrder.getObjectId(), distance);
                    orderService.changeStatus(OrderStatus.PERFORMED, currentOrder.getObjectId());
                    orderService.setClientPoints(currentOrder.getObjectId());
                    driverService.changeStatus(DriverStatusList.FREE, driver.getObjectId());
                    orderService.calcPrice(BigInteger.valueOf(distance), currentOrder.getObjectId());

                    acceptOrderButton.setEnabled(true);
                    window.close();
                    refreshContent();
                    ((DriverPage)getUI()).setStatusButtonEnabled(true);
                    appEventBus.publish(this, new RefreshClientViewEvent(this,currentOrder.getObjectId()));
                    currentOrder = null;


                } catch (NumberFormatException e) {
                    errorLabel.setCaption("Incorrect data. Only digits are admissible");
                }
            });

            verticalLayout.addComponent(errorLabel);
            verticalLayout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
            verticalLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
            verticalLayout.addComponent(ok);
            verticalLayout.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
            verticalLayout.setSpacing(true);

            window = new Window();
            window.setCaption("Enter km");
            window.center();
            window.setModal(true);
            window.setContent(verticalLayout);
            UI.getCurrent().addWindow(window);

            startPerformingButton.setEnabled(false);
            finishPerformingButton.setEnabled(false);
        });
    }

    private void initStartPerformingButton() {
        startPerformingButton = new Button("Start Performing");
        startPerformingButton.addClickListener(event -> {

            TextField textField = new TextField("Count of km");
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(textField);

            Label errorLabel = new Label();
            Button ok = new Button("OK");

            ok.addClickListener(newEvent -> {
                try {
                    startkm = Long.parseLong(textField.getValue());
                    orderService.changeStatus(OrderStatus.PERFORMING, currentOrder.getObjectId());
                    driverService.changeStatus(DriverStatusList.PERFORMING_ORDER, driver.getObjectId());
                    window.close();
                    refreshContent();
                    ((DriverPage)getUI()).setStatusButtonEnabled(false);
                } catch (NumberFormatException e) {
                    errorLabel.setCaption("Incorrect data. Only digits are admissible");
                }

            });

            verticalLayout.addComponent(errorLabel);
            verticalLayout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
            verticalLayout.addComponent(ok);
            verticalLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
            verticalLayout.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
            verticalLayout.setSpacing(true);

            window = new Window();
            window.setCaption("Enter km");
            window.center();
            window.setModal(true);
            window.setContent(verticalLayout);
            UI.getCurrent().addWindow(window);
            refreshContent();

        });
    }

    private void setTakeOrderButton() {
        acceptOrderButton = new Button("Accept", VaadinIcons.ARROW_RIGHT);
        acceptOrderButton.setStyleName(MaterialTheme.BUTTON_ICON_ALIGN_RIGHT);
        if (currentOrder != null) {
            acceptOrderButton.setEnabled(false);
        }
        acceptOrderButton.addClickListener(event -> {
            if (isBanned()) {
                SecurityContextHolder.clearContext();
                return;
            }
            if (isDismissed()) {
                logout();
                return;
            }
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                List<Order> currentOrder = orderService.getCurrentOrderByDriverId(driver.getObjectId());
                if (currentOrder.size() == 0) {
                    OrderInfo order = ordersGrid.asSingleSelect().getValue();
                    driverService.acceptOrder(order.getObjectId(), driver.getObjectId());
                    acceptOrderButton.setEnabled(false);
                    appEventBus.publish(this, new SendClientMessage(this,order.getObjectId()));
                   // setStartEndPointsLayoutsEmpty();
                }
                refreshContent();
                ((DriverPage)getUI()).setStatusButtonEnabled(false);
                refreshContent();

            }
        });

        acceptOrderButton.setEnabled(false);
    }

    public void refreshGrid() {
        ordersList = orderService.getOrders(OrderStatus.NEW);
        ordersGrid.setItems(orderService.getOrdersInfo(ordersList));
    }


    /*private void setStartEndPointsLayout(String source,String destination){
        startEndPointsLayout.removeAllComponents();
        Label sourceLabel = new Label(source);
        Label destinationLabel = new Label(destination);

        Label iconLabel = new Label();
        iconLabel.setIcon(VaadinIcons.ARROW_RIGHT);

        startEndPointsLayout.addComponents(sourceLabel,iconLabel,destinationLabel);
        startEndPointsLayout.setComponentAlignment(sourceLabel,Alignment.MIDDLE_CENTER);
        startEndPointsLayout.setComponentAlignment(iconLabel,Alignment.MIDDLE_CENTER);
        startEndPointsLayout.setComponentAlignment(destinationLabel,Alignment.MIDDLE_CENTER);
        startEndPointsLayout.setSizeFull();
        routePanel.setContent(startEndPointsLayout);
    }*/

    /*private void setStartEndPointsLayoutsEmpty(){
        setStartEndPointsLayout("----------------","----------------");
    }*/

    private VerticalLayout generateOrdersGrid() {
        VerticalLayout verticalLayout = new VerticalLayout();
        ordersGrid = new Grid<>();
        ordersGrid.setSizeFull();
        ordersList = orderService.getOrders(OrderStatus.NEW);

        List<OrderInfo> ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("#");
        ordersGrid.addColumn(OrderInfo::getStartPoint).setCaption("Start");
        ordersGrid.addColumn(OrderInfo::getDestination).setCaption("Destination");
        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");

        ordersGrid.addSelectionListener(selectionEvent -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                if (currentOrder == null) {
                    if (!driver.getStatus().equals(DriverStatusList.OFF_DUTY)) {
                        acceptOrderButton.setEnabled(true);
                    }
                }
                OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();
                List<Route> routes = orderService.getRoutes(orderInfo.getObjectId());
                /*if(!routes.isEmpty()){
                    setStartEndPointsLayout(routes.get(0).getCheckPoint(),
                            routes.get(routes.size() - 1).getCheckPoint());
                }else{
                   setStartEndPointsLayoutsEmpty();
                }*/
            }
        });

        verticalLayout.addComponent(ordersGrid);

        return verticalLayout;
    }


    private void logout() {
        SecurityContextHolder.clearContext();
        getUI().getSession().close();
        getUI().getPage().setLocation("/authorization");
    }

    private boolean isDismissed() {
        if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
            return true;
        }
        return false;
    }

    private boolean isBanned() {
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        if (!user.isEnabled()) {
            UI.getCurrent().setContent(toastr);
            Driver driver = adminService.getModelById(this.driver.getObjectId(), Driver.class);
            Toast banToast = ToastBuilder.of(ToastType.Warning,
                    "You have been banned." +
                            "\nUnban date: " + new Timestamp(driver.getUnbanDate().getTime()) +
                            "\n Contacts: yanbertaxi.netcracker@gmail.com")
                    .caption("Information")
                    .options(ToastOptionsBuilder.having()
                            .closeButton(false)
                            .debug(false)
                            .progressBar(false)
                            .preventDuplicates(true)
                            .position(ToastPosition.Top_Full_Width)
                            .tapToDismiss(false)
                            .extendedTimeOut(600000)
                            .build())
                    .build();
            toastr.toast(banToast);
            return true;
        }
        return false;
    }

    public void setView(OrdersViewForDrivers view) {
        this.view = view;
    }

    public List getOrdersList() {
        return ordersList;
    }

}

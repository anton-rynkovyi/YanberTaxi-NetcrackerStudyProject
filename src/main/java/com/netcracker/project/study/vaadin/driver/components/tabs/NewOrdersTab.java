package com.netcracker.project.study.vaadin.driver.components.tabs;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;

import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.client.events.RefreshClientOrderInfoEvent;
import com.netcracker.project.study.vaadin.client.events.RefreshClientViewEvent;
import com.netcracker.project.study.vaadin.client.events.SendClientMessage;
import com.netcracker.project.study.vaadin.driver.components.popup.ResultPopUp;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.page.DriverPage;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;
import org.vaadin.spring.events.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Scope(value = "prototype")
@SpringComponent
public class NewOrdersTab extends CustomComponent {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EventBus.ApplicationEventBus appEventBus;

    @Autowired
    ResultPopUp resultPopUp;

    private Toastr toastr;
    private Toastr banToastr;

    private Grid<OrderInfo> ordersGrid;
    private HorizontalLayout componentLayout;
    private List<Order> ordersList;

    private Window window;

    private Driver driver;
    private Car car;

    private Panel ordersHistoryPanel;
    private Panel currentOrderPanel;

    private OrderInfo currentOrder;

    private double startKm;
    private double finishKm;

    private Button acceptOrderButton;
    private Button startPerformingButton;
    private Button finishPerformingButton;
    private Button viewRouteButton;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public void init() {
        initRootLayout();

        toastr = new Toastr();
        banToastr = new Toastr();
        banToastr.registerToastrListener(new ToastrListenerAdapter(){
            @Override
            public void onClick() {
                UI.getCurrent().getPage().setLocation("/authorization");
            }

            @Override
            public void onHidden() {
                UI.getCurrent().getPage().setLocation("/authorization");
            }
        });

        setTakeOrderButton();
        setViewRouteButton();

        VerticalLayout ordersGridLayout = generateOrdersGrid();
        ordersHistoryPanel = getFilledHighPanel(ordersGridLayout);
        ordersHistoryPanel.setIcon(VaadinIcons.CLIPBOARD_TEXT);

        initStartPerformingButton();
        initFinishPerformingButton();

        currentOrderPanel = getCurrentOrderPanel();
        currentOrderPanel.setIcon(VaadinIcons.CLIPBOARD_USER);
        setButtonsEnabled();

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(ordersHistoryPanel, currentOrderPanel);
        horizontalSplitPanel.setSizeFull();

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(viewRouteButton,acceptOrderButton);
        buttonsLayout.setSpacing(true);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(toastr);
        verticalLayout.addComponents(horizontalSplitPanel, buttonsLayout);
        verticalLayout.setComponentAlignment(horizontalSplitPanel, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        verticalLayout.setSizeFull();
        verticalLayout.setMargin(false);

        componentLayout.addComponent(verticalLayout);
        componentLayout.setSizeFull();
        componentLayout.setMargin(false);

        setCompositionRoot(componentLayout);
        setSizeFull();
    }

    private void initRootLayout() {
        componentLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(true);
    }

    public void refreshContent() {
        refreshGrid();
        //((DriverPage)getUI()).refreshUI();
        ((OrdersViewForDrivers)(UI.getCurrent()).getNavigator().getCurrentView()).refreshAllOrdersGrid();
        refreshCurrentOrderPanel();
        setButtonsEnabled();
        setAcceptButtonEnabled();
    }

    public void refreshGrid() {
        ordersList = orderService.getOrders(OrderStatus.NEW);
        ordersGrid.setItems(orderService.getOrdersInfo(ordersList));
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

        orderLayout.addComponents(clientLayout, statusLayout);

        VerticalLayout routeLayout = routeInfoLayout(currentOrder.getObjectId());

        horizontalLayout.addComponents(orderLayout, routeLayout);

        return horizontalLayout;
    }

    private void setViewRouteButton() {
        viewRouteButton = new Button("View route");
        viewRouteButton.addClickListener(event -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                List<Order> currentOrder = orderService.getCurrentOrderByDriverId(driver.getObjectId());
                if (currentOrder.size() == 0) {
                    OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();

                    Window window = new Window(" Full route");
                    window.setModal(true);
                    window.center();
                    window.setResizable(false);
                    window.setContent(routeInfoLayout(orderInfo.getObjectId()));

                    UI.getCurrent().addWindow(window);
                }
            }

        });
    }

    private VerticalLayout routeInfoLayout(BigInteger orderId) {
        List<Route> routes = orderService.getRoutes(orderId);

        VerticalLayout routeLayout = new VerticalLayout();
        if (routes.size() != 0) {
            HorizontalLayout departureLayout = new HorizontalLayout();
            Label homeIcon = new Label();
            homeIcon.setIcon(VaadinIcons.HOME_O);
            Label departureLabel = new Label(routes.get(0).getCheckPoint());
            departureLayout.addComponents(homeIcon, departureLabel);
            routeLayout.addComponent(departureLayout);


            for (int i = 1; i < routes.size() - 1; i++) {
                HorizontalLayout middlePointLayout = new HorizontalLayout();
                Label middlePoint = new Label(routes.get(i).getCheckPoint());
                Label mapMarkerLabel = new Label();
                mapMarkerLabel.setIcon(VaadinIcons.MAP_MARKER);
                middlePointLayout.addComponents(mapMarkerLabel, middlePoint);
                routeLayout.addComponent(middlePointLayout);
            }

            HorizontalLayout destinationLayout = new HorizontalLayout();
            Label finishIcon = new Label();
            finishIcon.setIcon(VaadinIcons.FLAG_CHECKERED);
            Label destinationLabel = new Label(routes.get(routes.size() - 1).getCheckPoint());
            destinationLayout.addComponents(finishIcon, destinationLabel);
            routeLayout.addComponent(destinationLayout);

        } else {
            HorizontalLayout noRouteLayout = new HorizontalLayout();
            Label noRouteIcon = new Label();
            noRouteIcon.setIcon(VaadinIcons.BAN);
            Label noRouteLabel = new Label("There is no route");
            noRouteLayout.addComponents(noRouteIcon, noRouteLabel);
            routeLayout.addComponent(noRouteLayout);
        }

        return routeLayout;
    }

    private Panel getCurrentOrderPanel() {
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
            if(!driver.getStatus().equals(DriverStatusList.OFF_DUTY)){
                acceptOrderButton.setEnabled(true);
                ((DriverPage)getUI()).refreshUI();
            }
        }
    }

    private void initFinishPerformingButton() {
        finishPerformingButton = new Button("Finish Performing");
        finishPerformingButton.addClickListener(event -> {
            TextField textField = new TextField("Count of km");
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(textField);

            HorizontalLayout errorLayout = new HorizontalLayout();
            Label iconLabel = new Label();
            Label errorLabel = new Label();
            errorLayout.addComponents(iconLabel,errorLabel);

            Button ok = new Button("OK");
            ok.addClickListener(newEvent -> {
                try {
                    finishKm = Double.parseDouble(textField.getValue());
                    if(finishKm <= startKm){
                        throw new IllegalArgumentException();
                    }
                    if(finishKm <= 0){
                        iconLabel.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
                        errorLabel.setCaption("Incorrect data. Number must be positive..");
                    }else{
                        double distance = finishKm - startKm;

                        orderService.setDistance(currentOrder.getObjectId(), distance);
                        orderService.changeStatus(OrderStatus.PERFORMED, currentOrder.getObjectId());
                        orderService.setClientPoints(currentOrder.getObjectId());
                        driverService.changeStatus(DriverStatusList.FREE, driver.getObjectId());
                        BigDecimal cost = orderService.calcPrice(BigDecimal.valueOf(distance), currentOrder.getObjectId());
                        cost.setScale(2, BigDecimal.ROUND_HALF_UP);
                        acceptOrderButton.setEnabled(true);
                        window.close();
                        refreshContent();
                        ((DriverPage)getUI()).setStatusButtonEnabled(true);
                        appEventBus.publish(this, new RefreshClientViewEvent(this,currentOrder.getObjectId()));
                        currentOrder = null;
                        toastr.toast(ToastBuilder.info("Order has been successfully performed. Distance: " + distance + ". " + "Cost: " + cost).build());
                        BigDecimal bdDistance = new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_UP);
                        resultPopUp.init(cost, bdDistance);
                        UI.getCurrent().addWindow(resultPopUp);
                    }
                } catch (NumberFormatException e) {
                    iconLabel.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
                    errorLabel.setCaption("Incorrect data. Only digits are admissible.");
                }catch (IllegalArgumentException e){
                    iconLabel.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
                    errorLabel.setCaption("Incorrect data. Number must be greater than previous.");
                }
            });

            verticalLayout.addComponent(errorLayout);
            verticalLayout.setComponentAlignment(errorLayout, Alignment.MIDDLE_CENTER);
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

            setAcceptButtonEnabled();
        });
    }

    private void initStartPerformingButton() {
        startPerformingButton = new Button("Start Performing");
        startPerformingButton.addClickListener(event -> {
            TextField textField = new TextField("Count of km");
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(textField);

            HorizontalLayout errorLayout = new HorizontalLayout();
            Label iconLabel = new Label();
            Label errorLabel = new Label();
            errorLayout.addComponents(iconLabel,errorLabel);

            Button ok = new Button("OK");

            ok.addClickListener(newEvent -> {
                try {
                    startKm = Double.parseDouble(textField.getValue());
                    if(startKm < 0){
                        iconLabel.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
                        errorLabel.setCaption("Incorrect data. Number must be positive.");
                    }else{
                        orderService.changeStatus(OrderStatus.PERFORMING, currentOrder.getObjectId());
                        driverService.changeStatus(DriverStatusList.PERFORMING_ORDER, driver.getObjectId());
                        window.close();
                        refreshContent();
                        ((DriverPage)getUI()).setStatusButtonEnabled(false);
                        appEventBus.publish(this, new RefreshClientOrderInfoEvent(this,currentOrder.getObjectId()));

                    }

                } catch (NumberFormatException e) {
                    iconLabel.setIcon(VaadinIcons.EXCLAMATION_CIRCLE_O);
                    errorLabel.setCaption("Incorrect data. Only digits are admissible.");
                }
            });

            verticalLayout.addComponent(errorLayout);
            verticalLayout.setComponentAlignment(errorLayout, Alignment.MIDDLE_CENTER);
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
            setAcceptButtonEnabled();

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
                SecurityContextHolder.clearContext();
                return;
            }
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                List<Order> currentOrder = orderService.getCurrentOrderByDriverId(driver.getObjectId());
                if (currentOrder.size() == 0) {
                    OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();
                    try {
                        Order order = orderService.getOrder(orderInfo.getObjectId());
                        if (order.getDriverId() == null) {
                            driverService.acceptOrder(order.getObjectId(), driver.getObjectId());
                            acceptOrderButton.setEnabled(false);
                            appEventBus.publish(this, new SendClientMessage(this, order.getObjectId(), driver, car));
                        } else {
                            toastr.toast(ToastBuilder.info("This order has been accepted by another driver").build());
                        }
                    } catch (Exception e) {
                        toastr.toast(ToastBuilder.info("This order has been accepted by another driver").build());
                    }
                }
                refreshContent();
                ((DriverPage)getUI()).setStatusButtonEnabled(false);
            }
        });

        acceptOrderButton.setEnabled(false);
    }

    private VerticalLayout generateOrdersGrid() {

        VerticalLayout verticalLayout = new VerticalLayout();
        ordersGrid = new Grid<>();
        ordersGrid.setSizeFull();
        ordersList = orderService.getOrders(OrderStatus.NEW);

        List<OrderInfo> ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("№");
        ordersGrid.addColumn(OrderInfo::getStartPoint).setCaption("Departure");
        ordersGrid.addColumn(OrderInfo::getDestination).setCaption("Destination");
        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
        ordersGrid.addColumn(OrderInfo -> OrderInfo.getDescription().substring(0, OrderInfo.getDescription().indexOf(" -")) +" seats " +
                (OrderInfo.getDescription().substring((OrderInfo.getDescription().indexOf("- "))+1).contains("true") ? "(child)" : ""))
                .setCaption("Description");

        ordersGrid.addSelectionListener(selectionEvent -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                if (currentOrder == null) {
                    if (!driver.getStatus().equals(DriverStatusList.OFF_DUTY)) {
                       // acceptOrderButton.setEnabled(true);
                        setAcceptButtonEnabled();
                    }
                }
            }
        });

        verticalLayout.addComponent(ordersGrid);

        return verticalLayout;
    }

    private boolean isDismissed() {
        if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
            UI.getCurrent().setContent(banToastr);
            Toast banToast = ToastBuilder.of(ToastType.Warning,
                    "You have been dismissed." +
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
            banToastr.toast(banToast);
            return true;
        }
        return false;
    }

    private boolean isBanned() {
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        if (!user.isEnabled()) {
            UI.getCurrent().setContent(banToastr);
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
            banToastr.toast(banToast);
            return true;
        }
        return false;
    }

    public List getOrdersList() {
        return ordersList;
    }

    public void setAcceptButtonEnabled() {
        driver = userDetailsService.getCurrentUser();
        if(ordersList.isEmpty()){
            acceptOrderButton.setEnabled(false);
        }else{
            if(driver.getStatus().equals(DriverStatusList.FREE)){
                acceptOrderButton.setEnabled(true);
            }else{
                acceptOrderButton.setEnabled(false);
            }
        }
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        List<Car> cars = driverService.getCarByDriver(driver);
        this.car = cars.get(0);
    }

}

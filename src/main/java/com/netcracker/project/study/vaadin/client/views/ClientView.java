package com.netcracker.project.study.vaadin.client.views;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.client.components.grids.ClientCurrentOrderGrid;
import com.netcracker.project.study.vaadin.client.components.grids.ClientOrdersGrid;
import com.netcracker.project.study.vaadin.client.components.OrderMaker;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.*;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.vaadin.addons.Toast;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;

import java.math.BigInteger;
import java.util.List;

@SpringView(name = ClientView.VIEW_NAME)
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    @Autowired
    OrdersViewForDrivers ordersViewForDrivers;

    @Autowired
    private OrderMaker orderMaker;

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    private ClientOrdersGrid clientOrdersGrid;

    @Autowired
    private ClientCurrentOrderGrid clientCurrentOrderGrid;

    @Autowired
    PersistenceFacade facade;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private Button newOrder, cancelOrder;

    Client client;

    private Window window;

    private Toastr toastr;


    public void init() {
        initClient();
        clientCurrentOrderGrid.setClient(client);
        clientCurrentOrderGrid.init();
        orderMaker.setClient(client);
        clientOrdersGrid.setClient(client);
        clientOrdersGrid.init();

        Label label = new Label("<h2>YanberTaxi</h2>", ContentMode.HTML);
        addComponent(label);
        setComponentAlignment(label, Alignment.TOP_RIGHT);

        toastr = new Toastr();
        addComponent(toastr);
        setComponentAlignment(toastr, Alignment.BOTTOM_RIGHT);

        HorizontalLayout mainButtons = setMainButtons();
        addComponent(mainButtons);
        setComponentAlignment(mainButtons, Alignment.TOP_CENTER);

        HorizontalLayout clientInformationFields = new HorizontalLayout();
        Panel clientNamePanel = getClientInfo();
        Panel clientPoints = getClientPoints();

        clientInformationFields.addComponents(clientNamePanel, clientPoints);
        clientInformationFields.setSizeFull();
        addComponent(clientInformationFields);

        HorizontalLayout historyOfClientOrders = new HorizontalLayout(clientOrdersGrid);
        historyOfClientOrders.setSizeFull();

        Panel ordersHistoryPanel = getPanel("History of your orders", historyOfClientOrders);
        ordersHistoryPanel.setIcon(VaadinIcons.RECORDS);

        HorizontalLayout clientCurrentOrder = new HorizontalLayout(clientCurrentOrderGrid);

        Panel currentOrderPanel = getPanel("Your current order", clientCurrentOrder);
        currentOrderPanel.setIcon(VaadinIcons.INFO_CIRCLE_O);

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.addComponent(ordersHistoryPanel);
        horizontalSplitPanel.addComponent(currentOrderPanel);
        addComponent(horizontalSplitPanel);
        setComponentAlignment(horizontalSplitPanel, Alignment.MIDDLE_CENTER);

    }

    public void initClient() {
        this.client = userDetailsService.getCurrentUser();
    }

    private HorizontalLayout setMainButtons() {
        HorizontalLayout root = new HorizontalLayout();

        newOrder = new Button("Make new order", VaadinIcons.TAXI);
        newOrder.addStyleName(MaterialTheme.BUTTON_HUGE + " " + MaterialTheme.BUTTON_ICON_ALIGN_RIGHT + " "
                                + MaterialTheme.BUTTON_FRIENDLY);
        newOrder.setDescription("Press this button to make your order");
        Button.ClickListener orderCreaterListener = new NewOrderCreater();
        newOrder.addClickListener(orderCreaterListener);

        cancelOrder = new Button("Cancel the order", VaadinIcons.CLOSE_BIG);
        cancelOrder.addStyleName(MaterialTheme.BUTTON_HUGE + " " + MaterialTheme.BUTTON_ICON_ALIGN_RIGHT + " "
                                    + MaterialTheme.BUTTON_DANGER);
        cancelOrder.setDescription("Press this button to cancel your last order");
        Button.ClickListener orderCancelListener = new OrderCancelListener();
        cancelOrder.addClickListener(orderCancelListener);

        if (isActiveOrderExist()) {
            newOrder.setVisible(false);
            cancelOrder.setVisible(true);
        } else {
            newOrder.setVisible(true);
            cancelOrder.setVisible(false);
        }

        root.addComponent(newOrder);
        root.addComponent(cancelOrder);

        return root;
    }

    private Panel getPanel(String caption, Component component) {
        Panel panel = new Panel(caption);
        panel.setSizeFull();
        panel.setContent(component);

        return panel;
    }

    private String getClientName(){
        return client.getFirstName() + " " + client.getLastName();
    }

    private Panel getClientInfo(){
        Panel panel = new Panel("Your profile: ");
        Label clientInfo = new Label("<b>" + getClientName() + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(clientInfo);
        horizontalLayout.setComponentAlignment(clientInfo,Alignment.MIDDLE_CENTER);
        panel.setContent(horizontalLayout);

        return panel;
    }

    private Panel getClientPoints(){
        Panel panel = new Panel("Your points: ");
        BigInteger clientPoints = client.getPoints() != null ? client.getPoints() : BigInteger.ZERO;
        Label pointsInfo = new Label("<b>" + clientPoints + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(pointsInfo);
        panel.setContent(horizontalLayout);

        return panel;
    }

    class NewOrderCreater implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            initWindow("");
            window.setCaption("Add information about your order");
            window.setIcon(VaadinIcons.KEYBOARD);
            window.setContent(orderMaker);
            UI.getCurrent().addWindow(window);
            orderMaker.enableCancelOrderButton(cancelOrder);
            orderMaker.disableNewOrderButton(newOrder);
            orderMaker.closeOrderMakerWindow(window);
        }
    }

    class OrderCancelListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            List<Order> orderList = orderService.getActiveOrdersByClientId(client.getObjectId());
            if (orderList.size()>0) {
                Order currentOrder = null;
                for (Order order : orderList) {
                    currentOrder = order;
                    orderService.changeStatus(OrderStatus.CANCELED, order.getObjectId());
                }
                //toastr.toast(ToastBuilder.success("<b>Order " + currentOrder.getName() + " was canceled</b> ").build());
                Toast toast = ToastBuilder.of(ToastType.Info, "<b>Order " + currentOrder.getName() + " was canceled</b> ")
                        .caption("Infomation..")
                        .options(ToastOptionsBuilder.having()
                                .closeButton(true)
                                .debug(false)
                                .progressBar(true)
                                .preventDuplicates(true)
                                .position(ToastPosition.Bottom_Full_Width)
                                .timeOut(10000)
                                .build())
                        .build();
                toastr.toast(toast);

                newOrder.setVisible(true);
                cancelOrder.setVisible(false);

                clientOrdersGrid.init();
                clientCurrentOrderGrid.init();
            } else {
                initWindow("<b>You can't cancel performing order</b> ");
                UI.getCurrent().addWindow(window);
            }
        }
    }

    private boolean isActiveOrderExist() {
        List<Order> orderList = orderService.getActiveOrdersByClientId(client.getObjectId());
        List<Order> orderPerformingList = orderService.getPerformingOrdersByClientId(client.getObjectId());
        if (orderList.size()>0 || orderPerformingList.size() > 0) return true;

        return false;
    }

    private void initWindow(String text){
        window = new Window(" Information");
        window.setResizable(false);
        window.setIcon(VaadinIcons.INFO_CIRCLE);
        window.center();
        window.setModal(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        Label content = new Label(text, ContentMode.HTML);
        verticalLayout.addComponent(content);
        window.setContent(verticalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
}

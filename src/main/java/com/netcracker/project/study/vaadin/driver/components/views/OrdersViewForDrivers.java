package com.netcracker.project.study.vaadin.driver.components.views;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.driver.components.popup.OrderInfoPopUp;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.netcracker.project.study.vaadin.driver.events.CancelClientOrderEvent;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = OrdersViewForDrivers.VIEW_NAME)
public class OrdersViewForDrivers extends VerticalLayout implements View {

    public static final String VIEW_NAME = "driver.orders";

    @Autowired
    private NewOrdersTab newOrders;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderInfoPopUp orderInfoPopUp;

    @Autowired
    org.vaadin.spring.events.EventBus.ViewEventBus viewEventBus;

    @Autowired
    DriverService driverService;

    private Toastr toastr;
    private Driver driver;
    private TabSheet tabSheet;
    private VerticalLayout rootLayout;

    private Grid allOrdersGrid;
    private List<Order> allOrdersList;

    private Window orderInfoWindow;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public void init() {
        toastr = new Toastr();
        addComponent(toastr);

        initDriver();
        newOrders.setDriver(driver);
        newOrders.init();
        tabSheet = getTabSheet();

        rootLayout = new VerticalLayout();
        MarginInfo marginInfo = new MarginInfo(false, true, true, true);
        rootLayout.setMargin(marginInfo);
        rootLayout.setSpacing(false);
        rootLayout.addComponent(tabSheet);
        rootLayout.setSizeFull();

        setSizeFull();

        addComponent(rootLayout);
        setExpandRatio(rootLayout,0.8f);
    }

    public void setAcceptButtonEnabled(){
        newOrders.setAcceptButtonEnabled();
    }

    private void initOrderInfoWindow() {
        orderInfoWindow = new Window(" Information about the order");
        orderInfoWindow.setIcon(VaadinIcons.INFO_CIRCLE);
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);
        orderInfoWindow.setResizable(false);
        orderInfoWindow.setHeight("95%");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(orderInfoPopUp);

        orderInfoPopUp.okButton.addClickListener(event->{
            orderInfoWindow.close();
        });

        verticalLayout.setMargin(false);
        orderInfoWindow.setContent(verticalLayout);

    }

    private HorizontalLayout initViewOrderButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button viewOrderButton = new Button("View order", VaadinIcons.INFO);
        viewOrderButton.addClickListener(event -> {

            if (!allOrdersGrid.asSingleSelect().isEmpty()) {
                OrderInfo orderInfo = (OrderInfo)allOrdersGrid.asSingleSelect().getValue();
                orderInfoPopUp.init(orderInfo);
                initOrderInfoWindow();
                UI.getCurrent().addWindow(orderInfoWindow);
            }
        });

        horizontalLayout.addComponent(viewOrderButton);
        horizontalLayout.setComponentAlignment(viewOrderButton, Alignment.BOTTOM_RIGHT);
        return horizontalLayout;
    }

    private VerticalLayout generateOrdersGrid() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);

        if(allOrdersList != null) {
            Grid<OrderInfo> ordersGrid = new Grid<>();
            ordersGrid.setSizeFull();
            List<OrderInfo> ordersInfo = orderService.getOrdersInfo(allOrdersList);

            ordersGrid.setItems(ordersInfo);

            ordersGrid.addColumn(OrderInfo::getObjectId).setCaption("№").setId("№");
            ordersGrid.addColumn(orderInfo -> orderService.getLastDateFromOrdersLog(orderService.getOrder(orderInfo.getObjectId()))).setCaption("Date");
            ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
            ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
            ordersGrid.addColumn(OrderInfo::getStartPoint).setCaption("Departure");
            ordersGrid.addColumn(OrderInfo::getDestination).setCaption("Destination");
            ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
            ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");

            ordersGrid.sort("№", SortDirection.DESCENDING);

            allOrdersGrid = ordersGrid;

            HorizontalLayout viewOrderButtonLayout = initViewOrderButton();
            verticalLayout.addComponents(allOrdersGrid,viewOrderButtonLayout);
            verticalLayout.setExpandRatio(allOrdersGrid,1f);
        }else{
            Label noOrdersLabel = new Label("<h1>Your history of orders is empty yet.</h1>",ContentMode.HTML);
            Panel panel = new Panel();
            panel.setContent(noOrdersLabel);
            verticalLayout.addComponent(panel);
        }
        verticalLayout.setSizeFull();
        return verticalLayout;
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        VerticalLayout allOrdersLayout = generateOrdersGrid();
        allOrdersLayout.setSizeFull();
        tabSheet.addTab(getNewOrdersControlTab(),"New orders (" + getNewOrdersCount() + ")").setIcon(VaadinIcons.LIST_SELECT);
        tabSheet.addTab(allOrdersLayout, "History of orders (" + getAllOrdersCount() + ")").setIcon(VaadinIcons.LIST);

        tabSheet.setSizeFull();
        return tabSheet;
    }

    private VerticalLayout getNewOrdersControlTab(){
        VerticalLayout controlLayout = new VerticalLayout();
        MarginInfo marginInfo = new MarginInfo(false, true, true, true);
        controlLayout.setMargin(marginInfo);
        controlLayout.setSpacing(false);
        newOrders.setDriver(driver);
        controlLayout.addComponent(newOrders);
        return controlLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
        newOrders.refreshGrid();
    }

    @PostConstruct
    public void afterPropertiesSet() {
        viewEventBus.subscribe(this,true);
    }

    @EventBusListenerMethod
    public void sendCancelMessage(CancelClientOrderEvent event) {
        Order order = orderService.getOrder(event.getOrderId());
        List<Driver> drivers = driverService.getActiveDrivers();

        if (driver == null || toastr == null || order == null) {
            return;
        }

        if (driver.getObjectId().compareTo(order.getDriverId()) == 0) {
            toastr.toast(ToastBuilder.warning("Order has been canceled").build());
        }
    }

    public void refreshAllOrdersGrid(){
        initDriver();
        newOrders.setDriver(driver);

        tabSheet.getTab(0).setCaption("New orders (" + getNewOrdersCount() + ")");
        tabSheet.getTab(1).setCaption("History of orders (" + getAllOrdersCount() + ")");

        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);
        if(allOrdersList != null) {
            allOrdersGrid.setItems(orderService.getOrdersInfo(allOrdersList));
        }
    }
    public void refresh(){
        initDriver();
        newOrders.setDriver(driver);

        tabSheet.getTab(0).setCaption("New orders (" + getNewOrdersCount() + ")");
        tabSheet.getTab(1).setCaption("History of orders (" + getAllOrdersCount() + ")");

        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);
        if(allOrdersList != null) {
            allOrdersGrid.setItems(orderService.getOrdersInfo(allOrdersList));
        }
        newOrders.refreshContent();
    }

    public void refreshNewOrdersTab(){
        newOrders.refreshContent();
    }

    private int getAllOrdersCount(){
        return allOrdersList.size();
    }

    private int getNewOrdersCount(){
        return newOrders.getOrdersList().size();
    }

    public Driver getDriver(){
        return driver;
    }

    private void initDriver(){
        driver = userDetailsService.getCurrentUser();
    }

    public void setDriver(Driver driver){
        this.driver = driver;
    }

}

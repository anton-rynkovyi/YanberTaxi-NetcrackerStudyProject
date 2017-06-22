package com.netcracker.project.study.vaadin.driver.components.views;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.driver.components.popup.OrderInfoPopUp;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@SpringView(name = OrdersViewForDrivers.VIEW_NAME)
public class OrdersViewForDrivers extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ordersForDriverPage";

    @Autowired
    private NewOrdersTab newOrders;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderInfoPopUp orderInfoPopUp;

    private Driver driver;
    private TabSheet tabSheet;
    private VerticalLayout rootLayout;

    private Panel statusPanel;
    private Panel ratingPanel;

    private Grid allOrdersGrid;
    private List<Order> allOrdersList;

    private Window orderInfoWindow;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public void init() {
        rootLayout = new VerticalLayout();
        initDriver();

        newOrders.setDriver(driver);
        newOrders.setView(this);
        newOrders.init();

        tabSheet = getTabSheet();

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        statusPanel = getDriverStatus();
        changeStatusIcon();

        ratingPanel = getDriverRating();
        ratingPanel.setIcon(VaadinIcons.STAR_O);

        horizontalLayout.addComponents(statusPanel,ratingPanel);
        horizontalLayout.setSizeFull();

        rootLayout.addComponent(horizontalLayout);
        rootLayout.addComponent(tabSheet);

        addComponent(rootLayout);
    }

    private void changeStatusIcon(){
        if(driver.getStatus().equals(DriverStatusList.FREE)){
            statusPanel.setIcon(VaadinIcons.COFFEE);
        }
        if(driver.getStatus().equals(DriverStatusList.ON_CALL)){
            statusPanel.setIcon(VaadinIcons.TAXI);
        }
        if(driver.getStatus().equals(DriverStatusList.PERFORMING_ORDER)){
            statusPanel.setIcon(VaadinIcons.ROAD);
        }
        if(driver.getStatus().equals(DriverStatusList.OFF_DUTY)){
            statusPanel.setIcon(VaadinIcons.HOME_O);
        }
    }

    private void initOrderInfoWindow() {
        orderInfoWindow = new Window(" Information about the order");
        orderInfoWindow.setIcon(VaadinIcons.INFO_CIRCLE);
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(orderInfoPopUp);

        Button okButton = new Button("OK");
        okButton.addClickListener(event->{
            orderInfoWindow.close();
        });
        verticalLayout.addComponent(okButton);
        verticalLayout.setComponentAlignment(okButton,Alignment.MIDDLE_CENTER);
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
        horizontalLayout.setComponentAlignment(viewOrderButton, Alignment.BOTTOM_LEFT);
        return horizontalLayout;
    }

    public Driver getDriver(){
        return driver;
    }

    private void initDriver(){
        driver = userDetailsService.getCurrentUser();
    }

    private Panel getDriverStatus(){
        Panel panel = new Panel("Your status");
        Label statusInfo = new Label("<b>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(statusInfo);
        horizontalLayout.setComponentAlignment(statusInfo,Alignment.MIDDLE_CENTER);
        panel.setContent(horizontalLayout);

        return panel;
    }

    private Panel getDriverRating(){
        Panel panel = new Panel("Your rating");
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        int rating = 3;
        if(driver.getRating() != null){
            rating = driver.getRating().intValue();
        }

        for(int i = 0; i < rating; i++ ){
            Label starIconLabel = new Label();
            starIconLabel.setIcon(VaadinIcons.STAR);
            horizontalLayout.addComponent(starIconLabel);
            horizontalLayout.setComponentAlignment(starIconLabel,Alignment.MIDDLE_CENTER);
        }
        horizontalLayout.setSizeFull();
        panel.setContent(horizontalLayout);

        return panel;
    }


    public void setDriver(Driver driver){
        this.driver = driver;
    }

    private VerticalLayout generateOrdersGrid() {
        VerticalLayout verticalLayout = new VerticalLayout();
        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);

        if(allOrdersList != null) {
            Grid<OrderInfo> ordersGrid = new Grid<>();
            ordersGrid.setSizeFull();
            List<OrderInfo> ordersInfo = orderService.getOrdersInfo(allOrdersList);

            ordersGrid.setItems(ordersInfo);

            ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("#");
            ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
            ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
            ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
            ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");
            allOrdersGrid = ordersGrid;

            HorizontalLayout viewOrderButtonLayout = initViewOrderButton();
            verticalLayout.addComponents(allOrdersGrid,viewOrderButtonLayout);
        }else{
            Label noOrdersLabel = new Label("<h1>Your history of orders is empty yet.</h1>",ContentMode.HTML);
            Panel panel = new Panel();
            panel.setContent(noOrdersLabel);
            verticalLayout.addComponent(panel);
        }

        return verticalLayout;
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();

        VerticalLayout allOrdersLayout = generateOrdersGrid();
        tabSheet.addTab(getNewOrdersControlTab(),"New orders (" + getNewOrdersCount() + ")").setIcon(VaadinIcons.LIST_SELECT);
        tabSheet.addTab(allOrdersLayout, "History of orders (" + getAllOrdersCount() + ")").setIcon(VaadinIcons.LIST);

        tabSheet.setHeight(100,Unit.PERCENTAGE);

        return tabSheet;
    }

    public void Refresh(){
        initDriver();
        newOrders.setDriver(driver);

        Label statusLabel = new Label("<b>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</b>", ContentMode.HTML);
        HorizontalLayout statusLayout = new HorizontalLayout();
        statusLayout.setSizeFull();
        statusLayout.addComponent(statusLabel);
        statusLayout.setComponentAlignment(statusLabel,Alignment.MIDDLE_CENTER);
        statusPanel.setContent(statusLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        changeStatusIcon();
        int rating = 3;

        if(driver.getRating() != null){
            rating = (int)driver.getRating().doubleValue();
        }

        for(int i = 0; i < rating; i++ ){
            Label starIconLabel = new Label();
            starIconLabel.setIcon(VaadinIcons.STAR);
            horizontalLayout.addComponent(starIconLabel);
            horizontalLayout.setComponentAlignment(starIconLabel,Alignment.MIDDLE_CENTER);
        }
        horizontalLayout.setSizeFull();
        ratingPanel.setContent(horizontalLayout);

        tabSheet.getTab(0).setCaption("New orders (" + getNewOrdersCount() + ")");
        tabSheet.getTab(1).setCaption("History of orders (" + getAllOrdersCount() + ")");


        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);
        if(allOrdersList != null) {
            allOrdersGrid.setItems(orderService.getOrdersInfo(allOrdersList));
        }
    }


    private int getAllOrdersCount(){
        return allOrdersList.size();
    }

    private int getNewOrdersCount(){
        return newOrders.getOrdersList().size();
    }


    private VerticalLayout getNewOrdersControlTab(){
        VerticalLayout controlLayout = new VerticalLayout();
        newOrders.setView(this);
        newOrders.setDriver(driver);
        controlLayout.addComponent(newOrders);
        return controlLayout;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }


}

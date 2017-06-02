package com.netcracker.project.study.vaadin.driver.components.grids;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;


@SpringComponent
public class NewOrdersGrid extends CustomComponent {

    @Autowired
    OrderService orderService;

    @Autowired
    DriverService driverService;

    OrdersViewForDrivers view;

    private Grid<OrderInfo> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    @PostConstruct
    public void init() {
        ordersGrid = generateOrdersGrid();
        componentLayout = getFilledComponentLayout();
        setButton();
        setGridSettings(ordersGrid);
        setCompositionRoot(componentLayout);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponents(ordersGrid);
        return componentLayout;
    }

    private HorizontalLayout setButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        Button takeOrderButton = new Button("Take", FontAwesome.CAB);
        takeOrderButton.addClickListener(event->{
            if(!ordersGrid.asSingleSelect().isEmpty()){
                OrderInfo order = ordersGrid.asSingleSelect().getValue();
                driverService.acceptOrder(order.getObjectId(), BigInteger.valueOf(1));
                refreshGrid();
                view.Refresh();
            }
        });


        horizontalLayout.addComponent(takeOrderButton);
        horizontalLayout.setComponentAlignment(takeOrderButton,Alignment.BOTTOM_LEFT);
        componentLayout.addComponent(takeOrderButton);
        return horizontalLayout;
    }

    private void refreshGrid(){
        ordersList =  orderService.getOrders(Order.NEW);
        ordersGrid.setItems(orderService.getOrdersInfo(ordersList));
    }

    private Grid<OrderInfo> generateOrdersGrid() {
        Grid<OrderInfo> ordersGrid = new Grid<>();
        ordersList = orderService.getOrders(Order.NEW);

        List<OrderInfo>ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("#");
        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
        ordersGrid.addColumn(OrderInfo::getDriverName).setCaption("Driver");
        ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
        ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
        ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");

        return ordersGrid;
    }

    public void setView(OrdersViewForDrivers view){
        this.view = view;
    }

    private void setGridSettings(Grid<OrderInfo> ordersGrid) {
        ordersGrid.setSizeFull();
    }

    public Grid<OrderInfo> getOrdersGrid() {
        return ordersGrid;
    }

    public List getOrdersList() {
        return ordersList;
    }

}

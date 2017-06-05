package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.popup.AdminOrderInfoPopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
public class OrdersGrid extends CustomComponent {

    @Autowired AdminService adminService;

    private Grid<Order> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    private Window orderInfoWindow;

    @Autowired
    AdminOrderInfoPopUp orderInfoPopUp;

    @PostConstruct
    public void init() {
        ordersGrid = generateOrdersGrid();
        initOrderInfoWindow();
        componentLayout = getFilledComponentLayout();
        componentLayout.addComponent(getButtons());
        setGridSettings(ordersGrid);
        setCompositionRoot(componentLayout);
    }

    private void initOrderInfoWindow() {
        orderInfoWindow = new Window("Information about the order");
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);
        orderInfoWindow.setContent(orderInfoPopUp);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setSizeFull();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponents(ordersGrid);

        return componentLayout;
    }

    private Grid<Order> generateOrdersGrid() {

        Grid<Order> ordersGrid = new Grid<>();
        ordersList = adminService.allModelsAsList(Order.class);
        ordersGrid.setItems(ordersList);

        ordersGrid.addColumn(Order::getObjectId).setCaption("№");
        ordersGrid.addColumn(Order -> Order.getDriverOnOrder() != null ? Order.getDriverOnOrder().getFirstName() + " " +
                Order.getDriverOnOrder().getLastName() : "").setCaption("Driver");
        ordersGrid.addColumn(Order -> Order.getClientOnOrder() != null ? Order.getClientOnOrder().getFirstName() + " " +
                Order.getClientOnOrder().getLastName() : "").setCaption("Client");
        ordersGrid.addColumn(Order::getStatus).setCaption("Status");
        ordersGrid.addColumn(Order::getCost).setCaption("Cost");
        ordersGrid.addColumn(Order::getDistance).setCaption("Distance");
        ordersGrid.addColumn(Order::getDriverRating).setCaption("Driver rating");
        //ordersGrid.addColumn(Order::getDriverMemo).setCaption("Driver comment");

        return ordersGrid;
    }

    private HorizontalLayout getButtons(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);
        Button viewOrderButton = new Button("View order", FontAwesome.INFO);
        viewOrderButton.addClickListener(event->{
            if(!ordersGrid.asSingleSelect().isEmpty()){
                Order order = ordersGrid.asSingleSelect().getValue();
                orderInfoPopUp.init(order);
                UI.getCurrent().addWindow(orderInfoWindow);
            }
        });
        horizontalLayout.addComponent(viewOrderButton);
        horizontalLayout.setComponentAlignment(viewOrderButton,Alignment.BOTTOM_LEFT);
        return horizontalLayout;
    }

    private void setGridSettings(Grid<Order> ordersGrid) {
        ordersGrid.setSizeFull();
    }

    public Grid<Order> getOrdersGrid() {
        return ordersGrid;
    }

    public List getOrdersList() {
        return ordersList;
    }

}


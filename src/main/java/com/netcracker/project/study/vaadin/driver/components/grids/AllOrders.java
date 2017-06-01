
package com.netcracker.project.study.vaadin.driver.components.grids;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.components.popup.OrderInfoPopUp;
import com.netcracker.project.study.vaadin.driver.pojos.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringComponent
public class AllOrders extends CustomComponent {

    @Autowired AdminService adminService;

    @Autowired OrderService orderService;

    private Grid<OrderInfo> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    private Window orderInfoWindow;

    @Autowired OrderInfoPopUp orderInfoPopUp;

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
        orderInfoWindow = new Window(" Information about the order");
        orderInfoWindow.setIcon(FontAwesome.INFO_CIRCLE);
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);
        orderInfoWindow.setContent(orderInfoPopUp);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponents(ordersGrid);

        return componentLayout;
    }

    private Grid<OrderInfo> generateOrdersGrid() {

        Grid<OrderInfo> ordersGrid = new Grid<>();
        ordersList = adminService.allModelsAsList(Order.class);

        List<OrderInfo>ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
        ordersGrid.addColumn(OrderInfo::getDriverName).setCaption("Driver");
        ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
        ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
        ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");

        return ordersGrid;
    }

    private HorizontalLayout getButtons(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        Button viewOrderButton = new Button("View order", FontAwesome.INFO);
        viewOrderButton.addClickListener(event->{
            if(!ordersGrid.asSingleSelect().isEmpty()){
                OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();
                orderInfoPopUp.init(orderInfo);
                UI.getCurrent().addWindow(orderInfoWindow);
            }
        });

        horizontalLayout.addComponent(viewOrderButton);
        horizontalLayout.setComponentAlignment(viewOrderButton,Alignment.BOTTOM_LEFT);
        return horizontalLayout;
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


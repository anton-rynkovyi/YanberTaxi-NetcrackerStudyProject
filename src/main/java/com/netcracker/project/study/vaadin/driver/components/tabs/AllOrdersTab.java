
package com.netcracker.project.study.vaadin.driver.components.tabs;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.services.impl.DriverServiceImpl;
import com.netcracker.project.study.vaadin.driver.components.popup.OrderInfoPopUp;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.pojos.*;
import com.vaadin.data.HasValue;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@ViewScope
@SpringComponent
public class AllOrdersTab extends CustomComponent {

    @Autowired
    AdminService adminService;

    @Autowired
    OrderService orderService;

    OrdersViewForDrivers view;

    private Grid<OrderInfo> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    private Window orderInfoWindow;

    @Autowired
    OrderInfoPopUp orderInfoPopUp;

    Driver driver;

    NativeSelect statusSelect;

    public OrderInfoPopUp getOrderInfoPopUp() {
        return orderInfoPopUp;
    }


    @PostConstruct
    public void init() {
        ordersGrid = generateOrdersGrid();
        initStatusSelect();
        initOrderInfoWindow();

        componentLayout = getFilledComponentLayout();
        componentLayout.addComponent(getButtons());

        setGridSettings(ordersGrid);
        setCompositionRoot(componentLayout);
    }

    private void initStatusSelect() {
        statusSelect = new NativeSelect("Order status");
        List<String> statusList = new ArrayList<>();
        statusList.add("All");
        statusList.add("Accepted");
        statusList.add("Performing");
        statusList.add("Performed");
        statusList.add("Canceled");
        statusSelect.setItems(statusList);

        statusSelect.setEmptySelectionAllowed(false);
        statusSelect.setValue(statusList.get(0));

        statusSelect.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
                switch (valueChangeEvent.getValue().toString()) {
                    case "All":
                        ordersGrid.setItems(orderService.getOrdersInfoByDriverId(driver.getObjectId(), null));
                        break;
                    case "Accepted":
                        ordersGrid.setItems(orderService.getOrdersInfoByDriverId(driver.getObjectId(), OrderStatus.ACCEPTED));
                        break;
                    case "Performing":
                        ordersGrid.setItems(orderService.getOrdersInfoByDriverId(driver.getObjectId(), OrderStatus.PERFORMING));
                        break;
                    case "Performed":
                        ordersGrid.setItems(orderService.getOrdersInfoByDriverId(driver.getObjectId(), OrderStatus.PERFORMED));
                        break;
                    case "Canceled":
                        ordersGrid.setItems(orderService.getOrdersInfoByDriverId(driver.getObjectId(), OrderStatus.CANCELED));
                        break;
                }
            }
        });


    }

    private void initOrderInfoWindow() {
        orderInfoWindow = new Window(" Information about the order");
        orderInfoWindow.setIcon(FontAwesome.INFO_CIRCLE);
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);
        orderInfoWindow.setContent(orderInfoPopUp);
    }

    public void setView(OrdersViewForDrivers view) {
        this.view = view;
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponent(statusSelect);
        componentLayout.addComponents(ordersGrid);

        return componentLayout;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void refreshGrid() {
        ordersList = orderService.getOrdersByDriverId(driver.getObjectId(),null);
        ordersGrid.setItems(orderService.getOrdersInfo(ordersList));
    }

    private Grid<OrderInfo> generateOrdersGrid() {
        Grid<OrderInfo> ordersGrid = new Grid<>();
        ordersList = adminService.allModelsAsList(Order.class);

        List<OrderInfo> ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("#");
        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
        ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
        ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
        ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");

        return ordersGrid;
    }

    private HorizontalLayout getButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        Button viewOrderButton = new Button("View order", FontAwesome.INFO);
        viewOrderButton.addClickListener(event -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();
                orderInfoPopUp.init(orderInfo);
                UI.getCurrent().addWindow(orderInfoWindow);
            }
        });

        componentLayout.addComponents(ordersGrid);
        horizontalLayout.addComponent(viewOrderButton);
        horizontalLayout.setComponentAlignment(viewOrderButton, Alignment.BOTTOM_LEFT);
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


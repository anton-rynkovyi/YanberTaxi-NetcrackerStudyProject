package com.netcracker.project.study.vaadin.driver.components.tabs;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.components.popup.OrderInfoForNewOrders;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;


@SpringComponent
public class NewOrdersTab extends CustomComponent {

    @Autowired
    OrderService orderService;

    @Autowired
    DriverService driverService;

    @Autowired
    OrderInfoForNewOrders orderInfoForNewOrders;

    OrdersViewForDrivers view;

    private Grid<OrderInfo> ordersGrid;
    private HorizontalLayout componentLayout;
    private List<Order> ordersList;
    private Button takeOrderButton;

    private Panel leftPanel;
    private Panel rightPanel;

    Window window;

    Driver driver;

    @PostConstruct
    public void init() {
        initRootLayout();
        ordersGrid = generateOrdersGrid();
        orderInfoForNewOrders.init(null);
        setTakeOrderButton();
        leftPanel = getFilledHighPanel();
        rightPanel = getFilledLowPanel(takeOrderButton);
        setGridSettings(ordersGrid);
        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(leftPanel, rightPanel);
        componentLayout.addComponent(horizontalSplitPanel);
        componentLayout.setSizeFull();

        setCompositionRoot(componentLayout);
    }

    public void setDriver(Driver driver){
        this.driver = driver;
    }

    private void initRootLayout() {
        componentLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(true);
    }

    private Panel getFilledHighPanel() {
        Panel panel = new Panel("Available orders");
        panel.setSizeFull();
        panel.setContent(ordersGrid);
        return panel;
    }

    private Panel getFilledLowPanel(Button button) {
        Panel panel = new Panel("Selected order");
        panel.setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(orderInfoForNewOrders);
        verticalLayout.addComponent(button);
        verticalLayout.setComponentAlignment(button, Alignment.BOTTOM_RIGHT);
        panel.setContent(verticalLayout);

        return panel;
    }

    private void refreshContent() {
        refreshGrid();
        orderInfoForNewOrders.init(null);
        takeOrderButton.setEnabled(false);
        view.Refresh();
    }

    private void initSuccessfulOrderWindow(String client){
        window = new Window(" Information");
        window.setIcon(FontAwesome.INFO_CIRCLE);
        window.center();
        window.setModal(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        Label content = new Label("<b>The order has been successfully taken</b> ", ContentMode.HTML);
        Label clientNameLabel = new Label("<b>Your client is: </b>" + client, ContentMode.HTML);
        verticalLayout.addComponents(content,clientNameLabel);
        window.setContent(verticalLayout);
    }

    private void initUnsuccessfulOrderWindow(){
        window = new Window(" Information");
        window.setIcon(FontAwesome.INFO_CIRCLE);
        window.center();
        window.setModal(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        Label content = new Label("<b>You can't take this order. To have the possibility to take orders, your status must be \"free\"</b> ", ContentMode.HTML);
        Label statusLabel = new Label("<b>Your status is: </b>" + DriverStatusEnum.getStatusValue(driver.getStatus()), ContentMode.HTML);
        verticalLayout.addComponents(content,statusLabel);
        window.setContent(verticalLayout);
    }

    private void setTakeOrderButton() {
        takeOrderButton = new Button("Take", FontAwesome.CAB);
        takeOrderButton.addClickListener(event -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                List<Order>currentOrder = orderService.getCurrentOrderByDriverId(driver.getObjectId());
                if(currentOrder.size() == 0){
                    OrderInfo order = ordersGrid.asSingleSelect().getValue();
                    driverService.acceptOrder(order.getObjectId(), driver.getObjectId());
                    initSuccessfulOrderWindow(order.getClientName());
                    refreshContent();
                    UI.getCurrent().addWindow(window);
                }else{
                    initUnsuccessfulOrderWindow();
                    refreshContent();
                    UI.getCurrent().addWindow(window);
                }
            }
        });
        takeOrderButton.setEnabled(false);
    }

    private void refreshGrid() {
        ordersList = orderService.getOrders(OrderStatus.NEW);
        ordersGrid.setItems(orderService.getOrdersInfo(ordersList));
    }

    private Grid<OrderInfo> generateOrdersGrid() {
        Grid<OrderInfo> ordersGrid = new Grid<>();
        ordersList = orderService.getOrders(OrderStatus.NEW);

        List<OrderInfo> ordersInfo = orderService.getOrdersInfo(ordersList);

        ordersGrid.setItems(ordersInfo);

        ordersGrid.addColumn(OrderInfo::getQueueN).setCaption("#");
        ordersGrid.addColumn(OrderInfo::getClientName).setCaption("Client");
        ordersGrid.addColumn(OrderInfo::getDriverName).setCaption("Driver");
        ordersGrid.addColumn(OrderInfo::getStatus).setCaption("Status");
        ordersGrid.addColumn(OrderInfo::getCost).setCaption("Cost");
        ordersGrid.addColumn(OrderInfo::getDistance).setCaption("Distance");

        ordersGrid.addSelectionListener(selectionEvent -> {
            if (!ordersGrid.asSingleSelect().isEmpty()) {
                OrderInfo orderInfo = ordersGrid.asSingleSelect().getValue();
                orderInfoForNewOrders.init(orderInfo);
                takeOrderButton.setEnabled(true);
            }
        });

        return ordersGrid;
    }

    public void setView(OrdersViewForDrivers view) {
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

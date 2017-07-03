package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.admin.components.popup.AdminOrderInfoPopUp;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.*;

@SpringComponent
@Scope(value = "prototype")
public class OrdersGrid extends CustomComponent {

    @Autowired AdminService adminService;

    @Autowired
    OrderService orderService;

    private Grid<Order> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    private Window orderInfoWindow;

    private NativeSelect statusSelect;

    private HorizontalLayout filtersLayout;

    private TextField fieldFilter;

    @Autowired
    AdminOrderInfoPopUp orderInfoPopUp;

    public void init() {
        initFilters();
        ordersGrid = generateOrdersGrid();
        initOrderInfoWindow();
        componentLayout = getFilledComponentLayout();
        componentLayout.addComponent(getButtons());
        setGridSettings(ordersGrid);
        setCompositionRoot(componentLayout);
    }

    private void initFilters(){
        filtersLayout = new HorizontalLayout();
        filtersLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        initStatusChooser();
        filtersLayout.addComponent(statusSelect);
        //initTextFieldFilter();
//        filtersLayout.addComponent(fieldFilter);
    }

    private void initOrderInfoWindow() {
        orderInfoWindow = new Window("Information about the order");
        orderInfoWindow.setIcon(VaadinIcons.INFO);
        orderInfoWindow.center();
        orderInfoWindow.setModal(true);
        orderInfoWindow.setResizable(false);
        orderInfoWindow.setContent(orderInfoPopUp);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setSizeFull();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponent(filtersLayout);
        componentLayout.addComponents(ordersGrid);

        return componentLayout;
    }

    private void initTextFieldFilter() {
        fieldFilter = new TextField();
        fieldFilter.addValueChangeListener(this::onNameFilterTextChange);
        fieldFilter.setPlaceholder("Search");
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Order> dataProvider = (ListDataProvider<Order>) ordersGrid.getDataProvider();
        dataProvider.setFilter(Order::getDriverMemo, s -> caseInsensitiveContains(s, event.getValue()));
    }


    private Boolean caseInsensitiveContains(String where, String what) {
        if (where != null) {
            return where.toLowerCase().contains(what.toLowerCase());
        }
        return false;
    }


    private void initStatusChooser() {
        statusSelect = new NativeSelect("Status filter");
        List<String> statusList = Arrays.asList("All",
                OrderStatusEnum.getStatusValue(OrderStatus.NEW),
                OrderStatusEnum.getStatusValue(OrderStatus.ACCEPTED),
                OrderStatusEnum.getStatusValue(OrderStatus.PERFORMING),
                OrderStatusEnum.getStatusValue(OrderStatus.PERFORMED),
                OrderStatusEnum.getStatusValue(OrderStatus.CANCELED));
        statusSelect.setItems(statusList);

        statusSelect.setEmptySelectionAllowed(false);
        statusSelect.setSelectedItem(statusList.get(0));

        statusSelect.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
                switch (valueChangeEvent.getValue().toString()) {
                    case "All":
                        ordersGrid.setItems(orderService.getAllOrders());
                        break;
                    case "New":
                        ordersGrid.setItems(orderService.getOrders(OrderStatus.NEW));
                        break;
                    case "Accepted":
                        ordersGrid.setItems(orderService.getOrders(OrderStatus.ACCEPTED));
                        break;
                    case "Performing":
                        ordersGrid.setItems(orderService.getOrders(OrderStatus.PERFORMING));
                        break;
                    case "Performed":
                        ordersGrid.setItems(orderService.getOrders(OrderStatus.PERFORMED));
                        break;
                    case "Canceled":
                        ordersGrid.setItems(orderService.getOrders(OrderStatus.CANCELED));
                        break;
                }
            }
        });
    }

    private Grid<Order> generateOrdersGrid() {
        Grid<Order> ordersGrid = new Grid<>();
        ordersGrid.addColumn(Order::getObjectId).setCaption("№").setId("№");
        ordersGrid.addColumn(Order -> Order.getDriverOnOrder() != null ? Order.getDriverOnOrder().getFirstName() + " " +
                Order.getDriverOnOrder().getLastName() : "").setCaption("Driver");
        ordersGrid.addColumn(Order -> Order.getClientOnOrder() != null ? Order.getClientOnOrder().getFirstName() + " " +
                Order.getClientOnOrder().getLastName() : "").setCaption("Client");
        ordersGrid.addColumn(order -> OrderStatusEnum.getStatusValue(order.getStatus())).setCaption("Status");
        ordersGrid.addColumn(order -> order.getCost() != null ? order.getCost() + " hrn" : "").setCaption("Cost");
        ordersGrid.addColumn(order -> order.getDistance() != null ? order.getDistance() + " km" : "").setCaption("Distance");
        ordersGrid.addColumn(order -> order.getDriverRating()).setCaption("Driver rating");

        ordersGrid.sort("№", SortDirection.DESCENDING);

        return ordersGrid;
    }

    private HorizontalLayout getButtons(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);
        Button viewOrderButton = new Button("View order", VaadinIcons.INFO);
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

    public void refreshGrid() {
        ordersList = adminService.allModelsAsList(Order.class);
        ordersGrid.setItems(ordersList);
        //init();
    }

}


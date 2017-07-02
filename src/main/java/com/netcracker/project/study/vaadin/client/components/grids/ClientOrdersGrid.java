package com.netcracker.project.study.vaadin.client.components.grids;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.client.components.popup.ClientOrderInfoPopUp;
import com.vaadin.annotations.Push;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.Toastr;

import java.util.List;


@ViewScope
@SpringComponent
public class ClientOrdersGrid  extends CustomComponent {

    @Autowired
    OrderService orderService;

    @Autowired
    ClientOrderInfoPopUp orderInfoPopUp;

    private Grid<Order> clientOrderGrid;

    private VerticalLayout componentLayout;

    private Window orderInfoWindow;

    private Toastr toastr;

    private Client client;

    public void init() {
        clientOrderGrid = generateClientOrderGrid();
        initOrderInfoWindow();
        componentLayout = getFilledComponentLayout();
        componentLayout.addComponent(getButtons());

        toastr = new Toastr();
        componentLayout.addComponent(toastr);
        setGridSettings(clientOrderGrid);
        setCompositionRoot(componentLayout);
    }

    public void setClient(Client client) {this.client = client;}

    private Grid<Order> generateClientOrderGrid() {
        Grid<Order> ordersGrid = new Grid<>();
        List<Order> clientOrderList = orderService.getOrdersByClientId(client.getObjectId());
        orderService.getOrdersInfo(clientOrderList);
        ordersGrid.setItems(clientOrderList);

        ordersGrid.addColumn(Order::getObjectId).setCaption("№").setId("№");
        ordersGrid.addColumn(Order -> Order.getName().substring(0, Order.getName().indexOf(" "))).setCaption("Start point");
        ordersGrid.addColumn(Order -> Order.getName().substring((Order.getName().indexOf("- "))+1)).setCaption("Destination");
        ordersGrid.addColumn(Order -> Order.getDriverOnOrder() != null ? Order.getDriverOnOrder().getFirstName() + " " +
                Order.getDriverOnOrder().getLastName() : "absent").setCaption("Driver");
        ordersGrid.addColumn(order -> OrderStatusEnum.getStatusValue(order.getStatus())).setCaption("Status");
        ordersGrid.addColumn(Order::getCost).setCaption("Cost");
        ordersGrid.addColumn(Order::getDistance).setCaption("Distance");

        ordersGrid.sort("№", SortDirection.DESCENDING);

        return ordersGrid;
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

        componentLayout.addComponents(clientOrderGrid);

        return componentLayout;
    }

    private HorizontalLayout getButtons(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);
        Button viewOrderButton = new Button("View order", VaadinIcons.INFO);
        viewOrderButton.addClickListener(event->{
            if(!clientOrderGrid.asSingleSelect().isEmpty()){
                Order order = clientOrderGrid.asSingleSelect().getValue();
                orderInfoPopUp.init(order, toastr);
                UI.getCurrent().addWindow(orderInfoWindow);
            }
        });
        horizontalLayout.addComponent(viewOrderButton);
        horizontalLayout.setComponentAlignment(viewOrderButton,Alignment.BOTTOM_LEFT);
        return horizontalLayout;
    }

    private void setGridSettings(Grid<Order> ordersGrid) {
        ordersGrid.setSizeFull();
        ordersGrid.setHeightByRows(10);
    }
}

package com.netcracker.project.study.vaadin.client.views;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
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

    Client client;

    private Window window;

    @PostConstruct
    public void init() {
        initfakeClient();
        clientCurrentOrderGrid.setClient(client);
        clientCurrentOrderGrid.init();
        orderMaker.setClient(client);
        clientOrdersGrid.setClient(client);
        clientOrdersGrid.init();

        Label label = new Label("<h2>YanberTaxi</h2>", ContentMode.HTML);
        addComponent(label);
        setComponentAlignment(label, Alignment.TOP_RIGHT);

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

    public void initfakeClient(){
        this.client = facade.getOne(BigInteger.valueOf(154), Client.class);
    }

    private HorizontalLayout setMainButtons() {
        HorizontalLayout root = new HorizontalLayout();

        Button newOrder = new Button("Make new order", VaadinIcons.TAXI);
        newOrder.addStyleName(MaterialTheme.BUTTON_HUGE + " " + MaterialTheme.BUTTON_ICON_ALIGN_RIGHT + " "
                                + MaterialTheme.BUTTON_FRIENDLY);
        newOrder.setDescription("Press this button to make your order");
        Button.ClickListener orderCreaterListener = new NewOrderCreater();
        newOrder.addClickListener(orderCreaterListener);

        Button cancelOrder = new Button("Cancel the order", VaadinIcons.CLOSE_BIG);
        cancelOrder.addStyleName(MaterialTheme.BUTTON_HUGE + " " + MaterialTheme.BUTTON_ICON_ALIGN_RIGHT + " "
                                    + MaterialTheme.BUTTON_DANGER);
        cancelOrder.setDescription("Press this button to cancel your last order");
        Button.ClickListener orderCancelListener = new OrderCancelListener();
        cancelOrder.addClickListener(orderCancelListener);

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
        Label pointsInfo = new Label("<b>" + client.getPoints() + "</b>", ContentMode.HTML);
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
        }
    }

    class OrderCancelListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            List<Order> orderList = orderService.getActiveOrdersByClientId(client.getObjectId());
            if (orderList.size()>0) {
                for (Order order : orderList) {
                    orderService.changeStatus(OrderStatus.CANCELED, order.getObjectId());
                }
                initWindow("<b>Current order canceled</b> ");
                UI.getCurrent().addWindow(window);
                clientOrdersGrid.init();
                clientCurrentOrderGrid.init();
            } else {
                initWindow("<b>You don't have an active order</b> ");
                UI.getCurrent().addWindow(window);
            }
        }
    }

    private void initWindow(String text){
        window = new Window(" Information");
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

    }
}

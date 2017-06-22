package com.netcracker.project.study.vaadin.client.components.popup;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ClientOrderInfoPopUp extends VerticalLayout {

    @Autowired
    OrderService orderService;

    private Order order;

    public void init(Order order) {
        this.order = order;
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);
        rootLayout.addComponent(setOrderInfoLayout());
        addComponent(rootLayout);
    }

    private VerticalLayout setOrderInfoLayout() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);

        VerticalLayout allInfo = new VerticalLayout();
        HorizontalLayout routesAndDetalies = new HorizontalLayout();
        VerticalLayout orderForm = new VerticalLayout();

        Label orderNumber = new Label("№: " + order.getObjectId());

        Driver driver = order.getDriverOnOrder();
        String driverFullName = driver != null ? driver.getFirstName() + " " + driver.getLastName() : "absent";
        Label driverName = new Label("Driver: <i>" + driverFullName + "</i>", ContentMode.HTML);

        Label status = new Label("Order status: <i>" + OrderStatusEnum.getStatusValue(order.getStatus()) + "</i>", ContentMode.HTML);

        BigDecimal orderCost = order.getCost() != null ? order.getCost() : BigDecimal.ZERO;
        Label cost = new Label("Cost: <i>" + orderCost + " hrn" + "</i>", ContentMode.HTML);

        BigInteger orderDistance = order.getDistance() != null ? order.getDistance() : BigInteger.ZERO;
        Label distance = new Label("Distance: <i>" + orderDistance + " km</i>", ContentMode.HTML);

        Label driverRating = new Label("Driver rating: <i>" +
                (order.getDriverRating() != null ? order.getDriverRating() : "N/A")+
                "</i>", ContentMode.HTML);

        orderForm.addComponents(orderNumber, driverName, status, cost, distance, driverRating);

        Label[] routes = getRoutes(order.getObjectId());
        VerticalLayout allRoutes = getLayoutWithRoutes(routes);

        routesAndDetalies.addComponents(orderForm, allRoutes);
        allInfo.addComponent(routesAndDetalies);

        TextArea textArea = new TextArea();
        textArea.setWidth(100, Unit.PERCENTAGE);
        textArea.setEnabled(false);
        textArea.setCaptionAsHtml(true);
        textArea.setCaption("Comment for Driver");
        if (order.getDriverMemo() != null) {
            textArea.setValue(order.getDriverMemo());
        } else textArea.setValue("You are not leaving comment for this driver");
        allInfo.addComponent(textArea);


        Panel orderPanel = new Panel(allInfo);
        orderPanel.setWidth(600, Unit.PIXELS);
        orderPanel.setHeightUndefined();
        orderPanel.setWidthUndefined();
        rootLayout.addComponent(orderPanel);

        return rootLayout;
    }

    private Label[] getRoutes(BigInteger orderId){
        List<Route> routes = orderService.getRoutes(orderId);
        Label[] routesLables = new Label[routes.size()];

        for(int i = 0; i < routes.size(); i++){
            String value = "<b>Address " + (i+1) + ": </b>";
            if (i == 0) value = "<b>Starting Point (Address 1): </b>";
            if (i == routes.size() - 1) value = "<b>Destination (Address " + routes.size() + "): </b>";
            Label label = new Label(value + routes.get(i).getCheckPoint(), ContentMode.HTML);
            label.setIcon(VaadinIcons.MAP_MARKER);
            if (i == 0) label.setIcon(VaadinIcons.HOME_O);
            if (i == routes.size() - 1) label.setIcon(VaadinIcons.FLAG_CHECKERED);
            routesLables[i] = label;
        }

        return routesLables;
    }

    private VerticalLayout getLayoutWithRoutes(Label[] routes) {
        VerticalLayout layoutWithRoutes = new VerticalLayout();
        int numOfRoutes = routes.length;
        if ((numOfRoutes % 2) == 0) {
            HorizontalLayout[] splitPanels = new HorizontalLayout[numOfRoutes/2];
            for (int i = 0, j = 0; i < splitPanels.length; i++, j += 2) {
                splitPanels[i] = new HorizontalLayout();
                splitPanels[i].setSizeFull();
                splitPanels[i].addComponents(routes[j], routes[j+1]);
                layoutWithRoutes.addComponent(splitPanels[i]);
                layoutWithRoutes.setComponentAlignment(splitPanels[i], Alignment.TOP_CENTER);
            }
        } else if ((numOfRoutes % 2) == 1) {
            HorizontalLayout[] splitPanels = new HorizontalLayout[numOfRoutes/2];
            for (int i = 0, j = 0; i < splitPanels.length; i++, j += 2) {
                splitPanels[i] = new HorizontalLayout();
                splitPanels[i].addComponents(routes[j], routes[j+1]);
                layoutWithRoutes.addComponent(splitPanels[i]);
                layoutWithRoutes.setComponentAlignment(splitPanels[i], Alignment.TOP_CENTER);
            }
            HorizontalLayout panel = new HorizontalLayout();
            panel.addComponent(routes[numOfRoutes-1]);
            layoutWithRoutes.addComponent(panel);
            layoutWithRoutes.setComponentAlignment(panel, Alignment.TOP_CENTER);
        }
        return layoutWithRoutes;
    }
}

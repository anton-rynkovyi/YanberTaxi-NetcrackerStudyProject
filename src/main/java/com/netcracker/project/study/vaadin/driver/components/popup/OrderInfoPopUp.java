package com.netcracker.project.study.vaadin.driver.components.popup;

import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.impl.OrderServiceImpl;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@ViewScope
@SpringComponent
public class OrderInfoPopUp extends VerticalLayout {

    private OrderInfo orderInfo;

    HorizontalLayout rootLayout;
    @Autowired private OrderServiceImpl orderService;


    public void init(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        removeAllComponents();
        initRootLayout();
        initOrderInfoLayout();
        addComponent(rootLayout);
    }

    private void initRootLayout(){
        rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
    }

    private void initOrderInfoLayout(){
        VerticalLayout orderLayout = new VerticalLayout();
        Label driverId = new Label("<b>Driver: </b>"+ orderInfo.getDriverName(), ContentMode.HTML);
        driverId.setIcon(FontAwesome.MALE);
        Label clientId = new Label("<b>Client: </b>" + orderInfo.getClientName(), ContentMode.HTML);
        clientId.setIcon(FontAwesome.MALE);
        Label status = new Label("<b>Status: </b>" + orderInfo.getStatus(), ContentMode.HTML);
        status.setIcon(FontAwesome.ROUBLE);
        Label cost = new Label("<b>Cost: </b>" + orderInfo.getCost(), ContentMode.HTML);
        cost.setIcon(FontAwesome.DOLLAR);
        Label distance = new Label("<b>Distance: </b>" + orderInfo.getDistance(), ContentMode.HTML);
        distance.setIcon(FontAwesome.ARROWS_H);
        orderLayout.addComponents(driverId,clientId,status,cost,distance);

        Panel orderPanel = new Panel("Order", orderLayout);
        orderPanel.setIcon(FontAwesome.MOBILE);
        orderPanel.setWidth(300, Unit.PIXELS);

        VerticalLayout routeLayout = getRoutesLayout();
        Panel routePanel = new Panel("Route", routeLayout);
        routePanel.setIcon(FontAwesome.ROAD);
        routePanel.setWidth(300, Unit.PIXELS);

        rootLayout.addComponent(orderPanel);
        rootLayout.addComponent(routePanel);
    }

    private VerticalLayout getRoutesLayout(){
        VerticalLayout routeLayout= new VerticalLayout();
        List<Route> routes = orderService.getRoutes(orderInfo.getObjectId());

        int i = 0;
        for(Route route:routes){
            Label label = new Label("<b>Address " + i + ": </b>"+ route.getCheckPoint(), ContentMode.HTML);
            label.setIcon(FontAwesome.MAP_MARKER);
            routeLayout.addComponent(label);
            i++;
        }

        return routeLayout;
    }
}

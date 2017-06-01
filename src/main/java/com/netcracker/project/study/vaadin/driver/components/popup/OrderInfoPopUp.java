package com.netcracker.project.study.vaadin.driver.components.popup;

import com.netcracker.project.study.services.impl.OrderServiceImpl;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class OrderInfoPopUp extends VerticalLayout {

    private OrderInfo orderInfo;

    VerticalLayout rootLayout;
    @Autowired private OrderServiceImpl orderService;

    public void init(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        removeAllComponents();
        initRootLayout();
        initOrderInfoLayout();
        addComponent(rootLayout);
    }

    private void initRootLayout(){
        rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
    }

    private void initOrderInfoLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        Label driverId = new Label("<b>Driver: </b>"+ orderInfo.getDriverName(), ContentMode.HTML);
        Label clientId = new Label("<b>Client: </b>" + orderInfo.getClientName(), ContentMode.HTML);
        Label status = new Label("<b>Status: </b>" + orderInfo.getStatus(), ContentMode.HTML);
        Label cost = new Label("<b>Cost: </b>" + orderInfo.getCost(), ContentMode.HTML);
        Label distance = new Label("<b>Distance: </b>" + orderInfo.getDistance(), ContentMode.HTML);
        verticalLayout.addComponents(driverId,clientId,status,cost,distance);
        rootLayout.addComponent(verticalLayout);
    }
}

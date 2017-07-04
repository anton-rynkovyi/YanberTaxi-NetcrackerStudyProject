package com.netcracker.project.study.vaadin.driver.components.popup;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;
import java.math.BigInteger;

@SpringComponent
@Scope(value = "prototype")
public class ResultPopUp extends Window {

    @Autowired
    OrderService orderService;

    private BigDecimal cost;
    private BigDecimal distance;


    public void init(BigDecimal cost, BigDecimal distance) {
        this.cost = cost;
        this.distance = distance;
        setCaption(" Cost of order");
        VerticalLayout verticalLayout = genRootLayout();
        verticalLayout.addComponent(genCostLayout());
        setResizable(false);
        center();
        setIcon(VaadinIcons.MONEY);
        setContent(verticalLayout);
    }

    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(400, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private VerticalLayout genCostLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label distance = new Label("<h3>Distance: " + this.distance.doubleValue() + " km</h3>", ContentMode.HTML);
        Label distanceIcon = new Label();
        distanceIcon.setIcon(VaadinIcons.ROAD);
        Label cost = new Label("<h2>Cost: " + this.cost.doubleValue() + " hrn</h2>", ContentMode.HTML);
        Label costIcon = new Label();
        costIcon.setIcon(VaadinIcons.CASH);
        Button button = new Button("Ok");
        button.addClickListener(event -> {
            close();
        });
        verticalLayout.addComponents(
                new HorizontalLayout(distanceIcon, distance),
                new HorizontalLayout(costIcon, cost),
                new Label(), button);
        return verticalLayout;
    }
}










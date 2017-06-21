package com.netcracker.project.study.vaadin.driver.components.popup;

import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.impl.OrderServiceImpl;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
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

    private void initOrderInfoLayout() {

        VerticalLayout orderLayout = new VerticalLayout();
        if (orderInfo != null) {
            Label header = new Label("<h2><center>General information</center></h2>", ContentMode.HTML);

            Panel generalInfoPanel = new Panel();
            HorizontalLayout clientIdLayout = new HorizontalLayout();
            Label clientId = new Label("<b>Client: </b>" + orderInfo.getClientName(), ContentMode.HTML);
            Label iconLabel = new Label();
            iconLabel.setIcon(VaadinIcons.MALE);
            clientIdLayout.addComponents(iconLabel, clientId);

            HorizontalLayout statusIdLayout = new HorizontalLayout();
            Label status = new Label("<b>Status: </b>" + orderInfo.getStatus(), ContentMode.HTML);
            Label iconStatus = new Label();
            iconStatus.setIcon(VaadinIcons.RHOMBUS);
            statusIdLayout.addComponents(iconStatus, status);

            HorizontalLayout costLayout = new HorizontalLayout();
            Label cost = new Label("<b>Cost: </b>" + orderInfo.getCost(), ContentMode.HTML);
            Label iconCost = new Label();
            iconCost.setIcon(VaadinIcons.CASH);
            costLayout.addComponents(iconCost, cost);

            HorizontalLayout distanceLayout = new HorizontalLayout();
            Label iconDistance = new Label();
            iconDistance.setIcon(VaadinIcons.ARROWS_LONG_H);
            Label distance = new Label("<b>Distance: </b>" + orderInfo.getDistance(), ContentMode.HTML);
            distanceLayout.addComponents(iconDistance, distance);

            HorizontalLayout ratingLayout = new HorizontalLayout();
            Label iconRating = new Label();
            iconRating.setIcon(VaadinIcons.STAR);

            BigInteger rating = orderInfo.getRating();
            if(rating == null){
                rating = BigInteger.valueOf(0);
            }

            Label ratingLabel = new Label("<b>Rating: </b>" + rating.toString(), ContentMode.HTML);
            ratingLayout.addComponents(iconRating,ratingLabel);


            orderLayout.addComponents(header, clientIdLayout, statusIdLayout, costLayout, distanceLayout,ratingLayout);
            generalInfoPanel.setContent(orderLayout);

            rootLayout.addComponent(generalInfoPanel);

            Panel routeInfoPanel = new Panel();

            List<Label> labels = getRoutesLayout();
            Label routeHeader = new Label("<h2><center>Route information</center></h2>", ContentMode.HTML);
            VerticalLayout routeLayout = new VerticalLayout();
            routeLayout.addComponent(routeHeader);
            if (labels.size() == 0) {
                Label noRouteLabel = new Label("No route provided");
                routeLayout.addComponent(noRouteLabel);
            }else{
                for(Label label:labels){
                    routeLayout.addComponent(label);
                }
            }
            routeInfoPanel.setContent(routeLayout);

            rootLayout.addComponent(routeInfoPanel);
        }
    }

    private List<Label> getRoutesLayout(){
        List<Route> routes = orderService.getRoutes(orderInfo.getObjectId());
        List<Label>labels = new ArrayList<>();

        int i = 0;
        for(Route route:routes){
            Label label = new Label("<b>Address " + i + ": </b>"+ route.getCheckPoint(), ContentMode.HTML);
            labels.add(label);
            label.setIcon(VaadinIcons.MAP_MARKER);
            i++;
        }

        return labels;
    }
}

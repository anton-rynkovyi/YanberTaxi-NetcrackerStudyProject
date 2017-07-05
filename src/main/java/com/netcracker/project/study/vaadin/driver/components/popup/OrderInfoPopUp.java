package com.netcracker.project.study.vaadin.driver.components.popup;

import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.impl.OrderServiceImpl;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Scope(value = "prototype")
@SpringComponent
public class OrderInfoPopUp extends VerticalLayout {

    private OrderInfo orderInfo;

    VerticalLayout rootLayout;
    @Autowired private OrderServiceImpl orderService;

    public Button okButton;

    public void init(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        removeAllComponents();
        initRootLayout();
        initOrderInfoLayout();
        setHeightUndefined();
        addComponent(rootLayout);
    }

    private void initRootLayout(){
        rootLayout = new VerticalLayout();
        rootLayout.setHeightUndefined();
    }

    private void initOrderInfoLayout() {

        VerticalLayout orderLayout = new VerticalLayout();
        orderLayout.setSpacing(false);
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

//            HorizontalLayout ratingLayout = new HorizontalLayout();
//            Label iconRating = new Label();
//            iconRating.setIcon(VaadinIcons.STAR);

            BigInteger rating = orderInfo.getRating();
            if(rating == null){
                rating = BigInteger.valueOf(0);
            }

            //Label ratingLabel = new Label("<b>Rating: </b>" + rating.toString(), ContentMode.HTML);
            //ratingLayout.addComponents(iconRating,ratingLabel);


//            orderLayout.addComponents(header, clientIdLayout, statusIdLayout, costLayout, distanceLayout,ratingLayout);
            orderLayout.addComponents(header, clientIdLayout, statusIdLayout, costLayout, distanceLayout);
            generalInfoPanel.setContent(orderLayout);
            generalInfoPanel.setIcon(VaadinIcons.CLIPBOARD_TEXT);


            Panel routeInfoPanel = new Panel();

            List<HorizontalLayout> routes = getRoutesLayout();
            Label routeHeader = new Label("<h2><center>Route information</center></h2>", ContentMode.HTML);
            VerticalLayout routeLayout = new VerticalLayout();
            routeLayout.addComponent(routeHeader);
            if (routes.size() == 0) {
                Label noRouteLabel = new Label("No route provided");
                routeLayout.addComponent(noRouteLabel);
            }else{
                for(HorizontalLayout route:routes){
                    routeLayout.addComponent(route);
                }
            }
            routeLayout.setSizeFull();
            routeLayout.setSpacing(false);

            routeInfoPanel.setContent(routeLayout);
            routeInfoPanel.setIcon(VaadinIcons.ROAD);

            routeInfoPanel.setSizeFull();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponent(generalInfoPanel);
            horizontalLayout.addComponent(routeInfoPanel);

            //TextArea commentLayout = new TextArea();
            //String driverMemo = orderInfo.getDriverMemo();

            //Panel commentPanel = new Panel("Comment");
            //commentPanel.setIcon(VaadinIcons.COMMENT_ELLIPSIS_O);

            //commentPanel.setContent(commentLayout);
            //commentLayout.setSizeFull();
            //commentPanel.setSizeFull();

            /*if(driverMemo == null){
                commentLayout.setValue("Client has not left comment yet.");
            }else{
                commentLayout.setValue(driverMemo);
            }*/

            //commentLayout.setEnabled(false);

            rootLayout.addComponent(horizontalLayout);
            //rootLayout.addComponent(commentPanel);

            okButton = new Button("OK");
            rootLayout.addComponent(okButton);
            rootLayout.setComponentAlignment(okButton,Alignment.BOTTOM_CENTER);
        }
    }

    private List<HorizontalLayout> getRoutesLayout(){
        List<Route> routes = orderService.getRoutes(orderInfo.getObjectId());
        List<HorizontalLayout>routesLayout = new ArrayList<>();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("<b>Departure " + ": </b>"+ routes.get(0).getCheckPoint(), ContentMode.HTML);
        Label mapMarkerIcon = new Label();
        mapMarkerIcon.setIcon(VaadinIcons.HOME_O);

        horizontalLayout.addComponents(mapMarkerIcon,label);
        routesLayout.add(horizontalLayout);

        int i = 1;
        for(int j = 1;j < routes.size() - 1; j++){
            horizontalLayout = new HorizontalLayout();
            label = new Label("<b>Address " + i + ": </b>"+ routes.get(j).getCheckPoint(), ContentMode.HTML);
            mapMarkerIcon = new Label();
            mapMarkerIcon.setIcon(VaadinIcons.MAP_MARKER);

            horizontalLayout.addComponents(mapMarkerIcon,label);
            routesLayout.add(horizontalLayout);
            i++;
        }

        horizontalLayout = new HorizontalLayout();
        label = new Label("<b>Destination " + ": </b>"+ routes.get(routes.size() - 1).getCheckPoint(), ContentMode.HTML);
        mapMarkerIcon = new Label();
        mapMarkerIcon.setIcon(VaadinIcons.FLAG_CHECKERED);

        horizontalLayout.addComponents(mapMarkerIcon,label);
        routesLayout.add(horizontalLayout);


        return routesLayout;
    }
}

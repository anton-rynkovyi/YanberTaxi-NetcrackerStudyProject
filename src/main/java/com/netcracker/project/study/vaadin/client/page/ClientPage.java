package com.netcracker.project.study.vaadin.client.page;

import com.netcracker.project.study.vaadin.admin.views.ClientsView;
import com.netcracker.project.study.vaadin.admin.views.DriversView;
import com.netcracker.project.study.vaadin.client.views.ClientView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.*;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

@Theme("valo")
@SpringUI(path = "/client")
public class ClientPage extends UI {

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    public static Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout rootLayout = getVerticalLayout();
        rootLayout.setMargin(true);
        rootLayout.setSpacing(false);
      /*  Button b = new Button("Logout");
        rootLayout.addComponent(b);
        b.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getPage().setLocation("/auth");
        });*/
        setContent(rootLayout);

        viewDisplay = getViewDisplay();
        rootLayout.addComponent(viewDisplay);
        //rootLayout.setExpandRatio(viewDisplay, 1.0f);

        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(ClientView.VIEW_NAME);
    }

    private VerticalLayout getVerticalLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        //verticalLayout.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        return verticalLayout;
    }

    private Panel getViewDisplay() {
        Panel springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        return  springViewDisplay;
    }
}

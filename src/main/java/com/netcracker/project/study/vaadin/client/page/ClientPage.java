package com.netcracker.project.study.vaadin.client.page;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.views.ClientsView;
import com.netcracker.project.study.vaadin.admin.views.DriversView;
import com.netcracker.project.study.vaadin.client.popups.ClientUpdate;
import com.netcracker.project.study.vaadin.client.views.ClientView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

@Theme("valo")
@SpringUI(path = "/client")
@Title("YanberTaxi-Client")
public class ClientPage extends UI {

    @Autowired
    private SpringViewProvider provider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ClientUpdate ClientWindow;

    private Panel viewDisplay;


    public static Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout rootLayout = getVerticalLayout();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setHeight(100, Unit.PERCENTAGE);
        setContent(rootLayout);
        //rootLayout.setExpandRatio(viewDisplay, 1.0f);
        HorizontalLayout panelCaption = new HorizontalLayout();
        panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        panelCaption.setStyleName(MaterialTheme.LAYOUT_CARD);
        panelCaption.setMargin(new MarginInfo(false, true, false, true));
       // panelCaption.addStyleName("v-panel-caption");
        panelCaption.setWidth("100%");
       // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label label1 = new Label("<h3>YanberTaxi<h3>", ContentMode.HTML);
        panelCaption.addComponent(label1);
        panelCaption.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);
        panelCaption.setExpandRatio(label1, 1);
        Client client = userDetailsService.getCurrentUser();
        String clientName = client.getFirstName() + " " + client.getLastName();
        Label label2 = new Label( "Hello, "+clientName);
        panelCaption.addComponent(label2);

        Button action = new Button();
        action.setIcon(FontAwesome.PENCIL);
        action.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action.addStyleName(ValoTheme.BUTTON_SMALL);
        action.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        action.addClickListener(clock -> {
            ClientWindow.init(userDetailsService.getUser(),client);
            getUI().addWindow(ClientWindow);
        });
        panelCaption.addComponent(action);
        Button action1 = new Button("LogOut");
        action1.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action1.addStyleName(ValoTheme.BUTTON_SMALL);
        action1.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });
        panelCaption.addComponent(action1);
        rootLayout.addComponent(panelCaption);



        viewDisplay = getViewDisplay();
        rootLayout.addComponent(viewDisplay);
        rootLayout.setExpandRatio(viewDisplay, 0.8f);

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

    private Panel getStandartPanel() {
        Panel panel = new Panel("Astronomer Panel");
        panel.addStyleName("mypanelexample");
        panel.setSizeUndefined(); // Shrink to fit content
        return panel;
    }

}

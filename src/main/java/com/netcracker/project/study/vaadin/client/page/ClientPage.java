package com.netcracker.project.study.vaadin.client.page;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.netcracker.project.study.vaadin.client.events.RefreshClientViewEvent;
import com.netcracker.project.study.vaadin.client.popups.ClientUpdate;
import com.netcracker.project.study.vaadin.client.popups.DriverEvaluation;
import com.netcracker.project.study.vaadin.client.views.ClientView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.addons.Toast;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
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

    @Autowired
    Copyright bottomTeamLogo;

    @Autowired
    private EventBus.UIEventBus uiEventBus;

    @Autowired
    private DriverEvaluation driverEvaluation;


    private Client client;

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
        client = userDetailsService.getCurrentUser();
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
        Button logOutButton = new Button("LogOut");
        logOutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logOutButton.addStyleName(ValoTheme.BUTTON_SMALL);
        logOutButton.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });
        logOutButton.setIcon(VaadinIcons.EXIT);
        panelCaption.addComponent(logOutButton);

        rootLayout.addComponent(panelCaption);

        HorizontalLayout clientPoints = getClientPoints();
        rootLayout.addComponent(clientPoints);
        rootLayout.setComponentAlignment(clientPoints, Alignment.BOTTOM_CENTER);

        viewDisplay = getViewDisplay();
        rootLayout.addComponent(viewDisplay);
        rootLayout.addComponent(bottomTeamLogo);
        rootLayout.setExpandRatio(viewDisplay, 0.8f);

        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(ClientView.VIEW_NAME);


    }

    @PostConstruct
    public void afterPropertiesSet() {
        uiEventBus.subscribe(this,true);

    }

    @EventBusListenerMethod
    public void closeSymmUsageWindow(RefreshClientViewEvent event) {
        driverEvaluation.init();
        getUI().addWindow(driverEvaluation);
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

    private HorizontalLayout getClientPoints(){
        HorizontalLayout clientPointslayout = new HorizontalLayout();
        BigInteger clientPoints = client.getPoints() != null ? client.getPoints() : BigInteger.ZERO;
        Label icon = new Label();
        icon.setIcon(VaadinIcons.COIN_PILES);
        Label pointsInfo = new Label("<b>Your points: " + clientPoints + "</b>", ContentMode.HTML);
        clientPointslayout.addComponents(icon, pointsInfo);
        return clientPointslayout;
    }

}

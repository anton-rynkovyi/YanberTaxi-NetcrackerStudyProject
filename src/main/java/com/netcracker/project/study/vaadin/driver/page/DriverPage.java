package com.netcracker.project.study.vaadin.driver.page;


import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.client.popups.ClientUpdate;
import com.netcracker.project.study.vaadin.driver.components.popup.DriverUpdate;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
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


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Theme("valo")
@SpringUI(path = "/driver")
@Title("YanberTaxi-Driver")
public class DriverPage extends UI{

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    static private Navigator navigator;

    private VerticalLayout rootLayout;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DriverUpdate DriverWindow;

    Driver driver;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getCurrentDriver();
        rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setHeight(100, Unit.PERCENTAGE);

        setContent(rootLayout);

        HorizontalLayout panelCaption = new HorizontalLayout();
        panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        panelCaption.setStyleName(MaterialTheme.LAYOUT_CARD);
        panelCaption.setMargin(new MarginInfo(false, true, false, true));
        // panelCaption.addStyleName("v-panel-caption");
        panelCaption.setWidth("100%");
        // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label label1 = new Label("<h3>YanberTaxi</h3>", ContentMode.HTML);
        panelCaption.addComponent(label1);
        panelCaption.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);
        panelCaption.setExpandRatio(label1, 0.1f);

        Driver driver = userDetailsService.getCurrentUser();
        String driverName = driver.getFirstName() + " " + driver.getLastName();
        Label label2 = new Label( "Hello, " + driverName);
        panelCaption.addComponent(label2);

        Button action = new Button();
        action.setIcon(FontAwesome.PENCIL);
        action.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action.addStyleName(ValoTheme.BUTTON_SMALL);
        action.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        action.addClickListener(clock -> {
            DriverWindow.init();
            getUI().addWindow(DriverWindow);
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


        HorizontalLayout infoPanel = getDriverStatus();
        infoPanel.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        infoPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
        infoPanel.setMargin(new MarginInfo(false, true, false, true));
        infoPanel.setWidth("100%");

        HorizontalLayout statusLayout = new HorizontalLayout();
        infoPanel.addComponent(statusLayout);
        infoPanel.setComponentAlignment(statusLayout,Alignment.TOP_LEFT);
        infoPanel.setExpandRatio(statusLayout,1f);


        HorizontalLayout stars = getDriverRating();
        infoPanel.addComponent(stars);

        rootLayout.addComponent(infoPanel);


        viewDisplay = new Panel();
        viewDisplay.setSizeFull();

        rootLayout.addComponent(viewDisplay);
        rootLayout.setExpandRatio(viewDisplay, 1.0f);

        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(OrdersViewForDrivers.VIEW_NAME);

        //OrdersViewForDrivers view = (OrdersViewForDrivers)navigator.getCurrentView();

        /*setPollInterval(1000);
        addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
                view.Refresh();
            }
        });*/

    }

    private void getCurrentDriver(){
        driver = userDetailsService.getCurrentUser();
    }

    private HorizontalLayout getDriverRating(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        int total = 5;
        int rating = driver.getRating().intValue();

        Label ratingHeader = new Label("Rating: ",ContentMode.HTML);
        horizontalLayout.addComponent(ratingHeader);

        for(int i = 0;i < rating; i++){
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR);
            horizontalLayout.addComponent(star);
        }

        int remainedRating = total - rating;

        for(int i = 0;i < remainedRating; i++){
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR_O);
            horizontalLayout.addComponent(star);
        }
        return horizontalLayout;
    }

    private HorizontalLayout getDriverStatus(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Label statusHeader = new Label("Status: ",ContentMode.HTML);
        horizontalLayout.addComponent(statusHeader);

        Label statusIcon = getStatusIcon();
        horizontalLayout.addComponent(statusIcon);

        Label status = new Label(DriverStatusEnum.getStatusValue(driver.getStatus()));
        horizontalLayout.addComponent(status);
        return horizontalLayout;
    }

    private Label getStatusIcon(){
        Label icon = new Label();
        if(driver.getStatus().equals(DriverStatusList.FREE)){
            icon.setIcon(VaadinIcons.COFFEE);
        }
        if(driver.getStatus().equals(DriverStatusList.ON_CALL)){
            icon.setIcon(VaadinIcons.TAXI);
        }
        if(driver.getStatus().equals(DriverStatusList.PERFORMING_ORDER)){
            icon.setIcon(VaadinIcons.ROAD);
        }
        if(driver.getStatus().equals(DriverStatusList.OFF_DUTY)){
            icon.setIcon(VaadinIcons.HOME_O);
        }
        return icon;
    }
}

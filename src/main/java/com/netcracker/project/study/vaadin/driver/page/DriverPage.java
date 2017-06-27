package com.netcracker.project.study.vaadin.driver.page;


import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.services.DriverService;
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
import org.vaadin.spring.events.EventBus;


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

    @Autowired
    DriverService driverService;



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
        panelCaption.setSpacing(true);
        panelCaption.setWidth("100%");

        HorizontalLayout yanberLabel = getYanberLabel();
        panelCaption.addComponent(yanberLabel);
        panelCaption.setComponentAlignment(yanberLabel, Alignment.TOP_LEFT);
        panelCaption.setExpandRatio(yanberLabel, 0.1f);


        HorizontalLayout driverNameLayout = getDriverNameLayout();
        panelCaption.addComponent(driverNameLayout);

        HorizontalLayout ratingLayout = getDriverRatingLayout();
        panelCaption.addComponent(ratingLayout);

        HorizontalLayout driverStausLayout = getDriverStatus();
        panelCaption.addComponent(driverStausLayout);
        panelCaption.setComponentAlignment(driverStausLayout,Alignment.TOP_LEFT);


       HorizontalLayout editButtonLayout = getEditButtonLayout();
        panelCaption.addComponent(editButtonLayout);
        panelCaption.setComponentAlignment(editButtonLayout,Alignment.TOP_RIGHT);

        HorizontalLayout logOutButtonLayout = getLogOutButton();
        panelCaption.addComponent(logOutButtonLayout);
        panelCaption.setComponentAlignment(logOutButtonLayout,Alignment.TOP_RIGHT);
        rootLayout.addComponent(panelCaption);


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


    private HorizontalLayout getYanberLabel(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label yanberLabel = new Label("YanberTaxi", ContentMode.HTML);

        horizontalLayout.addComponents(yanberLabel);

        return horizontalLayout;

    }
    private HorizontalLayout getEditButtonLayout(){
        Button editButton = new Button();
        editButton.setIcon(VaadinIcons.PENCIL);
        editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(clock -> {
            DriverWindow.init();
            getUI().addWindow(DriverWindow);
        });

        Label separatorLabel = new Label();
        separatorLabel.setIcon(VaadinIcons.LINE_V);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(editButton);

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponent(separator);
        return  horizontalLayout;
    }


    private HorizontalLayout getLogOutButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button logOutButton = new Button("LogOut");
        logOutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logOutButton.addStyleName(ValoTheme.BUTTON_SMALL);
        logOutButton.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });

        logOutButton.setIcon(VaadinIcons.EXIT);

        horizontalLayout.addComponents(logOutButton);

        return horizontalLayout;
    }
    private HorizontalLayout getDriverNameLayout(){
        Driver driver = userDetailsService.getCurrentUser();
        String driverName = driver.getFirstName() + " " + driver.getLastName();

        Label helloLabel = new Label( "Hello, " + driverName,ContentMode.HTML);
        Label iconUser = new Label();
        iconUser.setIcon(VaadinIcons.USER);
        Label separatorLabel = new Label();
        separatorLabel.setIcon(VaadinIcons.LINE_V);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        HorizontalLayout separator = getSeparator();

        horizontalLayout.addComponents(iconUser,helloLabel,separator);
        return horizontalLayout;
    }
    private void getCurrentDriver(){
        driver = userDetailsService.getCurrentUser();
    }

    private HorizontalLayout getDriverRatingLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        int total = 5;
        int rating = driver.getRating().intValue();

        Label ratingHeader = new Label("Rating: ",ContentMode.HTML);
        Label emptyLabel = new Label();
        Label separatorlabel = new Label();
        separatorlabel.setIcon(VaadinIcons.LINE_V);
        horizontalLayout.addComponents(ratingHeader);

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

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponents(separator);
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

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponent(separator);
        return horizontalLayout;
    }

    private HorizontalLayout getSeparator(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Label emptyLabel = new Label();
        Label separator = new Label();
        separator.setIcon(VaadinIcons.LINE_V);

        horizontalLayout.addComponents(emptyLabel,separator,emptyLabel);

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

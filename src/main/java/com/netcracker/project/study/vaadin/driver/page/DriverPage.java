package com.netcracker.project.study.vaadin.driver.page;


import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.netcracker.project.study.vaadin.client.popups.ClientUpdate;
import com.netcracker.project.study.vaadin.driver.components.popup.DriverUpdate;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
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
import org.vaadin.addons.Toast;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;
import org.vaadin.spring.events.EventBus;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Theme("valo")
@SpringUI(path = "/driver")
@Title("YanberTaxi-Driver")
public class DriverPage extends UI {

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    static private Navigator navigator;

    private VerticalLayout rootLayout;

    @Autowired
    Copyright bottomTeamLogo;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DriverUpdate DriverWindow;

    Driver driver;

    @Autowired
    DriverService driverService;

    Label statusIcon;
    Label statusValue;

    Button changeStatusButton;

    private Toastr toastr;

    @Autowired
    UserFacade userFacade;

    @Autowired
    AdminService adminService;

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
        panelCaption.setStyleName("panel");
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

        HorizontalLayout driverStatusLayout = new HorizontalLayout();
        HorizontalLayout changeStatusLayout = getChangeStatusButton();

        HorizontalLayout driverStatus = getDriverStatus();
        driverStatusLayout.addComponents(changeStatusLayout,driverStatus);
        panelCaption.addComponent(driverStatusLayout);
        panelCaption.setComponentAlignment(driverStatusLayout, Alignment.TOP_LEFT);

        HorizontalLayout editButtonLayout = getEditButtonLayout();
        panelCaption.addComponent(editButtonLayout);
        panelCaption.setComponentAlignment(editButtonLayout, Alignment.TOP_RIGHT);

        HorizontalLayout logOutButtonLayout = getLogOutButton();
        panelCaption.addComponent(logOutButtonLayout);
        panelCaption.setComponentAlignment(logOutButtonLayout, Alignment.TOP_RIGHT);
        rootLayout.addComponent(panelCaption);


        viewDisplay = new Panel();
        viewDisplay.setSizeFull();

        rootLayout.addComponent(viewDisplay);
        rootLayout.addComponent(bottomTeamLogo);
        rootLayout.setExpandRatio(viewDisplay, 0.8f);

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

    public void setStatusButtonEnabled(boolean value){
        changeStatusButton.setEnabled(value);
    }

    private HorizontalLayout getChangeStatusButton() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        changeStatusButton = new Button();
        changeStatusButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        changeStatusButton.addStyleName(ValoTheme.BUTTON_SMALL);


        changeStatusButton.setIcon(VaadinIcons.POWER_OFF);
        changeStatusButton.setDescription("Press to start/stop working");
        horizontalLayout.addComponent(changeStatusButton);

        changeStatusButton.addClickListener(clickEvent -> {
            if (isBanned()) {
                SecurityContextHolder.clearContext();
                return;
            }
            if (isDismissed()) {
                logout();
                return;
            }

            if(driver.getStatus().equals(DriverStatusList.FREE)){
                driverService.changeStatus(DriverStatusList.OFF_DUTY,driver.getObjectId());
                ((OrdersViewForDrivers)navigator.getCurrentView()).setAcceptButtonEnabled(false);
            }
            if(driver.getStatus().equals(DriverStatusList.OFF_DUTY)){
                driverService.changeStatus(DriverStatusList.FREE,driver.getObjectId());
                ((OrdersViewForDrivers)navigator.getCurrentView()).setAcceptButtonEnabled(true);
            }
            ((OrdersViewForDrivers)navigator.getCurrentView()).Refresh();
        });

        return horizontalLayout;
    }

    private void logout() {
        SecurityContextHolder.clearContext();
        getUI().getSession().close();
        getUI().getPage().setLocation("/authorization");
    }

    private boolean isDismissed() {
        if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
            return true;
        }
        return false;
    }

    private boolean isBanned() {
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        if (!user.isEnabled()) {
            UI.getCurrent().setContent(toastr);
            Driver driver = adminService.getModelById(this.driver.getObjectId(), Driver.class);
            Toast banToast = ToastBuilder.of(ToastType.Warning,
                    "You have been banned." +
                            "\nUnban date: " + new Timestamp(driver.getUnbanDate().getTime()) +
                            "\n Contacts: yanbertaxi.netcracker@gmail.com")
                    .caption("Information")
                    .options(ToastOptionsBuilder.having()
                            .closeButton(false)
                            .debug(false)
                            .progressBar(false)
                            .preventDuplicates(true)
                            .position(ToastPosition.Top_Full_Width)
                            .tapToDismiss(false)
                            .extendedTimeOut(600000)
                            .build())
                    .build();
            toastr.toast(banToast);
            return true;
        }
        return false;
    }

    public void refreshUI() {
        getCurrentDriver();
        changeStatusIcon();
    }

    private void changeStatusIcon() {
        if (driver.getStatus().equals(DriverStatusList.FREE)) {
            statusIcon.setIcon(VaadinIcons.COFFEE);
        }
        if (driver.getStatus().equals(DriverStatusList.ON_CALL)) {
            statusIcon.setIcon(VaadinIcons.TAXI);
        }
        if (driver.getStatus().equals(DriverStatusList.PERFORMING_ORDER)) {
            statusIcon.setIcon(VaadinIcons.ROAD);
        }
        if (driver.getStatus().equals(DriverStatusList.OFF_DUTY)) {
            statusIcon.setIcon(VaadinIcons.HOME_O);
        }
        statusValue.setValue(DriverStatusEnum.getStatusValue(driver.getStatus()));
    }

    private HorizontalLayout getYanberLabel() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label yanberLabel = new Label("YanberTaxi", ContentMode.HTML);

        horizontalLayout.addComponents(yanberLabel);

        return horizontalLayout;

    }

    private HorizontalLayout getEditButtonLayout() {
        Button editButton = new Button();
        editButton.setIcon(VaadinIcons.PENCIL);
        editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(clock -> {
            DriverWindow.init();
            getUI().addWindow(DriverWindow);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(editButton);

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponent(separator);
        return horizontalLayout;
    }


    private HorizontalLayout getLogOutButton() {
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

    private HorizontalLayout getDriverNameLayout() {
        Driver driver = userDetailsService.getCurrentUser();
        String driverName = driver.getFirstName() + " " + driver.getLastName();

        Label helloLabel = new Label("Hello, " + driverName, ContentMode.HTML);
        Label iconUser = new Label();
        iconUser.setIcon(VaadinIcons.USER);
        Label separatorLabel = new Label();
        separatorLabel.setIcon(VaadinIcons.LINE_V);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        HorizontalLayout separator = getSeparator();

        horizontalLayout.addComponents(iconUser, helloLabel, separator);
        return horizontalLayout;
    }

    private void getCurrentDriver() {
        driver = userDetailsService.getCurrentUser();
    }

    private HorizontalLayout getDriverRatingLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        int total = 5;
        int rating = driver.getRating().intValue();

        Label ratingHeader = new Label("Rating: ", ContentMode.HTML);
        Label emptyLabel = new Label();
        Label separatorlabel = new Label();
        separatorlabel.setIcon(VaadinIcons.LINE_V);
        horizontalLayout.addComponents(ratingHeader);

        for (int i = 0; i < rating; i++) {
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR);
            horizontalLayout.addComponent(star);
        }

        int remainedRating = total - rating;

        for (int i = 0; i < remainedRating; i++) {
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR_O);
            horizontalLayout.addComponent(star);
        }

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponents(separator);
        return horizontalLayout;
    }

    private HorizontalLayout getDriverStatus() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Label statusHeader = new Label("Status: ", ContentMode.HTML);
        horizontalLayout.addComponent(statusHeader);

        statusIcon = getStatusIcon();
        horizontalLayout.addComponent(statusIcon);

        statusValue = new Label(DriverStatusEnum.getStatusValue(driver.getStatus()));
        horizontalLayout.addComponent(statusValue);

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponent(separator);


        return horizontalLayout;
    }

    private HorizontalLayout getSeparator() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Label emptyLabel = new Label();
        Label separator = new Label();
        separator.setIcon(VaadinIcons.LINE_V);

        horizontalLayout.addComponents(emptyLabel, separator, emptyLabel);

        return horizontalLayout;
    }

    private Label getStatusIcon() {
        Label icon = new Label();
        if (driver.getStatus().equals(DriverStatusList.FREE)) {
            icon.setIcon(VaadinIcons.COFFEE);
        }
        if (driver.getStatus().equals(DriverStatusList.ON_CALL)) {
            icon.setIcon(VaadinIcons.TAXI);
        }
        if (driver.getStatus().equals(DriverStatusList.PERFORMING_ORDER)) {
            icon.setIcon(VaadinIcons.ROAD);
        }
        if (driver.getStatus().equals(DriverStatusList.OFF_DUTY)) {
            icon.setIcon(VaadinIcons.HOME_O);
        }
        return icon;
    }
}

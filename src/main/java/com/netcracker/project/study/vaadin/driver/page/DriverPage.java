package com.netcracker.project.study.vaadin.driver.page;


import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.StartPage;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.netcracker.project.study.vaadin.driver.components.popup.CarUpdate;
import com.netcracker.project.study.vaadin.driver.components.popup.DriverUpdate;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;


import java.net.URI;
import java.sql.Timestamp;
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
    StartPage startPage;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DriverUpdate driverWindow;

    private Driver driver;
    private Car car;

    @Autowired
    private DriverService driverService;

    private Label statusIcon;
    private Label statusValue;

    private Button changeStatusButton;

    private Toastr banToastr;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private AdminService adminService;


    @Autowired
    CarUpdate carWindow;

    //10 seconds
    private final int timerInterval = 10000;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getCurrentDriver();
        rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setHeight(100, Unit.PERCENTAGE);
        banToastr = new Toastr();
        banToastr.registerToastrListener(new ToastrListenerAdapter(){
            @Override
            public void onClick() {
                UI.getCurrent().getPage().setLocation("/authorization");
            }

            @Override
            public void onHidden() {
                UI.getCurrent().getPage().setLocation("/authorization");
            }
        });
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
        panelCaption.setExpandRatio(yanberLabel, 0.01f);

        HorizontalLayout driverStatusLayout = new HorizontalLayout();
        HorizontalLayout changeStatusLayout = getChangeStatusButton();

        HorizontalLayout driverStatus = getDriverStatus();
        driverStatusLayout.addComponents(changeStatusLayout,driverStatus);
        panelCaption.addComponent(driverStatusLayout);
        panelCaption.setExpandRatio(driverStatusLayout,0.05f);
        panelCaption.setComponentAlignment(driverStatusLayout, Alignment.TOP_LEFT);


        HorizontalLayout driverNameLayout = getDriverNameLayout();
        panelCaption.addComponent(driverNameLayout);

        HorizontalLayout ratingLayout = getDriverRatingLayout();
        panelCaption.addComponent(ratingLayout);

        HorizontalLayout editButtonLayout = getEditButtonLayout();
        panelCaption.addComponent(editButtonLayout);
        panelCaption.setComponentAlignment(editButtonLayout, Alignment.TOP_RIGHT);

        HorizontalLayout editButtonCarLayout = getEditButtonCarLayout();
        panelCaption.addComponent(editButtonCarLayout);
        panelCaption.setComponentAlignment(editButtonCarLayout, Alignment.TOP_RIGHT);

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

        setPollInterval(timerInterval);
        addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
                try {
                    getCurrentView().refreshNewOrdersTab();
                    getCurrentUI().refreshUI();
                }catch (Exception e) {}
            }
        });
    }

    public void refreshUI() {
        getCurrentDriver();
        changeStatusIcon();
    }

    private DriverPage getCurrentUI(){
        return (DriverPage)UI.getCurrent();
    }

    private OrdersViewForDrivers getCurrentView(){
        return ((OrdersViewForDrivers)(UI.getCurrent()).getNavigator().getCurrentView());
    }

    private void getCurrentDriver() {
        driver = userDetailsService.getCurrentUser();
        List<Car> cars = driverService.getCarByDriver(driver);
        car = cars.get(0);
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
                SecurityContextHolder.clearContext();
                return;
            }

            if(driver.getStatus().equals(DriverStatusList.FREE)){
                driverService.changeStatus(DriverStatusList.OFF_DUTY,driver.getObjectId());
            }
            if(driver.getStatus().equals(DriverStatusList.OFF_DUTY)){
                driverService.changeStatus(DriverStatusList.FREE,driver.getObjectId());
            }
            getCurrentView().setAcceptButtonEnabled();
            getCurrentUI().refreshUI();
        });

        return horizontalLayout;
    }

    private void logout() {
        SecurityContextHolder.clearContext();
        getUI().getSession().close();
        getUI().getPage().setLocation("/authorization");
    }

    private boolean isDismissed() {
        Driver driver = adminService.getModelById(this.driver.getObjectId(), Driver.class);
        if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
            UI.getCurrent().setContent(banToastr);
            Toast banToast = ToastBuilder.of(ToastType.Warning,
                    "You have been dismissed." +
                            "\n Contacts: yanbertaxi.netcracker@gmail.com")
                    .caption("Information")
                    .options(ToastOptionsBuilder.having()
                            .closeButton(false)
                            .debug(false)
                            .progressBar(false)
                            .preventDuplicates(true)
                            .position(ToastPosition.Top_Full_Width)
                            .tapToDismiss(false)
                            .extendedTimeOut(10000)
                            .build())
                    .build();
            banToastr.toast(banToast);
            return true;
        }
        return false;
    }

    private boolean isBanned() {
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        if (!user.isEnabled()) {
            UI.getCurrent().setContent(banToastr);
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
                            .extendedTimeOut(10000)
                            .build())
                    .build();
            banToastr.toast(banToast);
            return true;
        }
        return false;
    }

    public void changeStatusIcon() {
        if (driver.getStatus().equals(DriverStatusList.FREE)) {
            statusIcon.setIcon(VaadinIcons.COFFEE);
            setStatusButtonEnabled(true);
        }
        if (driver.getStatus().equals(DriverStatusList.ON_CALL)) {
            statusIcon.setIcon(VaadinIcons.TAXI);
            setStatusButtonEnabled(false);
        }
        if (driver.getStatus().equals(DriverStatusList.PERFORMING_ORDER)) {
            statusIcon.setIcon(VaadinIcons.ROAD);
            setStatusButtonEnabled(false);
        }
        if (driver.getStatus().equals(DriverStatusList.OFF_DUTY)) {
            statusIcon.setIcon(VaadinIcons.HOME_O);
            setStatusButtonEnabled(true);
        }
        statusValue.setValue(DriverStatusEnum.getStatusValue(driver.getStatus()));
    }

    private HorizontalLayout getYanberLabel() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label yanberLabel = new Label("YanberTaxi", ContentMode.HTML);

        horizontalLayout.addComponents(yanberLabel);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        return horizontalLayout;

    }


    private HorizontalLayout getEditButtonLayout() {
        Button editButton = new Button();
        editButton.setIcon(VaadinIcons.PENCIL);
        editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(clock -> {
            driverWindow.init();
            getUI().addWindow(driverWindow);
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(editButton);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        return horizontalLayout;
    }

    private HorizontalLayout getEditButtonCarLayout() {
        Button editButtonCar = new Button();
        editButtonCar.setDescription(car.getMakeOfCar() + " " + car.getModelType());
        editButtonCar.setIcon(VaadinIcons.CAR);
        editButtonCar.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        editButtonCar.addStyleName(ValoTheme.BUTTON_SMALL);
        editButtonCar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButtonCar.addClickListener(clock -> {
            carWindow.init();
            getUI().addWindow(carWindow);
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(editButtonCar);

        HorizontalLayout separator = getSeparator();
        horizontalLayout.addComponent(separator);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        return horizontalLayout;
    }

    private HorizontalLayout getLogOutButton() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button logOutButton = new Button("Logout");
        logOutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logOutButton.addStyleName(ValoTheme.BUTTON_SMALL);
        logOutButton.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });

        logOutButton.setIcon(VaadinIcons.EXIT);

        horizontalLayout.addComponents(logOutButton);
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

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
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        return horizontalLayout;
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
        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
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

        horizontalLayout.setHeight("100%");
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

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
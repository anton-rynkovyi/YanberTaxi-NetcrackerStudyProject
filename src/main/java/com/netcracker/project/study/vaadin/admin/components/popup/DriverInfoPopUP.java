package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.math.BigInteger;
import java.util.List;

@SpringComponent
@Scope(value = "prototype")
public class DriverInfoPopUP extends VerticalLayout {

    private Driver driver;

    private DriversGrid driversGrid;

    private DriversBanGrid driversBanGrid;

    @Autowired
    AdminService adminService;

    @Autowired
    DriverService driverService;

    @Autowired
    OrderService orderService;

    @Autowired
    BanDaysPopUp banDaysPopUp;

    private Window banDaysWindow;

    private Toastr toastr;

    //private List<Car> driverCarList;

    public void init(Driver driver) {
        this.driver = driver;
        //this.driverCarList = driverService.getCarByDriver(driver);
        initBanDaysWindow();
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        setTextFields(rootLayout);
        addComponent(rootLayout);
        toastr = new Toastr();
        rootLayout.addComponent(toastr);
    }

    private void initBanDaysWindow() {
        banDaysWindow = new Window("Ban");
        banDaysWindow.center();
        banDaysWindow.setModal(true);
        banDaysWindow.setContent(banDaysPopUp);
    }

    private void setTextFields(VerticalLayout rootLayout) {
        //rootLayout.addComponent(lastSeen());

        rootLayout.addComponent(setDriverAndCarInfoLayout());
        rootLayout.addComponent(setControlButtonsLayout());
        rootLayout.addComponent(setDriverCommentsLayout());
    }

    private Component lastSeen() {
        //String ls = ad
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label lastSeen = new Label("<i>Last seen in <b>free</b> status:</i>", ContentMode.HTML);
        horizontalLayout.addComponent(lastSeen);
        return horizontalLayout;
    }

    private HorizontalLayout setDriverAndCarInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        MarginInfo marginInfo = new MarginInfo(true, true, false, true);
        horizontalLayout.setMargin(marginInfo);
        VerticalLayout driverForm = new VerticalLayout();
        Label name = new Label("<h2><b>" + driver.getFirstName() + " " + driver.getLastName() + "</b></h2><hr>",
                ContentMode.HTML);
        Label midName = new Label("Middle name: <i>" + driver.getMiddleName() + "</i>", ContentMode.HTML);
        Label phone = new Label("Phone: <i>" + driver.getPhoneNumber() + "</i>", ContentMode.HTML);
        Label email = new Label("Email: <i>" + driver.getEmail() + "</i>", ContentMode.HTML);
        Label exp = new Label("Experience: <i>" + driver.getExperience() + " years </i>", ContentMode.HTML);
        Label status = new Label("Status: <i>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</i>", ContentMode.HTML);
        driverForm.addComponents(name, midName, phone, email, exp, status);

        Panel driverPanel = new Panel("Personal information", driverForm);
        driverPanel.setWidth(400, Unit.PIXELS);


        VerticalLayout carForm = new VerticalLayout();
        List<Car> carList = driverService.getCarByDriver(driver);
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            Label carName = new Label("<h2><b>" + car.getMakeOfCar() + "</b></h2><hr>", ContentMode.HTML);
            Label model = new Label("Model: <i>" + car.getModelType() + "</i>", ContentMode.HTML);
            Label releaseDate = new Label("Release date: <i>" + car.getReleaseDate() + "</i>", ContentMode.HTML);
            Label seatsCount = new Label("Seats count: <i>" + car.getSeatsCount() + "</i>", ContentMode.HTML);
            Label stateNumber = new Label("State number: <i>" + car.getStateNumber() + "</i>", ContentMode.HTML);
            Label childSeat = new Label("Child seat: <i>" + car.isChildSeat() + "</i>", ContentMode.HTML);
            carForm.addComponents(carName, model, stateNumber, releaseDate, seatsCount, childSeat);
        }
        Panel carPanel = new Panel("Vehicle", carForm);
        carPanel.setWidth(400, Unit.PIXELS);

        horizontalLayout.addComponent(driverPanel);
        horizontalLayout.addComponent(carPanel);

        return horizontalLayout;
    }

    private HorizontalLayout setControlButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        MarginInfo marginInfo = new MarginInfo(true, true, false, true);
        horizontalLayout.setMargin(marginInfo);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);
        horizontalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

        Button btnFire = new Button("Dismiss");
        btnFire.setWidth(100, Unit.PIXELS);
        btnFire.addClickListener(clickEvent -> {
            BigInteger driverId = driver.getObjectId();
            Driver driver = adminService.getModelById(driverId, Driver.class);
            if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
                toastr.toast(ToastBuilder.error("This driver is already dismissed.").build());
                return;
            }
            if (driver.getStatus().compareTo(DriverStatusList.ON_CALL) == 0 &&
                    driver.getStatus().compareTo(DriverStatusList.PERFORMING_ORDER) == 0) {
                toastr.toast(ToastBuilder.error("This driver has order. Try again later.").build());
                return;
            }
            String firstName = driver.getFirstName();
            String lastName = driver.getLastName();
            MessageBox
                    .createQuestion()
                    .withCaption("Dismissal")
                    .withMessage("Are you want to dismiss " + firstName + " " + lastName + "?")
                    .withYesButton(() -> {
                        adminService.fireDriver(driver);
                        driversGrid.refreshGrid();
                    })
                    .withNoButton(() -> {})
                    .open();
            driversGrid.getDriverInfoWindow().close();
        });
        horizontalLayout.addComponent(btnFire);

        Button btnBan = new Button("Ban");
        btnBan.setWidth(100, Unit.PIXELS);
        btnBan.addClickListener(clickEvent -> {
            BigInteger driverId = driver.getObjectId();
            Driver driver = adminService.getModelById(driverId, Driver.class);
            if (driver.getStatus().compareTo(DriverStatusList.DISMISSED) == 0) {
                toastr.toast(ToastBuilder.error("You cannot ban a dismissed driver.").build());
                return;
            }
            if (driver.getUnbanDate() != null) {
                toastr.toast(ToastBuilder.error("This driver already has ban.").build());
                return;
            }
            if (driver.getStatus().compareTo(DriverStatusList.ON_CALL) == 0 &&
                    driver.getStatus().compareTo(DriverStatusList.PERFORMING_ORDER) == 0) {
                toastr.toast(ToastBuilder.error("This driver has order. Try again later.").build());
                return;
            }
            banDaysPopUp.setDriversGrid(driversGrid);
            banDaysPopUp.setDriverInfoPopUP(this);
            UI.getCurrent().addWindow(banDaysWindow);

        });

        horizontalLayout.addComponent(btnBan);
        horizontalLayout.setExpandRatio(btnFire, 0.9f);
        horizontalLayout.setExpandRatio(btnBan, 0.1f);

        return horizontalLayout;
    }


    private VerticalLayout setDriverCommentsLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        List<Order> orderList = orderService.getOrdersByDriverId(driver.getObjectId(),null);
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getDriverMemo() != null) {
                if (orderList.get(i).getClientId() != null) {
                    Client client = adminService.getModelById(orderList.get(i).getClientId(), Client.class);
                    TextArea textArea = new TextArea();
                    textArea.setCaptionAsHtml(true);
                    textArea.setWidth(100, Unit.PERCENTAGE);
                    textArea.setHeight(80, Unit.PIXELS);
                    textArea.setCaption(client.getFirstName() + " " + client.getLastName());
                    textArea.setReadOnly(true);
                    textArea.setEnabled(false);
                    textArea.setValue(orderList.get(i).getDriverMemo());
                    verticalLayout.addComponent(textArea);
                    verticalLayout.addComponent(new Label());
                }
            }
        }
        return verticalLayout;
    }

    public Window getBanDaysWindow() {
        return banDaysWindow;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setDriversGrid(DriversGrid driversGrid) {
        this.driversGrid = driversGrid;
    }

    public void setDriversBanGrid(DriversBanGrid driversBanGrid) {
        this.driversBanGrid = driversBanGrid;
    }
}

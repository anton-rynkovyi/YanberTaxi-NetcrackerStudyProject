package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.services.tools.EmailMassageSender;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.sql.Date;
import java.util.List;

@SpringComponent
@Scope(value = "prototype")
public class DriverRequestInfoPopUp extends VerticalLayout{

    private Driver driver;

    DriversRequestsGrid driversRequestsGrid;

    @Autowired
    AdminService adminService;

    @Autowired
    DriverService driverService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    RichTextArea richTextArea;

    private List<Car> driverCarList;

    private Toastr toastr;

    @Autowired
    EmailMassageSender emailMassageSender;


    public void init(Driver driver) {
        this.toastr = new Toastr();
        this.driver = driver;
        this.driverCarList = driverService.getCarByDriver(driver);
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setHeightUndefined();
        rootLayout.setWidthUndefined();
        rootLayout.addComponent(toastr);
        setTextFields(rootLayout);
        addComponent(rootLayout);
    }

    private void setTextFields(VerticalLayout rootLayout) {
        rootLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        rootLayout.addComponent(setDriverAndCarInfoLayout());
        rootLayout.addComponent(setRichTextAreaLayout());
        rootLayout.addComponent(setControlButtonsLayout());
        rootLayout.setSizeFull();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);
    }

    private HorizontalLayout setDriverAndCarInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        VerticalLayout driverForm = new VerticalLayout();
        driverForm.setWidth(100, Unit.PERCENTAGE);
        Label name = new Label( "<h2><b>" + driver.getFirstName() + " " + driver.getLastName()+ "</b></h2><hr>",
                ContentMode.HTML);
        Label midName = new Label("Middle name: <i>" + driver.getMiddleName() + "</i>", ContentMode.HTML);
        Label phone = new Label("Phone: <i>" + driver.getPhoneNumber() + "</i>", ContentMode.HTML);
        Label email = new Label("Email: <i>" + driver.getEmail() + "</i>", ContentMode.HTML);
        Label exp = new Label("Experience: <i>" + driver.getExperience() + " years </i>", ContentMode.HTML);
        Label status = new Label("Status: <i>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</i>", ContentMode.HTML);
        driverForm.addComponents(name, midName, phone, email, exp, status);

        Panel driverPanel = new Panel("Personal information", driverForm);
        driverPanel.setWidth(100, Unit.PERCENTAGE);
        driverPanel.setWidth(400, Unit.PIXELS);


        VerticalLayout carForm = new VerticalLayout();
        carForm.setWidth(100, Unit.PERCENTAGE);
        List<Car> carList = driverService.getCarByDriver(driver);
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            System.out.println("CAR:" + car.getDriverId());
            Label carName = new Label("<h2><b>" + car.getMakeOfCar() + "</b></h2><hr>", ContentMode.HTML);
            Label model = new Label("Model: <i>" + car.getModelType() + "</i>", ContentMode.HTML);
            Label releaseDate = new Label("Release date: <i>" + car.getReleaseDate() + "</i>", ContentMode.HTML);
            Label seatsCount = new Label("Seats count: <i>" + car.getSeatsCount() + "</i>", ContentMode.HTML);
            Label stateNumber = new Label("State number: <i>" + car.getStateNumber() + "</i>", ContentMode.HTML);
            Label childSeat = new Label("Child seat: <i>" + car.isChildSeat() + "</i>", ContentMode.HTML);
            carForm.addComponents(carName, model, stateNumber, releaseDate, seatsCount, childSeat);
        }
        Panel carPanel = new Panel("Vehicle", carForm);
        carPanel.setWidth(100, Unit.PERCENTAGE);
        carPanel.setWidth(400, Unit.PIXELS);

        horizontalLayout.addComponent(driverPanel);
        horizontalLayout.setExpandRatio(driverPanel, 0.5f);
        horizontalLayout.addComponent(carPanel);
        horizontalLayout.setExpandRatio(carPanel, 0.5f);

        return horizontalLayout;
    }

    private HorizontalLayout setRichTextAreaLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        richTextArea = new RichTextArea();
        richTextArea.setSizeFull();
        horizontalLayout.addComponent(richTextArea);
        return horizontalLayout;
    }

    private HorizontalLayout setControlButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);
        horizontalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

        Button btnDecline = new Button("Decline");
        btnDecline.addClickListener(clickEvent -> {
            if (richTextArea.getValue().isEmpty()) {
                toastr.toast(ToastBuilder.warning("Write answer for this driver").build());
                return;
            }
            emailMassageSender.sendMessage(driver.getEmail(), richTextArea.getValue());
            List<Car> carList = driverService.getCarByDriver(driver);
            for (int i = 0; i < carList.size(); i++) {
                adminService.deleteModel(carList.get(i));
            }
            User user = userDetailsService.findUserByUsername(driver.getPhoneNumber());
            userDetailsService.deleteUser(user);
            adminService.deleteModel(driver);

            driversRequestsGrid.refreshGrid();
            driversRequestsGrid.getDriversRequestSubWindow().close();
            driver.getEmail();
        });

        Button btnApprove = new Button("Approve");
        btnApprove.addClickListener(clickEvent -> {
            if (richTextArea.getValue().isEmpty()) {
                toastr.toast(ToastBuilder.warning("Write answer for this driver").build());
                return;
            }
            emailMassageSender.sendMessage(driver.getEmail(), richTextArea.getValue());
            driver.setStatus(DriverStatusList.OFF_DUTY);
            driver.setHireDate(new Date(System.currentTimeMillis()));
            adminService.updateModel(driver);
            driversRequestsGrid.refreshGrid();

            driversRequestsGrid.getDriversRequestSubWindow().close();
        });

        Button btnClose = new Button("Close");
        btnClose.addClickListener(clickEvent -> {
            for (Window window : UI.getCurrent().getWindows()){
                window.close();
            }
        });

        horizontalLayout.addComponent(btnClose);
        horizontalLayout.addComponent(btnDecline);
        horizontalLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_LEFT);
        horizontalLayout.addComponent(btnApprove);
        horizontalLayout.setExpandRatio(btnDecline, 0.78f);
        horizontalLayout.setExpandRatio(btnApprove, 0.12f);

        return horizontalLayout;
    }

    public List<Car> getDriverCarList() {
        return driverCarList;
    }

    public void setDriverCarList(List<Car> driverCarList) {
        this.driverCarList = driverCarList;
    }

    public void setDriversRequestsGrid(DriversRequestsGrid driversRequestsGrid) {
        this.driversRequestsGrid = driversRequestsGrid;
    }
}

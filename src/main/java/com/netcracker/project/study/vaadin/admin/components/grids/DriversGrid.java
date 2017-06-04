package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;

import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverInfoPopUP;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverCreatePopUp;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverUpdatePopUp;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
public class DriversGrid extends CustomComponent{

    @Autowired AdminService adminService;

    @Autowired DriverService driverService;

    @Autowired DriverCreatePopUp driverCreatePopUp;

    @Autowired DriverInfoPopUP driverInfoPopUp;

    @Autowired DriverUpdatePopUp driverUpdatePopUp;

    private Grid<Driver> driversGrid;

    private VerticalLayout componentLayout;

    private List<Driver> driversList;

    private Window createDriverWindow;

    private Window driverInfoWindow;

    private Window updateDriverWindow;

    private NativeSelect statusSelect;

    private HorizontalLayout filtersLayout;

    private TextField fieldFilter;

    //private ListDataProvider<Driver> dataProvider;

    @PostConstruct
    public void init() {
        //initStatusChooser();
        initFilters();
        driversGrid = generateDriversGrid();
        //initDataProvider();
        componentLayout = getFilledComponentLayout();
        initCreateDriverWindow();
        initDriverInfoWindow();
        initUpdateInfoWindow();
        setGridSettings(driversGrid);
        setCompositionRoot(componentLayout);
    }

    private void initDataProvider() {
        //dataProvider = (ListDataProvider<Driver>) driversGrid.getDataProvider();
    }

    private void initFilters(){
        filtersLayout = new HorizontalLayout();
        //filtersLayout.setWidth(100,  Unit.PERCENTAGE);
        filtersLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        initStatusChooser();
        filtersLayout.addComponent(statusSelect);


        initTextFieldFilter();
        filtersLayout.addComponent(fieldFilter);

    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();

        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);
        HorizontalLayout controlButtons = getControlButtonsLayout();

        componentLayout.addComponent(filtersLayout);
        componentLayout.addComponent(driversGrid);
        componentLayout.addComponent(controlButtons);

        return componentLayout;
    }

    private void initTextFieldFilter() {
        fieldFilter = new TextField();
        fieldFilter.addValueChangeListener(this::onNameFilterTextChange);
        //fieldFilter.setStyleName(ValoTheme.TEXTFIELD_TINY);
        fieldFilter.setPlaceholder("Search");
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Driver> dataProvider = (ListDataProvider<Driver>) driversGrid.getDataProvider();
        dataProvider.setFilter(Driver::getLastName, s -> caseInsensitiveContains(s, event.getValue()));
    }


    private Boolean caseInsensitiveContains(String where, String what) {
        if (where != null) {
            System.out.println(where + " : " + what);
            return where.toLowerCase().contains(what.toLowerCase());
        }
        return false;
    }


    private void initStatusChooser() {
        statusSelect = new NativeSelect("Status filter");
        List<String> statusList = Arrays.asList("All",
                DriverStatusEnum.getStatusValue(DriverStatusList.OFF_DUTY),
                DriverStatusEnum.getStatusValue(DriverStatusList.FREE),
                DriverStatusEnum.getStatusValue(DriverStatusList.ON_CALL),
                DriverStatusEnum.getStatusValue(DriverStatusList.PERFORMING_ORDER));
        statusSelect.setItems(statusList);

        statusSelect.setEmptySelectionAllowed(false);
        statusSelect.setSelectedItem(statusList.get(0));

        statusSelect.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
                switch (valueChangeEvent.getValue().toString()) {
                    case "All":
                        driversGrid.setItems(adminService.getActiveDrivers());
                        break;
                    case "Off duty":
                        driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.OFF_DUTY));
                        break;
                    case "Free":
                        driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.FREE));
                        break;
                    case "On call":
                        driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.ON_CALL));
                        break;
                    case "Performing order":
                        driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.PERFORMING_ORDER));
                        break;
                }
            }
        });
    }


    private Grid<Driver> generateDriversGrid() {
        driversGrid = new Grid<>();
        driversGrid.setStyleName(ValoTheme.TABLE_SMALL);
        driversGrid.setItems(adminService.getActiveDrivers());
        driversGrid.addColumn(Driver::getObjectId).setCaption("№");
        driversGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driversGrid.addColumn(Driver::getFirstName).setCaption("First name");
        driversGrid.addColumn(Driver::getMiddleName).setCaption("Middle name");
        driversGrid.addColumn(Driver::getPhoneNumber).setCaption("Phone number");
        driversGrid.addColumn(Driver::getEmail).setCaption("Email");
        driversGrid.addColumn(driver -> DriverStatusEnum.getStatusValue(driver.getStatus())).setCaption("Status");
        driversGrid.addColumn(Driver::getRating).setCaption("Rating");
        driversGrid.addColumn(Driver::getHireDate).setCaption("Hire date");
        driversGrid.addColumn(Driver::getExperience).setCaption("Exp");

        return driversGrid;
    }

    private void setGridSettings(Grid<Driver> driversGrid) {
        driversGrid.setSizeFull();
    }

    private void initCreateDriverWindow() {
        createDriverWindow = new Window("Add new driver");
        createDriverWindow.center();
        createDriverWindow.setModal(true);
        createDriverWindow.setContent(driverCreatePopUp);
    }

    private void initDriverInfoWindow() {
        driverInfoWindow = new Window("Driver information");
        driverInfoWindow.center();
        driverInfoWindow.setModal(true);
        driverInfoWindow.setContent(driverInfoPopUp);
    }

    private void initUpdateInfoWindow() {
        updateDriverWindow = new Window("Update driver");
        updateDriverWindow.center();
        updateDriverWindow.setModal(true);
        updateDriverWindow.setContent(driverUpdatePopUp);
    }

    public Window getDriversCreateSubWindow() {
        return createDriverWindow;
    }

    public Window getUpdateDriverWindow() {
        return updateDriverWindow;
    }

    private HorizontalLayout getControlButtonsLayout() {
        HorizontalLayout controlButtonsLayout = new HorizontalLayout();
        controlButtonsLayout.setMargin(false);
        controlButtonsLayout.setSpacing(false);

        Button btnAddDriver = new Button("Add driver", VaadinIcons.PLUS);
        controlButtonsLayout.addComponent(btnAddDriver);
        controlButtonsLayout.setComponentAlignment(btnAddDriver, Alignment.BOTTOM_LEFT);
        btnAddDriver.addClickListener(event -> {
            UI.getCurrent().addWindow(createDriverWindow);
        });


        Button btnDeleteDriver = new Button("Delete driver", VaadinIcons.FILE_REMOVE);
        controlButtonsLayout.addComponent(btnDeleteDriver);
        controlButtonsLayout.setComponentAlignment(btnDeleteDriver, Alignment.BOTTOM_LEFT);
        btnDeleteDriver.addClickListener(event -> {
            Driver driver = driversGrid.asSingleSelect().getValue();
            String firstName = driver.getFirstName();
            String lastName = driver.getLastName();
            MessageBox
                    .createQuestion()
                    .withCaption("Delete")
                    .withMessage("Are you want to delete " + firstName + " " + lastName + "?")
                    .withYesButton(() -> {
                        adminService.deleteModel(driver);
/*
                        driversList.remove(driver);
                        driversGrid.setItems(driversList);
*/
                    refreshGrid();
                    })
                    .withNoButton(() -> {})
                    .open();
        });


        Button btnUpdateDriver = new Button("Update driver", VaadinIcons.UPLOAD);
        controlButtonsLayout.addComponent(btnUpdateDriver);
        controlButtonsLayout.setComponentAlignment(btnUpdateDriver, Alignment.BOTTOM_LEFT);
        btnUpdateDriver.addClickListener(event -> {
            if(!driversGrid.asSingleSelect().isEmpty() ) {
                Driver driver = driversGrid.asSingleSelect().getValue();
                driverUpdatePopUp.init(driver);
                UI.getCurrent().addWindow(updateDriverWindow);
                updateDriverWindow.center();
            }
        });


        Button btnDriverInfo = new Button("Driver info", VaadinIcons.INFO);
        controlButtonsLayout.addComponent(btnDriverInfo);
        controlButtonsLayout.setComponentAlignment(btnDriverInfo, Alignment.BOTTOM_RIGHT);
        btnDriverInfo.addClickListener(event ->{
            if(!driversGrid.asSingleSelect().isEmpty() ) {
                Driver driver = driversGrid.asSingleSelect().getValue();
                driverInfoPopUp.init(driver);
                UI.getCurrent().addWindow(driverInfoWindow);
            }
        });

        return controlButtonsLayout;
    }

    public void refreshGrid(){
        if (statusSelect.getValue().toString().equals("All")) {
            driversGrid.setItems(adminService.getActiveDrivers());
        }else if (statusSelect.getValue().toString().equals("Off duty")) {
            driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.OFF_DUTY));
        }else if (statusSelect.getValue().toString().equals("Free")) {
            driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.FREE));
        }else if (statusSelect.getValue().toString().equals("On call")) {
            driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.ON_CALL));
        }else if (statusSelect.getValue().toString().equals("Performing order")) {
            driversGrid.setItems(adminService.getDriversByStatusId(DriverStatusList.PERFORMING_ORDER));
        }
        //driversGrid.setItems(driversList);
    }

    public Grid<Driver> getDriversGrid() {
        return driversGrid;
    }

    public List<Driver> getApprovedDriversList() {
        return driversList;
    }

    public void setDriversList(List<Driver> driversList) {
        this.driversList = driversList;
    }
}

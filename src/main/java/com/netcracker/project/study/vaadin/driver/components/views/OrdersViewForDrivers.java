package com.netcracker.project.study.vaadin.driver.components.views;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.components.tabs.AllOrdersTab;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@SpringView(name = OrdersViewForDrivers.VIEW_NAME)
public class OrdersViewForDrivers extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ordersForDriverPage";

    @Autowired
    private AllOrdersTab allOrders;

    @Autowired
    private NewOrdersTab newOrders;

    Driver driver;

    TabSheet tabSheet;

    VerticalLayout rootLayout;

    @Autowired
    PersistenceFacade facade;

    Panel driverPanel;
    Panel statusPanel;
    Panel ratingPanel;

    @Autowired
    OrderService orderService;

    @Autowired
    DriverService driverService;

    Panel currentOrderPanel;

    OrderInfo currentOrder;

    Button startPerformingButton;
    Button finishPerformingButton;

    Window window;

    long startkm;
    long finishkm;

    @PostConstruct
    public void init() {
        rootLayout = new VerticalLayout();
        initFakeDriver();
        currentOrderPanel = getCurrentorderPanel();

        initPerformingbutton();
        initPerformedbutton();

        setButtonsEnabled();

        tabSheet = getTabSheet();
        tabSheet.setHeight(100,Unit.PERCENTAGE);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        driverPanel = getDriverInfo();
        statusPanel = getDriverStatus();
        ratingPanel = getDriverRating();

        horizontalLayout.addComponents(driverPanel,statusPanel,ratingPanel);
        horizontalLayout.setSizeFull();

        rootLayout.addComponent(horizontalLayout);
        rootLayout.addComponent(tabSheet);

        addComponent(rootLayout);
    }


    private void setButtonsEnabled(){
        if(currentOrder == null){
            startPerformingButton.setEnabled(false);
            finishPerformingButton.setEnabled(false);
        }else{
            if(currentOrder.getStatus().equals("Accepted")){
                startPerformingButton.setEnabled(true);
                finishPerformingButton.setEnabled(false);
            }
            if(currentOrder.getStatus().equals("Performing")){
                startPerformingButton.setEnabled(false);
                finishPerformingButton.setEnabled(true);
            }
        }

    }

    public Driver getDriver(){
        return driver;
    }

    private void initFakeDriver(){
        driver = facade.getOne(BigInteger.valueOf(202),Driver.class);
    }

    private String getDriverName(){
        return driver.getFirstName() + " " + driver.getLastName();
    }

    private Panel getDriverInfo(){
        Panel panel = new Panel("Your profile: ");
        Label driverInfo = new Label("<b>" + getDriverName() + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(driverInfo);
        horizontalLayout.setComponentAlignment(driverInfo,Alignment.BOTTOM_CENTER);
        panel.setContent(horizontalLayout);

        return panel;
    }

    private Panel getDriverStatus(){
        Panel panel = new Panel("Your status: ");
        Label statusInfo = new Label("<b>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(statusInfo);
        panel.setContent(horizontalLayout);

        return panel;
    }

    private Panel getDriverRating(){
        Panel panel = new Panel("Your rating: ");
        Label ratingInfo = new Label("<b>" + driver.getRating() + "</b>", ContentMode.HTML);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(ratingInfo);
        panel.setContent(horizontalLayout);

        return panel;
    }

    public void setDriver(Driver driver){
        this.driver = driver;
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getAllOrdersControlTab(), "All orders (" + getAllOrdersCount() + ")");
        tabSheet.addTab(getNewOrdersControlTab(),"New orders (" + getNewOrdersCount() + ")");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(currentOrderPanel);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(startPerformingButton);
        horizontalLayout.addComponent(finishPerformingButton);

        verticalLayout.addComponent(horizontalLayout);
        tabSheet.addTab(verticalLayout,"Current order");

        return tabSheet;
    }

    public void Refresh(){
        initFakeDriver();
        driverPanel.setContent(new Label("<b>" + getDriverName() + "</b>", ContentMode.HTML));
        statusPanel.setContent(new Label("<b>" + DriverStatusEnum.getStatusValue(driver.getStatus()) + "</b>", ContentMode.HTML));
        ratingPanel.setContent(new Label("<b>" + driver.getRating() + "</b>", ContentMode.HTML));
        tabSheet.getTab(1).setCaption("New orders (" + getNewOrdersCount() + ")");
        allOrders.refreshGrid();
        refreshCurrentOrderPanel();
        setButtonsEnabled();
    }

    private void refreshCurrentOrderPanel(){
        List<Order>orders = orderService.getCurrentOrderByDriverId(driver.getObjectId());
        if(orders.size() != 0){
            List<OrderInfo> info = orderService.getOrdersInfo(orders);
            currentOrder = info.get(info.size() - 1);
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(getOrderInfoLayout());
            currentOrderPanel.setContent(verticalLayout);
        }else{
            Label label = new Label("You have no order performing");
            currentOrderPanel.setContent(label);
        }
    }
    private Panel getCurrentorderPanel(){
        List<Order>orders = orderService.getCurrentOrderByDriverId(driver.getObjectId());
        Panel panel = new Panel("Current order");
        if(orders.size() != 0){
            List<OrderInfo> info = orderService.getOrdersInfo(orders);
            currentOrder = info.get(info.size() - 1);
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addComponent(getOrderInfoLayout());
            panel.setContent(verticalLayout);
        }else{
            Label label = new Label("You have no order performing");
            panel.setContent(label);
        }
        return panel;

    }

    private void initPerformingbutton(){
        startPerformingButton = new Button("Start Performing");
        startPerformingButton.addClickListener(event -> {

            TextField textField = new TextField("Count of km");
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponent(textField);

            Button ok = new Button("OK");
            ok.addClickListener(newEvent->{
                try{
                    startkm = Long.parseLong(textField.getValue());
                    orderService.changeStatus(OrderStatus.PERFORMING, currentOrder.getObjectId());
                    driverService.changeStatus(DriverStatusList.PERFORMING_ORDER, driver.getObjectId());
                }catch(NumberFormatException e){
                }

            });

            horizontalLayout.addComponent(ok);

            window = new Window();
            window.setCaption("Enter km");
            window.center();
            window.setModal(true);
            window.setContent(horizontalLayout);
            UI.getCurrent().addWindow(window);
            Refresh();

        });
    }

    private void initPerformedbutton(){
        finishPerformingButton = new Button("Finish Performing");
        finishPerformingButton.addClickListener(event -> {

            TextField textField = new TextField("Count of km");
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponent(textField);

            Button ok = new Button("OK");
            ok.addClickListener(newEvent->{
                try{
                    finishkm = Long.parseLong(textField.getValue());
                    long distance = finishkm - startkm;
                    orderService.setDistance(currentOrder.getObjectId(),distance);
                    orderService.changeStatus(OrderStatus.PERFORMED, currentOrder.getObjectId());
                    driverService.changeStatus(DriverStatusList.FREE, driver.getObjectId());
                    window.close();
                }catch(NumberFormatException e){
                }

            });


            horizontalLayout.addComponent(ok);

            window = new Window();
            window.setCaption("Enter km");
            window.center();
            window.setModal(true);
            window.setContent(horizontalLayout);
            UI.getCurrent().addWindow(window);

            startPerformingButton.setEnabled(false);
            finishPerformingButton.setEnabled(false);
        });
    }

    private VerticalLayout getOrderInfoLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        Label client = new Label("Client: " + currentOrder.getClientName());
        Label status = new Label("Status: " + currentOrder.getStatus());
        Label cost = new Label("Cost: " + currentOrder.getCost());
        Label distance = new Label("Distance: " + currentOrder.getDistance());
        verticalLayout.addComponents(client,status,cost,distance);
        return  verticalLayout;
    }

    private int getAllOrdersCount(){
        return allOrders.getOrdersList().size();
    }

    private int getNewOrdersCount(){
        return newOrders.getOrdersList().size();
    }

    private VerticalLayout getAllOrdersControlTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        allOrders.setView(this);
        controlLayout.addComponent(allOrders);
        allOrders.setDriver(driver);

        return controlLayout;
    }

    private VerticalLayout getNewOrdersControlTab(){
        VerticalLayout controlLayout = new VerticalLayout();
        newOrders.setView(this);
        newOrders.setDriver(driver);
        controlLayout.addComponent(newOrders);
        return controlLayout;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Refresh();
    }


}

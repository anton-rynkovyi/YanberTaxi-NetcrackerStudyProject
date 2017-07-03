package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@Scope(value = "prototype")
public class AdminOrderInfoPopUp extends VerticalLayout{

    private Order order;

    @Autowired
    AdminService adminService;

    @Autowired
    OrderService orderService;

    @Autowired
    DriverInfoPopUP driverInfoPopUp;

    @Autowired
    ClientInfoPopUp clientInfoPopUp;

    public void init(Order order) {
        this.order = order;
        //initDriverInfoWindow();
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);
        setTextFields(rootLayout);
        addComponent(rootLayout);
    }

  /*  private void initDriverInfoWindow() {
        driverInfoWindow = new PopupView("Driver information", new TextField("Hello"));
        driverInfoWindow.setIcon(VaadinIcons.INFO);
    }
*/
    private void setTextFields(VerticalLayout rootLayout) {
        rootLayout.addComponent(setOrderInfoLayout());
        HorizontalLayout cancelBtn = setControlButtonLayout();
        rootLayout.addComponent(cancelBtn);
        rootLayout.setComponentAlignment(cancelBtn, Alignment.BOTTOM_CENTER);
        //rootLayout.addComponent(driverInfoWindow);
    }

    private HorizontalLayout setControlButtonLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button btnCancel = new Button("Cancel");
        btnCancel.addClickListener(clickEvent -> {
            for (Window window :UI.getCurrent().getWindows()){
                window.close();
            }
        });
        horizontalLayout.addComponent(btnCancel);
        return horizontalLayout;
    }

    private VerticalLayout setOrderInfoLayout() {
        VerticalLayout root = new VerticalLayout();
        root.setSpacing(false);
        root.setMargin(false);

        VerticalLayout fullOrderInfo = new VerticalLayout();
        VerticalLayout mainInfo = new VerticalLayout();
        HorizontalLayout orderForm = new HorizontalLayout();

        Client client = null;
        Driver driver = null;
        if (order.getClientId() != null) {
            client = adminService.getModelById(order.getClientId(), Client.class);
        }
        if (order.getDriverId() != null) {
            driver = adminService.getModelById(order.getDriverId(), Driver.class);
        }

        Label clientName = new Label("Client: -", ContentMode.HTML);
        if(client != null) {
            clientName.setValue("Client: <i>" +
                    client.getFirstName() + " " + client.getLastName() +
                    "</i>");
        }
        Label diverName = new Label("Driver: -", ContentMode.HTML);
        if(driver != null) {
            diverName.setValue("Driver: <i>" +
                    driver.getFirstName() + " " + driver.getLastName() +
                    "</i>");
        }

        Label status = new Label("Status: <i>" + OrderStatusEnum.getStatusValue(order.getStatus()) + "</i>", ContentMode.HTML);
        Label cost = new Label("Cost: <i>" +
                (order.getCost() != null ? order.getCost() + " hrn" : "-") + "</i>", ContentMode.HTML);
        Label distance = new Label("Distance: <i>" +
                (order.getDistance() != null ? order.getDistance() + " km" : "-") + "</i>", ContentMode.HTML);
        Label driverRating = new Label("Driver rating: <i>" +
                (order.getDriverRating() != null ? order.getDriverRating() : "-")+ "</i>", ContentMode.HTML);

        Label[] routes = getRoutes(order.getObjectId());
        VerticalLayout allRoutes = getLayoutWithRoutes(routes);

        mainInfo.addComponents(clientName, diverName, status, cost, distance, driverRating);
        orderForm.addComponents(mainInfo, allRoutes);
        fullOrderInfo.addComponent(orderForm);

        if (order.getDriverMemo() != null) {
            TextArea textArea = new TextArea();
            textArea.setSizeFull();
            textArea.setReadOnly(true);
            textArea.setHeight(80, Unit.PIXELS);
            textArea.setCaptionAsHtml(true);
            textArea.setCaption("Comment");
            textArea.setValue(order.getDriverMemo());
            fullOrderInfo.addComponent(textArea);
        }


        Panel orderPanel = new Panel(fullOrderInfo);
        orderPanel.setWidth(750, Unit.PIXELS);
        orderPanel.setHeightUndefined();
        root.addComponent(orderPanel);

        return root;
    }

    private Label[] getRoutes(BigInteger orderId){
        List<Route> routes = orderService.getRoutes(orderId);
        Label[] routesLables = new Label[routes.size()];

        for(int i = 0; i < routes.size(); i++){
            String value = "Address " + (i+1) + ": ";
            if (i == 0) value = "Starting Point (Address 1): ";
            if (i == routes.size() - 1) value = "Destination (Address " + routes.size() + "): ";
            Label label = new Label(value + "<i>" + routes.get(i).getCheckPoint() + "</i>", ContentMode.HTML);
            routesLables[i] = label;
        }

        return routesLables;
    }

    private VerticalLayout getLayoutWithRoutes(Label[] routes) {
        VerticalLayout layoutWithRoutes = new VerticalLayout();
        for (Label route : routes) {
            layoutWithRoutes.addComponent(route);
        }
        return layoutWithRoutes;
    }
}
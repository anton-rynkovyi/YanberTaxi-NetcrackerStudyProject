package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.AdminService;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class AdminOrderInfoPopUp extends VerticalLayout{

    private Order order;

    @Autowired
    AdminService adminService;

    @Autowired
    DriverInfoPopUP driverInfoPopUp;

    @Autowired
    ClientInfoPopUp clientInfoPopUp;

    private PopupView driverInfoWindow;

    private PopupView clientInfoWindow;

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
        //rootLayout.addComponent(driverInfoWindow);
    }

    private VerticalLayout setOrderInfoLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);

        VerticalLayout orderForm = new VerticalLayout();

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

        Label status = new Label("Status: <i>" + order.getStatus() + "</i>", ContentMode.HTML);
        Label cost = new Label("Cost: <i>" + order.getCost() + "</i>", ContentMode.HTML);
        Label distance = new Label("Distance: <i>" + order.getDistance() + " km</i>", ContentMode.HTML);
        Label driverRating = new Label("Driver rating: <i>" +
                (order.getDriverRating() != null ? order.getDriverRating() + " hrn" : "-")+
                "</i>", ContentMode.HTML);



        orderForm.addComponents(clientName, diverName, status, cost, distance, driverRating);

        if (order.getDriverMemo() != null) {
            TextArea textArea = new TextArea();
            textArea.setSizeFull();
            textArea.setEnabled(false);
            textArea.setCaptionAsHtml(true);
            textArea.setCaption("Comment");
            textArea.setValue(order.getDriverMemo());
            orderForm.addComponent(textArea);
        }


        Panel orderPanel = new Panel(orderForm);
        orderPanel.setWidth(600, Unit.PIXELS);
        orderPanel.setHeightUndefined();
        verticalLayout.addComponent(orderPanel);

        return verticalLayout;
    }
}
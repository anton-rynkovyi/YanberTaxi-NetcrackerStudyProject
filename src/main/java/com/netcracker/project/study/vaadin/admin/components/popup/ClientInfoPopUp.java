package com.netcracker.project.study.vaadin.admin.components.popup;


import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.ClientsGrid;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ClientInfoPopUp extends VerticalLayout {

    private Client client;

    @Autowired
    AdminService adminService;

    @Autowired
    ClientsGrid clientsGrid;

    public void init(Client client) {
        this.client = client;
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        setTextFields(rootLayout);
        addComponent(rootLayout);
    }



    private void setTextFields(VerticalLayout rootLayout) {
        rootLayout.addComponent(setOrderInfoLayout());
        //rootLayout.addComponent(setControlButtonsLayout());
        rootLayout.addComponent(setClientCommentsLayout());
    }

    private HorizontalLayout setOrderInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout orderForm = new VerticalLayout();
        orderForm.setSizeFull();

        Label name = new Label("<h2><b>" + client.getFirstName() + " " + client.getLastName() + "</b></h2><hr>",
                ContentMode.HTML);
        Label midName = new Label("Middle name: <i>" + client.getMiddleName() + "</i>", ContentMode.HTML);
        Label phone = new Label("Phone: <i>" + client.getPhoneNumber() + "</i>", ContentMode.HTML);
        Label points = new Label("Points: <i>" + client.getPoints() + "</i>", ContentMode.HTML);

        orderForm.addComponents(name, midName, phone, points);

        Panel orderPanel = new Panel(orderForm);
        orderPanel.setWidth(500, Unit.PIXELS);
        horizontalLayout.addComponent(orderPanel);

        return horizontalLayout;
    }

    private HorizontalLayout setControlButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

        Button btnCancel = new Button("Cancel");
        btnCancel.addClickListener(clickEvent -> {
            clientsGrid.getViewClientWindow().close();
        });
        horizontalLayout.addComponent(btnCancel);
        return horizontalLayout;
    }


    private VerticalLayout setClientCommentsLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        TextArea textArea;
        List<Order> orderList = adminService.getOrdersByClientId(client.getObjectId());
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getDriverMemo() != null) {
                textArea = new TextArea();
                textArea.setEnabled(false);
                textArea.setSizeFull();
                textArea.setCaptionAsHtml(true);
                if (orderList.get(i).getDriverId() != null) {
                    Driver driver = adminService.getModelById(orderList.get(i).getDriverId(), Driver.class);
                    textArea.setCaption("To " + driver.getFirstName() + " " + driver.getLastName());
                    textArea.setEnabled(false);
                    textArea.setValue(orderList.get(i).getDriverMemo());
                    verticalLayout.addComponent(textArea);
                    verticalLayout.addComponent(new Label());
                }
            }
        }
        return verticalLayout;
    }
}
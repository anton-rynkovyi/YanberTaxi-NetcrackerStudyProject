package com.netcracker.project.study.vaadin.admin.components.popup;


import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.ClientsGrid;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.math.BigInteger;


@SpringComponent
@Scope(value = "prototype")
public class ClientCreatePopUp extends VerticalLayout {

    @Autowired AdminService adminService;

    ClientsGrid clientsGrid;

    @PostConstruct
    public void init() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("600");
        layout.setSpacing(true);
        layout.setMargin(true);
        setTextFields(layout);
        addComponent(layout);
    }

    private void setTextFields(VerticalLayout layout) {

        FormLayout form = new FormLayout();

        TextField lastName = new TextField("Last Name");
        lastName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(false);
        form.addComponent(lastName);

        TextField firstName = new TextField("First Name");
        firstName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(true);
        form.addComponent(firstName);

        TextField middleName = new TextField("Middle Name");
        middleName.setIcon(FontAwesome.USER);
        form.addComponent(middleName);

        TextField phoneNumber = new TextField("Phone number");
        phoneNumber.setIcon(FontAwesome.MOBILE_PHONE);
        form.addComponent(phoneNumber);

        TextField points = new TextField("Points");
        points.setIcon(FontAwesome.MOUSE_POINTER);
        form.addComponent(points);

        layout.addComponent(form);



        Button btnAddToDB = new Button("Add", FontAwesome.PLUS);
        btnAddToDB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Client client = new Client();
                client.setName(lastName.getValue() +" " + firstName.getValue());
                client.setLastName(lastName.getValue());
                client.setFirstName(firstName.getValue());
                client.setMiddleName(middleName.getValue());
                client.setPhoneNumber(phoneNumber.getValue());
                client.setPoints(new BigInteger(points.getValue()));

                adminService.createModel(client);

                clientsGrid.getClientsList().add(client);
                clientsGrid.getClientsGrid().setItems(clientsGrid.getClientsList());
                clientsGrid.getClientsCreateSubWindow().close();

                lastName.setValue("");
                firstName.setValue("");
                middleName.setValue("");
                phoneNumber.setValue("");
                points.setValue("");
            }
        });

        layout.addComponent(btnAddToDB);
    }

    public void setClientsGrid(ClientsGrid clientsGrid) {
        this.clientsGrid = clientsGrid;
    }
}
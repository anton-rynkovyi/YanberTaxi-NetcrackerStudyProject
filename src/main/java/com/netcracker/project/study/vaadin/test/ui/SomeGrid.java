package com.netcracker.project.study.vaadin.test.ui;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Theme("valo")
@SpringUI(path = "")
public class SomeGrid extends UI {

    @Autowired
    PersistenceFacade facade;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutMain = new VerticalLayout();
        VerticalLayout layoutDrivers = new VerticalLayout();
        VerticalLayout layoutClients = new VerticalLayout();
        try {
            layoutDrivers.addComponent(getGridDrivers());
            layoutClients.addComponent(getGridClients());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        HorizontalLayout layoutBtn = new HorizontalLayout();
        Button btnDriverGrid = new Button("Drivers");
        Button btnClientGrid = new Button("Clients");
        layoutBtn.addComponents(btnDriverGrid, btnClientGrid);

        layoutMain.addComponents(layoutClients, layoutBtn);

        setContent(layoutMain);



        btnClientGrid.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                layoutMain.addComponents(layoutClients, layoutBtn);
                setContent(layoutMain);
            }
        });

        btnDriverGrid.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                layoutMain.addComponents(layoutDrivers, layoutBtn);
                setContent(layoutMain);
            }
        });
    }

    public Grid<Client> getGridClients() throws IllegalAccessException, InstantiationException {
        Grid<Client> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(facade.getAll(BigInteger.valueOf(Client.OBJECT_TYPE_ID), Client.class));
        grid.addColumn(Client::getObjectId).setCaption("id");
        grid.addColumn(Client::getLastName).setCaption("Last name");
        grid.addColumn(Client::getFirstName).setCaption("First name");
        grid.addColumn(Client::getMiddleName).setCaption("Middle name");
        grid.addColumn(Client::getPhoneNumber).setCaption("Phone");
        grid.addColumn(Client::getPoints).setCaption("Points");
        return grid;
    }


    public Grid<Driver> getGridDrivers() throws IllegalAccessException, InstantiationException {
        Grid<Driver> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(facade.getAll(BigInteger.valueOf(Driver.OBJECT_TYPE_ID), Driver.class));
        grid.addColumn(Driver::getObjectId).setCaption("id");
        grid.addColumn(Driver::getLastName).setCaption("Last name");
        grid.addColumn(Driver::getFirstName).setCaption("First name");
        grid.addColumn(Driver::getMiddleName).setCaption("Middle name");
        grid.addColumn(Driver::getPhoneNumber).setCaption("Phone");
        grid.addColumn(Driver::getEmail).setCaption("Email");
        grid.addColumn(Driver::getStatus).setCaption("Status");
        grid.addColumn(Driver::getExperience).setCaption("Exp");
        grid.addColumn(Driver::getHireDate).setCaption("Hire date");
        grid.addColumn(Driver::getRating).setCaption("Rating");
        grid.addColumn(Driver::getUnbanDate).setCaption("Unban date");
        return grid;
    }
}

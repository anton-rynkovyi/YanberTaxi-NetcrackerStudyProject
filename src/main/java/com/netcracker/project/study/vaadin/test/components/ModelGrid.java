package com.netcracker.project.study.vaadin.test.components;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.test.ui.DriversCreatePopUp;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ModelGrid {


    @Autowired
    PersistenceFacade facade;


    public Grid<Client> getClientsGrid(){
        Grid<Client> grid = new Grid<>();

        VerticalLayout verticalLayout = new VerticalLayout();

        List<Client> clientList = facade.getAll(BigInteger.valueOf(Client.OBJECT_TYPE_ID), Client.class);
        grid.setItems(clientList);

        grid.setSizeFull();
        grid.addColumn(Client::getObjectId).setCaption("id").setResizable(false);
        grid.addColumn(Client::getLastName).setCaption("Last name");
        grid.addColumn(Client::getFirstName).setCaption("First name");
        grid.addColumn(Client::getMiddleName).setCaption("Middle name");
        grid.addColumn(Client::getPhoneNumber).setCaption("Phone");
        grid.addColumn(Client::getPoints).setCaption("Points");
        grid.addColumn(client -> "Delete")
                .setWidth(100)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                        MessageBox
                                .createQuestion()
                                .withCaption("Delete")
                                .withMessage("Are you sure?")
                                .withYesButton(() -> {
                                    facade.delete(((Client) rendererClickEvent.getItem()).getObjectId());
                                    clientList.remove(rendererClickEvent.getItem());
                                    grid.setItems(clientList);
                                })
                                .withNoButton(() -> {})
                                .open();
                    }
                }));
        grid.addColumn(client -> "Update")
                .setWidth(110)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {

                    }
                }));
        return grid;
    }



    public Grid<Driver> getDriversGrid() {
        Grid<Driver> grid = new Grid<>();
        grid.setSizeFull();
        List<Driver> driverList = facade.getAll(BigInteger.valueOf(Driver.OBJECT_TYPE_ID), Driver.class);
        grid.setItems(driverList);
        grid.addColumn(Driver::getObjectId).setCaption("id").setWidth(50);
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
        grid.addColumn(client -> "Delete")
                .setWidth(100)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                        MessageBox
                                .createQuestion()
                                .withCaption("Delete")
                                .withMessage("Are you sure?")
                                .withYesButton(() -> {
                                    facade.delete(((Driver) rendererClickEvent.getItem()).getObjectId());
                                    driverList.remove(rendererClickEvent.getItem());
                                    grid.setItems(driverList);
                                })
                                .withNoButton(() -> {})
                                .open();
                    }
                }));
        grid.addColumn(client -> "Update")
                .setWidth(100)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                        DriversCreatePopUp driversCreatePopUp = new DriversCreatePopUp();
                        PopupView popupView = new PopupView(null, driversCreatePopUp);
                        popupView.setPopupVisible(true);

                    }
                }));
        return grid;
    }


    public Grid<Order> getOrdersGrid() {
        Grid<Order> grid = new Grid<>();
        grid.setSizeFull();
        List<Order> orderList = facade.getAll(BigInteger.valueOf(Order.OBJECT_TYPE_ID), Order.class);
        grid.setItems(orderList);
        grid.addColumn(Order::getObjectId).setCaption("№");
        grid.addColumn(Order::getClientId).setCaption("Client");
        grid.addColumn(Order::getDriverId).setCaption("Driver");
        grid.addColumn(Order::getStatus).setCaption("Status");
        grid.addColumn(Order::getCost).setCaption("Cost");
        grid.addColumn(Order::getDistance).setCaption("Distance");
        grid.addColumn(Order::getDriverRating).setCaption("Driver rating");
        grid.addColumn(Order::getDriverRating).setCaption("Comment of driver");
        grid.addColumn(order -> "Delete")
                .setWidth(100)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                        MessageBox
                                .createQuestion()
                                .withCaption("Delete")
                                .withMessage("Are you sure?")
                                .withYesButton(() -> {
                                    facade.delete(((Order) rendererClickEvent.getItem()).getObjectId());
                                    orderList.remove(rendererClickEvent.getItem());
                                    grid.setItems(orderList);
                                })
                                .withNoButton(() -> {})
                                .open();
                    }
                }));
        grid.addColumn(client -> "Update")
                .setWidth(100)
                .setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent rendererClickEvent) {
                        Notification.show("Update");
                    }
                }));
        return grid;
    }
}

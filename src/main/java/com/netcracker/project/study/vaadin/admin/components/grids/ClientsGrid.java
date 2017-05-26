package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.popup.ClientsCreatePopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
public class ClientsGrid extends CustomComponent {

    @Autowired ClientsCreatePopUp clientsCreatePopUp;

    @Autowired AdminService adminService;

    private Grid<Client> clientsGrid;

    private List<Client> clientsList;

    private Window window;

    @PostConstruct
    public void init() {
        clientsGrid = generateClientsGrid();
        VerticalLayout componentLayout = getFilledComponentLayout();
        initWindow();
        setGridSettings(clientsGrid);
        setCompositionRoot(componentLayout);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        HorizontalLayout controlButtons = getControlButtons();

        componentLayout.addComponents(clientsGrid);
        componentLayout.addComponent(controlButtons);

        return componentLayout;
    }

    private Grid<Client> generateClientsGrid() {

        Grid<Client> clientsGrid = new Grid<>();
        clientsList = adminService.allModelsAsList(Client.class);
        clientsGrid.setItems(clientsList);

        clientsGrid.addColumn(Client::getObjectId).setCaption("№");
        clientsGrid.addColumn(Client::getLastName).setCaption("Last name");
        clientsGrid.addColumn(Client::getFirstName).setCaption("First name");
        clientsGrid.addColumn(Client::getMiddleName).setCaption("Middle name");
        clientsGrid.addColumn(Client::getPhoneNumber).setCaption("Phone number");
        clientsGrid.addColumn(Client::getPoints).setCaption("Points");

        return clientsGrid;
    }

    private void setGridSettings(Grid<Client> clientsGrid) {
        clientsGrid.setSizeFull();
    }

    public Grid<Client> getClientsGrid() {
        return clientsGrid;
    }

    public List getClientsList() {
        return clientsList;
    }

    private void initWindow() {
        window = new Window("Add new client");
        window.center();
        window.setContent(clientsCreatePopUp);
    }

    public Window getClientsCreateSubWindow() {
        return window;
    }

    private HorizontalLayout getControlButtons() {
        HorizontalLayout controlButtonsLayout = new HorizontalLayout();
        controlButtonsLayout.setWidthUndefined();


        Button btnAddClient = new Button("Add client", FontAwesome.PLUS);
        controlButtonsLayout.addComponent(btnAddClient);
        controlButtonsLayout.setComponentAlignment(btnAddClient, Alignment.BOTTOM_LEFT);
        btnAddClient.addClickListener(event -> {UI.getCurrent().addWindow(window);});


        Button btnDeleteDriver = new Button("Delete client", FontAwesome.REMOVE);
        controlButtonsLayout.addComponent(btnDeleteDriver);
        controlButtonsLayout.setComponentAlignment(btnDeleteDriver, Alignment.BOTTOM_LEFT);
        btnDeleteDriver.addClickListener(event -> {
            Client client = clientsGrid.asSingleSelect().getValue();
            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            MessageBox
                    .createQuestion()
                    .withCaption("Delete")
                    .withMessage("Are you want to delete " + firstName + " " + lastName + "?")
                    .withYesButton(() -> {
                        adminService.deleteModel(client);
                        clientsList.remove(client);
                        clientsGrid.setItems(clientsList);
                    })
                    .withNoButton(() -> {})
                    .open();
        });


        Button btnUpdateDriver = new Button("Update client", FontAwesome.UPLOAD);
        controlButtonsLayout.addComponent(btnUpdateDriver);
        controlButtonsLayout.setComponentAlignment(btnUpdateDriver, Alignment.BOTTOM_LEFT);
        btnDeleteDriver.addClickListener(event -> {
            //todo realization for update button
        });

        return controlButtonsLayout;
    }
}

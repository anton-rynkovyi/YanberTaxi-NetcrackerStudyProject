package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.popup.ClientCreatePopUp;
import com.netcracker.project.study.vaadin.admin.components.popup.ClientInfoPopUp;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
public class ClientsGrid extends CustomComponent {

    @Autowired
    ClientCreatePopUp clientCreatePopUp;

    @Autowired
    ClientInfoPopUp clientInfoPopUp;

    @Autowired AdminService adminService;

    private Grid<Client> clientsGrid;

    private List<Client> clientsList;

    private Window createWindow;

    private Window viewClientWindow;

    public void init() {
        clientsGrid = generateClientsGrid();
        VerticalLayout componentLayout = getFilledComponentLayout();
        initCreateWindow();
        initViewClientWindow();
        setCompositionRoot(componentLayout);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setSizeFull();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        HorizontalLayout controlButtons = getControlButtons();

        componentLayout.addComponents(clientsGrid);
        componentLayout.addComponent(controlButtons);

        return componentLayout;
    }

    private Grid<Client> generateClientsGrid() {
        Grid<Client> clientsGrid = new Grid<>();
        clientsGrid.setSizeFull();

        clientsGrid.addColumn(Client::getObjectId).setCaption("№");
        clientsGrid.addColumn(Client::getLastName).setCaption("Last name");
        clientsGrid.addColumn(Client::getFirstName).setCaption("First name");
        clientsGrid.addColumn(Client::getMiddleName).setCaption("Middle name");
        clientsGrid.addColumn(Client::getPhoneNumber).setCaption("Phone number");
        clientsGrid.addColumn(Client::getPoints).setCaption("Points");

        return clientsGrid;
    }


    public Grid<Client> getClientsGrid() {
        return clientsGrid;
    }

    public List getClientsList() {
        return clientsList;
    }

    private void initCreateWindow() {
        createWindow = new Window("Add new client");
        createWindow.setIcon(VaadinIcons.USER);
        createWindow.center();
        createWindow.setModal(true);
        createWindow.setResizable(false);
        createWindow.setContent(clientCreatePopUp);
    }

    private void initViewClientWindow() {
        viewClientWindow = new Window("Client information");
        viewClientWindow.setIcon(VaadinIcons.INFO);
        viewClientWindow.center();
        viewClientWindow.setModal(true);
        viewClientWindow.setClosable(false);
        viewClientWindow.setContent(clientInfoPopUp);
    }


    public Window getClientsCreateSubWindow() {
        return createWindow;
    }

    private HorizontalLayout getControlButtons() {
        HorizontalLayout controlButtonsLayout = new HorizontalLayout();
        controlButtonsLayout.setMargin(false);
        controlButtonsLayout.setSpacing(false);

        Button btnAddClient = new Button("Add client", FontAwesome.PLUS);
        controlButtonsLayout.addComponent(btnAddClient);
        controlButtonsLayout.setComponentAlignment(btnAddClient, Alignment.BOTTOM_LEFT);
        btnAddClient.addClickListener(event -> {UI.getCurrent().addWindow(createWindow);});


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

        Button btnClientInfo = new Button("Client info", VaadinIcons.INFO);
        controlButtonsLayout.addComponent(btnClientInfo);
        controlButtonsLayout.setComponentAlignment(btnClientInfo, Alignment.BOTTOM_RIGHT);
        btnClientInfo.addClickListener(event ->{
            if(!clientsGrid.asSingleSelect().isEmpty() ) {
                Client client = clientsGrid.asSingleSelect().getValue();
                clientInfoPopUp.init(client);
                UI.getCurrent().addWindow(viewClientWindow);
            }
        });

        return controlButtonsLayout;
    }

    public Window getViewClientWindow() {
        return  viewClientWindow;
    }

    public void refreshGrid(){
        clientsList = adminService.allModelsAsList(Client.class);
        clientsGrid.setItems(clientsList);
    }
}

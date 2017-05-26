package com.netcracker.project.study.vaadin.admin.components.menu;


import com.netcracker.project.study.vaadin.admin.page.AdminPage;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
public class HeaderMenu extends CustomComponent {

    private MenuBar menuBar;

    private HorizontalLayout horizontalLayout;

    public HeaderMenu() {
        menuBar = getMenuBar();
        horizontalLayout = getLayout();
        Label label = new Label("<h3>YanberTaxi</h3>", ContentMode.HTML);
        horizontalLayout.addComponents(menuBar, label);
        setMenuButtons(menuBar);
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        //menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        return menuBar;
    }

    public HorizontalLayout getLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth(180, Unit.PERCENTAGE);
        setCompositionRoot(horizontalLayout);
        return horizontalLayout;
    }

    private void setMenuButtons(MenuBar menuBar) {
        MenuBar.MenuItem btnDrivers = menuBar.addItem("Drivers", FontAwesome.CAR, AdminPage.showDriversView);
        MenuBar.MenuItem btnClients = menuBar.addItem("Clients", FontAwesome.USERS, AdminPage.showClientsView);
        MenuBar.MenuItem btnOrders = menuBar.addItem("Orders", FontAwesome.DOLLAR, AdminPage.showOrdersView);
    }
}

package com.netcracker.project.study.vaadin.admin.components.menu;


import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.components.popup.AdminUpdate;
import com.netcracker.project.study.vaadin.admin.page.AdminPage;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
public class HeaderMenu extends CustomComponent {

    private MenuBar menuBar;

    private HorizontalLayout horizontalLayout;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AdminUpdate AdminWindow;

    public HeaderMenu() {
        horizontalLayout = getLayout();
        horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        menuBar = getMenuBar();
        //Label label = new Label("<h2>YanberTaxi</h2>", ContentMode.HTML);
        /*Label label = new Label("YanberTaxi");
        horizontalLayout.addComponents(label,menuBar);*/
        horizontalLayout.addComponent(menuBar);
        setMenuButtons(menuBar);
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        //menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        return menuBar;
    }

    public HorizontalLayout getLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        //horizontalLayout.setWidth(180, Unit.PERCENTAGE);
        horizontalLayout.setWidth("100%");
        setCompositionRoot(horizontalLayout);
        return horizontalLayout;
    }

    private void setMenuButtons(MenuBar menuBar) {
        MenuBar.MenuItem btnDrivers = menuBar.addItem("Drivers", FontAwesome.CAR, AdminPage.showDriversView);
        MenuBar.MenuItem btnClients = menuBar.addItem("Clients", FontAwesome.USERS, AdminPage.showClientsView);
        MenuBar.MenuItem btnOrders = menuBar.addItem("Orders", FontAwesome.DOLLAR, AdminPage.showOrdersView);
    }
}

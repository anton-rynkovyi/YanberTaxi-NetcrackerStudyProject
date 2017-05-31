package com.netcracker.project.study.vaadin.admin.page;

import com.netcracker.project.study.vaadin.admin.components.menu.HeaderMenu;
import com.netcracker.project.study.vaadin.admin.views.ClientsView;
import com.netcracker.project.study.vaadin.admin.views.DriversView;
import com.netcracker.project.study.vaadin.admin.views.OrdersView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@Theme("valo")
@SpringUI(path = "/admin")
@SpringComponent
public class AdminPage extends UI {

    @Autowired
    private HeaderMenu headerMenu;

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    public static Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout rootLayout = getVerticalLayout();
        rootLayout.setMargin(true);
        rootLayout.setSpacing(false);
        setContent(rootLayout);

        rootLayout.addComponent(headerMenu);

        viewDisplay = getViewDisplay();
        rootLayout.addComponent(viewDisplay);
        rootLayout.setExpandRatio(viewDisplay, 1.0f);


        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(DriversView.VIEW_NAME);
    }


    private VerticalLayout getVerticalLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        //verticalLayout.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        return verticalLayout;
    }

    private Panel getViewDisplay() {
        Panel springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        return  springViewDisplay;
    }


    public static final MenuBar.Command showDriversView = new MenuBar.Command() {
        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            navigator.navigateTo(DriversView.VIEW_NAME);
        }
    };

    public static final MenuBar.Command showClientsView = new MenuBar.Command() {
        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            navigator.navigateTo(ClientsView.VIEW_NAME);
        }
    };

    public static final MenuBar.Command showOrdersView = new MenuBar.Command() {
        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            navigator.navigateTo(OrdersView.VIEW_NAME);
        }
    };
}




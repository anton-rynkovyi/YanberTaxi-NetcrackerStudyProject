package com.netcracker.project.study.vaadin.admin.page;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.netcracker.project.study.vaadin.admin.components.menu.HeaderMenu;
import com.netcracker.project.study.vaadin.admin.components.popup.AdminUpdate;
import com.netcracker.project.study.vaadin.admin.views.ClientsView;
import com.netcracker.project.study.vaadin.admin.views.DriversView;
import com.netcracker.project.study.vaadin.admin.views.OrdersView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Theme("mytheme")
@SpringUI(path = "/admin")
@Title("YanberTaxi-Administration")
@SpringComponent
public class AdminPage extends UI {

    @Autowired
    HeaderMenu headerMenu;

    @Autowired
    SpringViewProvider provider;

    @Autowired
    Copyright bottomTeamLogo;

    private Panel viewDisplay;

    private static Navigator navigator;

    @Autowired
    AdminUpdate AdminWindow;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout rootLayout = getVerticalLayout();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setHeight(100, Unit.PERCENTAGE);
        setContent(rootLayout);

        HorizontalLayout panelCaption = new HorizontalLayout();
        panelCaption.setMargin(new MarginInfo(false, true, false, true));
        panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        panelCaption.setStyleName(MaterialTheme.LAYOUT_CARD);
        //panelCaption.setStyleName("caption");
        panelCaption.setWidth("100%");
        panelCaption.setHeight(50, Unit.PIXELS);
        Label label1 = new Label("<h3>YanberTaxi</h3>", ContentMode.HTML);
        panelCaption.addComponents(label1);
        panelCaption.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);
        //horizontalLayout.addComponent(menuBar);
        panelCaption.setExpandRatio(label1, 1);
        Label label2 = new Label( "Hello, Admin ");
        panelCaption.addComponent(label2);

        Button action = new Button();
        action.setIcon(FontAwesome.PENCIL);
        action.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action.addStyleName(ValoTheme.BUTTON_SMALL);
        action.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        action.addClickListener(clock -> {
            AdminWindow.init();
            getUI().addWindow(AdminWindow);
        });
        panelCaption.addComponent(action);
        Button logOutButton = new Button("LogOut");
        logOutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logOutButton.addStyleName(ValoTheme.BUTTON_SMALL);
        logOutButton.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });
        logOutButton.setIcon(VaadinIcons.EXIT);
        panelCaption.addComponent(logOutButton);
        rootLayout.addComponent(panelCaption);
        rootLayout.addComponent(headerMenu);

        viewDisplay = getViewDisplay();
        rootLayout.addComponent(viewDisplay);
        rootLayout.addComponent(bottomTeamLogo);
        rootLayout.setExpandRatio(viewDisplay, 0.8f);


        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(DriversView.VIEW_NAME);
    }


    private VerticalLayout getVerticalLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setSizeFull();
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




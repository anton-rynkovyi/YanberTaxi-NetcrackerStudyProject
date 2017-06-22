package com.netcracker.project.study.vaadin.driver.page;


import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.client.popups.ClientUpdate;
import com.netcracker.project.study.vaadin.driver.components.popup.DriverUpdate;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


import java.math.BigInteger;

@Theme("valo")
@SpringUI(path = "/driver")
@Title("YanberTaxi-Driver")
public class DriverPage extends UI{

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    static private Navigator navigator;

    private VerticalLayout rootLayout;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DriverUpdate DriverWindow;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setHeight(100, Unit.PERCENTAGE);


        setContent(rootLayout);

        HorizontalLayout panelCaption = new HorizontalLayout();
        panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        panelCaption.setStyleName(MaterialTheme.LAYOUT_CARD);
        panelCaption.setMargin(new MarginInfo(false, true, false, true));
        // panelCaption.addStyleName("v-panel-caption");
        panelCaption.setWidth("100%");
        // panelCaption.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label label1 = new Label("<h3>YanberTaxi</h3>", ContentMode.HTML);
        panelCaption.addComponent(label1);
        panelCaption.setComponentAlignment(label1, Alignment.MIDDLE_LEFT);
        panelCaption.setExpandRatio(label1, 1);
        Driver driver = userDetailsService.getCurrentUser();
        String driverName = driver.getFirstName() + " " + driver.getLastName();
        Label label2 = new Label( "Hello, "+driverName);
        panelCaption.addComponent(label2);

        Button action = new Button();
        action.setIcon(FontAwesome.PENCIL);
        action.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action.addStyleName(ValoTheme.BUTTON_SMALL);
        action.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        action.addClickListener(clock -> {
            DriverWindow.init();
            getUI().addWindow(DriverWindow);
        });
        panelCaption.addComponent(action);
        Button action1 = new Button("LogOut");
        action1.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        action1.addStyleName(ValoTheme.BUTTON_SMALL);
        action1.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            getUI().getSession().close();
            getUI().getPage().setLocation("/authorization");
        });
        panelCaption.addComponent(action1);
        rootLayout.addComponent(panelCaption);

        viewDisplay = new Panel();
        viewDisplay.setSizeFull();

        rootLayout.addComponent(viewDisplay);
        rootLayout.setExpandRatio(viewDisplay, 1.0f);

        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(OrdersViewForDrivers.VIEW_NAME);

        //OrdersViewForDrivers view = (OrdersViewForDrivers)navigator.getCurrentView();

        /*setPollInterval(1000);
        addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
                view.Refresh();
            }
        });*/

    }

}

package com.netcracker.project.study.vaadin.driver.page;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.annotations.Theme;
import com.vaadin.event.UIEvents;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.math.BigInteger;

@Theme("valo")
@SpringUI(path = "/driver")
public class DriverPage extends UI{

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    static private Navigator navigator;

    private VerticalLayout rootLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();

        setContent(rootLayout);

        viewDisplay = new Panel();

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

package com.netcracker.project.study.vaadin.driver.page;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.facade.Facade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.admin.views.OrdersView;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Theme("valo")
@SpringUI(path = "/driver")
public class DriverPage extends UI{

    @Autowired
    private SpringViewProvider provider;

    private Panel viewDisplay;

    static private Navigator navigator;

    private VerticalLayout verticalLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupLayout();
        setupViewDisplay();

        verticalLayout.setExpandRatio(viewDisplay, 1.0f);

        navigator = new Navigator(this, viewDisplay);
        navigator.addProvider(provider);
        navigator.navigateTo(OrdersViewForDrivers.VIEW_NAME);
    }

    private void setupLayout(){
        this.verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        setContent(verticalLayout);
    }

    private void setupViewDisplay() {
        viewDisplay = new Panel();
        viewDisplay.setSizeFull();
        verticalLayout.addComponent(viewDisplay);
    }
}

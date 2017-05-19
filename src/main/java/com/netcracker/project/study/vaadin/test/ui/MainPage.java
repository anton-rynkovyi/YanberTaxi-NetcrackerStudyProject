package com.netcracker.project.study.vaadin.test.ui;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.test.components.HeaderMenu;
import com.netcracker.project.study.vaadin.test.view.AdminView;
import com.vaadin.annotations.Theme;
import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.renderers.*;
import org.springframework.beans.factory.annotation.Autowired;


@Theme("valo")
@SpringUI(path = "s")
public class MainPage extends UI {

    @Autowired
    private HeaderMenu headerMenu;

    @Autowired
    PersistenceFacade facade;

    @Autowired
    DriversCreatePopUp driversCreatePopUp;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = genLayoutContent();
        VerticalLayout viewLayout = new VerticalLayout();
        setContent(layoutContent);
        layoutContent.addComponents(headerMenu, viewLayout);

        Navigator navigator = new Navigator(this, viewLayout);
        navigator.addView("admin", AdminView.class);
    }



    private VerticalLayout genLayoutContent(){
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setSizeFull();
        layoutContent.setMargin(false);
        layoutContent.setSpacing(false);
        return layoutContent;
    }

}

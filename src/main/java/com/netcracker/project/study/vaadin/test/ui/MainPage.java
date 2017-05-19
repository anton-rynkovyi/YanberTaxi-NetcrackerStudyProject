package com.netcracker.project.study.vaadin.test.ui;


import com.netcracker.project.study.vaadin.test.components.HeaderMenu;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import org.springframework.beans.factory.annotation.Autowired;


@Theme("valo")
@SpringUI(path = "")
public class MainPage extends UI {

    @Autowired
    private HeaderMenu headerMenu;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = new VerticalLayout();
        setContent(layoutContent);
        layoutContent.addComponents(headerMenu);

    }

}

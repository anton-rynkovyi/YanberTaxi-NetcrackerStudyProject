package com.netcracker.project.study.vaadin.test;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class NavigatorUI extends UI {
    Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Navigation Example");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("one", new OneUI());
        //navigator.addView(MAINVIEW, new MainView());
    }
}

package com.netcracker.project.study.vaadin.test;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI(path = "/one")
public class OneUI extends UI implements View{


    @Override
    protected void init(VaadinRequest vaadinRequest) {

        HorizontalLayout layout = new HorizontalLayout();


        setContent(layout);
        Button button = new Button("Go to main page");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

            }
        });
        layout.addComponent(button);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to the OneUI");
    }
}

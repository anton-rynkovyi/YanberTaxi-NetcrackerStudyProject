package com.netcracker.project.study.vaadin.client.page;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@Theme("valo")
@SpringUI(path = "client")
public class ClientPage extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.addComponent(new Label("It's page for CLIENTS."));
        //formLayout.addComponent(new Label("In development :("));
        formLayout.addComponent(new Label("Try again later..."));
        setContent(formLayout);
    }
}

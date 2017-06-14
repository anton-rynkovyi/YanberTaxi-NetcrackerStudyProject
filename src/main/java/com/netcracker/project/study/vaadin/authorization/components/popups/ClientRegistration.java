package com.netcracker.project.study.vaadin.authorization.components.popups;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
public class ClientRegistration extends VerticalLayout{

    public ClientRegistration() {
        VerticalLayout root = genRootLayout();
        root.addComponent(genFields());
    }

    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(500, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private Component genFields() {
        VerticalLayout verticalLayout = new VerticalLayout();
        //TextField
        return verticalLayout;
    }
}

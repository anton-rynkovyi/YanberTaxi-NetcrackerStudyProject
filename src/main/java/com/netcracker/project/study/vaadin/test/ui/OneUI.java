package com.netcracker.project.study.vaadin.test.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@Theme("mytheme")
@SpringUI(path = "/one")
public class OneUI extends UI{

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

}

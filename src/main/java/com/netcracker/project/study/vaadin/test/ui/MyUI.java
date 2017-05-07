package com.netcracker.project.study.vaadin.test.ui;

import com.vaadin.ui.*;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button.ClickEvent;

@Theme("mytheme")
@SpringUI(path = "myui")
public class MyUI extends UI{

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        Button button2 = new Button("Go to the OneUI");
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue()
                    + ", it works!"));

            Notification.show("Hello, " + name.getValue());
        });

        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Notification.show("Button 2");
            }
        });

        layout.addComponents(name, button, button2);

        setContent(layout);
    }

}
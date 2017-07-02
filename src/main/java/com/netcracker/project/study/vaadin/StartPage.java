package com.netcracker.project.study.vaadin;

import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.user.User;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Theme("valo")
@Title("YanberTaxi. It's a future!")
@SpringUI(path = "")
@Deprecated
public class StartPage extends UI {


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) vaadinRequest.getUserPrincipal();
        User user = (User) token.getPrincipal();
        if (user.getAuthorities().size() > 0) {
            Role role = user.getAuthorities().get(0);
            if (role.equals(Role.ROLE_ADMIN)) {
                UI.getCurrent().getPage().setLocation("/admin");
            }
            if (role.equals(Role.ROLE_DRIVER)) {
                UI.getCurrent().getPage().setLocation("/driver");
            }
            if (role.equals(Role.ROLE_CLIENT)) {
                UI.getCurrent().getPage().setLocation("/client");
            }
        }
    }


    /*private VerticalLayout getStartWindow(){
        VerticalLayout form = new VerticalLayout();
        form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        form.setMargin(true);
        form.setSpacing(true);
        form.setWidthUndefined();

        Button btnAdmin = new Button("Go to ADMIN page");
        btnAdmin.addClickListener(click ->{getUI().getPage().setLocation("/admin");});
        form.addComponent(btnAdmin);

        Button btnDriver = new Button("Go to DRIVER page");
        btnDriver.addClickListener(click ->{getUI().getPage().setLocation("/driver");});
        form.addComponent(btnDriver);


        Button btnClient = new Button("Go to CLIENT page");
        btnClient.addClickListener(click ->{getUI().getPage().setLocation("/client");});
        form.addComponent(btnClient);

        Label label = new Label("It's not true page. This page in development :(");
        form.addComponent(label);

        return form;
    }

    private VerticalLayout genLayoutContent(){
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setSizeFull();
        layoutContent.setMargin(false);
        layoutContent.setSpacing(false);
        return layoutContent;
    }*/

}
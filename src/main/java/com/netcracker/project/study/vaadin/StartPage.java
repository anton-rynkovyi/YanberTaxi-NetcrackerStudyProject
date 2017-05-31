package com.netcracker.project.study.vaadin;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.menu.HeaderMenuOld;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Theme("valo")
@SpringUI(path = "")
public class StartPage extends UI {

    /*@Autowired
    private HeaderMenuOld headerMenu;*/

    @Autowired
    AdminService adminService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = genLayoutContent();
        setContent(layoutContent);

        List<Driver> driverList = adminService.getBannedDrivers();
        for (int i = 0; i < driverList.size(); i++) {
            adminService.setBanTimer(driverList.get(i));
        }

        //layoutContent.addComponent(headerMenu);
        Window window = new Window("  Enter menu", getStartWindow());
        window.center();
        window.setIcon(FontAwesome.HOME);
        UI.getCurrent().addWindow(window);
    }


    private VerticalLayout getStartWindow(){
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
    }

}
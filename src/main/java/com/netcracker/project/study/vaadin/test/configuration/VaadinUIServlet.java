package com.netcracker.project.study.vaadin.test.configuration;

import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/*", name = "VaadinUIServlet", asyncSupported = true)
//@VaadinServletConfiguration(productionMode = false, ui = OneUI.class)
public class VaadinUIServlet extends SpringVaadinServlet {

}
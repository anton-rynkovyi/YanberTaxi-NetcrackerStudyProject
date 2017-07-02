package com.netcracker.project.study.vaadin.configurations;

import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/*", "/VAADIN/*"},
        name = "vaadinUiServlet", asyncSupported = true)
public class VaadinUIServlet extends SpringVaadinServlet {
    /*@Override
    protected void servletInitialized() throws ServletException {
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();

    }*/
}
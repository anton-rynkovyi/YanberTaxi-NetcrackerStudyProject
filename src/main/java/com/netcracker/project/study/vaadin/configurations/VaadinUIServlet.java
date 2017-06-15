package com.netcracker.project.study.vaadin.configurations;

import com.netcracker.project.study.vaadin.StartPage;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/*", name = "vaadinUiServlet", asyncSupported = true)
public class VaadinUIServlet extends SpringVaadinServlet {
    /*@Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().setSystemMessagesProvider((SystemMessagesProvider) systemMessagesInfo -> {
            CustomizedSystemMessages messages = new CustomizedSystemMessages();
            // Don't show any messages, redirect immediately to the session expired URL
            messages.setSessionExpiredNotificationEnabled(false);
            // Force a logout to also end the HTTP session and not only the Vaadin session
            messages.setSessionExpiredURL("logout");
            // Don't show any message, reload the page instead
            messages.setCommunicationErrorNotificationEnabled(false);
            return messages;
        });
    }*/
}
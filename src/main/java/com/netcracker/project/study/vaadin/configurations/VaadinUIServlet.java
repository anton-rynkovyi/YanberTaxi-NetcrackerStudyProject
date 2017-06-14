package com.netcracker.project.study.vaadin.configurations;

import com.netcracker.project.study.vaadin.StartPage;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/*", name = "vaadinUiServlet", asyncSupported = true)
public class VaadinUIServlet extends SpringVaadinServlet {


}
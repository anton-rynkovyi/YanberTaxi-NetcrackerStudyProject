package com.netcracker.project.study.vaadin.configurations;

import com.netcracker.project.study.vaadin.StartPage;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/*", name = "VaadinUIServlet", asyncSupported = true)
/*@VaadinServletConfiguration(productionMode = false, ui = StartPage.class,
        widgetset = "com.vaadin.tapio.googlemaps.demo.DemoWidgetset")*/
public class VaadinUIServlet extends SpringVaadinServlet {

    /*TouchKitSettings touchKitSettings;

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        touchKitSettings = new TouchKitSettings(getService());
    }

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.endsWith("themes/touchkit/styles.css")) {
                serveDummyFile(response, "max-age=1000000");
                return;
            } else if (pathInfo.endsWith("PING/")) {
                serveDummyFile(response, "no-store, no-cache, max-age=0, must-revalidate");
                return;
            }
        }
        super.service(request, response);
    }

    private void serveDummyFile(HttpServletResponse response, String cacheControl)
            throws IOException {
        response.setContentType("text/css");
        response.setHeader("Cache-Control", cacheControl);
        response.getOutputStream().write("\n".getBytes());
    }
*/
}
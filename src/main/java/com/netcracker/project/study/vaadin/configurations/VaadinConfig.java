package com.netcracker.project.study.vaadin.configurations;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.internal.ScopedEventBus;

@Configuration
@EnableVaadin // this imports VaadinConfiguration
public class VaadinConfig {
    // application specific configurations - register myBean in the context
    /*@Bean
    public VaadinServlet vaadinServlet() {
        return new VaadinUIServlet();
    }*/

    /*@Bean
    public EventBus eventBus() {
        return new ScopedEventBus.DefaultApplicationEventBus();
    }*/
}
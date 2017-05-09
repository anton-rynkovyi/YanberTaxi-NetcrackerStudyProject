package com.netcracker.project.study.vaadin.test.configuration;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableVaadin // this imports VaadinConfiguration
public class VaadinConfiguration {
    // application specific configuration - register myBean in the context
   /* @Bean
    public MyBean myBean() {
        return new MyBean();
    }*/
}
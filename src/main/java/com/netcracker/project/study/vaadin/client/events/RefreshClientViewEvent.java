package com.netcracker.project.study.vaadin.client.events;

import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import org.springframework.beans.factory.annotation.Autowired;


public class RefreshClientViewEvent {

    @Autowired
    private org.vaadin.spring.events.EventBus.UIEventBus uiEventBus;

    private final NewOrdersTab components;
    private final String driverId;

    public RefreshClientViewEvent (NewOrdersTab components, String s) {
        this.components = components;
        driverId = s;
    }
}

package com.netcracker.project.study.vaadin.client.events;

import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import org.springframework.beans.factory.annotation.Autowired;


public class RefreshClientViewEvent {

    private final String driverId;
    private final NewOrdersTab page;


    public RefreshClientViewEvent (NewOrdersTab page, String driverId) {
        this.page = page;
        this.driverId = driverId;
    }

    public String getDriverId() {
        return driverId;
    }

    public NewOrdersTab getPage() {
        return page;
    }
}

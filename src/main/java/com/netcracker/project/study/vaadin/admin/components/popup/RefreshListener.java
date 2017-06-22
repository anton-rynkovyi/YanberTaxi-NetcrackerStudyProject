package com.netcracker.project.study.vaadin.admin.components.popup;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;


@SpringComponent
public class RefreshListener {

    @Autowired
    DriversGrid driversGrid;

    @EventBusListenerMethod
    public void refreshDriverGrid(String str) {
        driversGrid.refreshGrid();
    }

}

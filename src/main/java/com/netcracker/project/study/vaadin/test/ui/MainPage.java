package com.netcracker.project.study.vaadin.test.ui;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.test.components.HeaderMenu;
import com.vaadin.annotations.Theme;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.Renderer;
import org.springframework.beans.factory.annotation.Autowired;

import org.vaadin.gridutil.cell.CellFilterComponent;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.gridutil.renderer.BooleanRenderer;
import org.vaadin.gridutil.renderer.EditButtonValueRenderer;
import org.vaadin.gridutil.renderer.*;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer.EditDeleteButtonClickListener;


@Theme("valo")
@SpringUI(path = "")
public class MainPage extends UI {

    @Autowired
    private HeaderMenu headerMenu;

    @Autowired
    PersistenceFacade facade;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setSizeFull();
        layoutContent.setMargin(true);
        layoutContent.setSpacing(true);


        layoutContent.addComponent(headerMenu);

        setContent(layoutContent);
    }
}

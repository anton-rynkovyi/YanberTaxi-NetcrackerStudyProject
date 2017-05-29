package com.netcracker.project.study.vaadin.admin.components.popup;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;

import java.lang.reflect.Array;
import java.util.*;

import javax.annotation.PostConstruct;

@SpringComponent
public class BanDaysPopUp extends VerticalLayout{

    @PostConstruct
    private void init() {
        VerticalLayout rootLayout = generateRootLayot();
        RadioButtonGroup radioButtonGroup = generateRadioButtons();

        rootLayout.addComponent(radioButtonGroup);
        addComponent(rootLayout);
    }

    private VerticalLayout generateRootLayot() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        return verticalLayout;
    }

    private RadioButtonGroup generateRadioButtons() {
        RadioButtonGroup radioButtonGroup = new RadioButtonGroup<>("For how many days?");
        List<String> badDaysList = Arrays.asList("3 day", "5 days", "7 days");
        radioButtonGroup.setItems(badDaysList);
        return radioButtonGroup;
    }
}

package com.netcracker.project.study.vaadin.common.components;

import com.vaadin.data.HasValue;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;

public class PhoneField extends TextField implements HasValue.ValueChangeListener {

    private boolean isDelete = false;
    private boolean isAdd = true;

    public PhoneField(String caption) {
        super(caption);
        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(this);
        setMaxLength(14);
        //setValue("+38");
        //setCursorPosition(3);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
      /*  if (getValue().length() == 2) {
            setValue("+38()");
            setCursorPosition(4);
        }
        if (getValue().length() == 8) {
            setCursorPosition(8);
        }
        if (getValue().length() == 11) {
            setValue(getValue() + "-");
        }*/

        if (getValue().length() == 3) {
            setValue("(" + getValue() + ")");
        } else if (getValue().length() == 8 || getValue().length() == 11) {
            setValue(getValue() + "-");
        }
    }
}















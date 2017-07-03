package com.netcracker.project.study.vaadin.common.components;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;

public class PhoneField extends TextField implements HasValue.ValueChangeListener {

    private boolean isDelete = false;
    private String str;

    public PhoneField(String caption) {
        super(caption);
        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(this);
        setMaxLength(14);
        setPlaceholder("(000)000-00-00");
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        if (getValue().length() >= 0 && !getValue().contains("(")){
            str = "(" + getValue();
            setValue(str);
            isDelete = false;
        }
        if (getValue().length() > 4 && !getValue().contains(")")){
            str = getValue().substring(0,4) + ")" + getValue().substring(4);
            setValue(str);
            isDelete = false;
        }
        if (getValue().length() == 8 && !getValue().contains("-") && !isDelete){
            str = getValue() + "-";
            setValue(str);
        }
        if (getValue().length() == 9 && !getValue().substring(8).contains("-")){
            str = getValue().substring(0,8) + "-" + getValue().substring(8);
            setValue(str);
        }
        if (getValue().length() == 11 && !isDelete){
            str = getValue() + "-";
            setValue(str);
            isDelete = true;
        }
        if (getValue().length() == 12 && !getValue().substring(11).contains("-")){
            str = getValue().substring(0,11) + "-" + getValue().substring(11);
            setValue(str);
        }
    }
}















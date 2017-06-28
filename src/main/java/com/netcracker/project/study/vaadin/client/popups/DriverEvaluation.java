package com.netcracker.project.study.vaadin.client.popups;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.math.BigInteger;

@SpringComponent
@Scope(value = "prototype")
public class DriverEvaluation extends Window {
    @Autowired
    OrderService orderService;

    @Autowired
    ClientService clientService;

    private Order order;

    public void init() {

        VerticalLayout root = genRootLayout();
        root.addComponent(setRadioButton());
        setModal(true);
        setResizable(false);
        setCaption(" Driver evaluation");
        setContent(root);
    }

    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(200, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private HorizontalLayout setRadioButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        RadioButtonGroup<Integer> single = new RadioButtonGroup<>("Appreciate the driver");
        single.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        RadioButtonGroup.ValueChangeListener singleListener = new RadioButtonListener();
        single.setItems(1,2,3,4,5);
        single.setIcon(VaadinIcons.STAR);
        horizontalLayout.addComponent(single);

        return horizontalLayout;
    }

    class RadioButtonListener implements RadioButtonGroup.ValueChangeListener {
        @Override
        public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
            Order order = orderService.getOrder(new BigInteger("89"));
            clientService.sendDriverRating(order,new BigInteger(valueChangeEvent.toString()));
        }
    }
}
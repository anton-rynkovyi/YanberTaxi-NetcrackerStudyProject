package com.netcracker.project.study.vaadin.client.components;

import com.netcracker.project.study.services.ClientService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;


@SpringComponent
public class OrderMaker extends CustomComponent {

    @Autowired
    ClientService clientService;

    private TextField[] fields = new TextField[5];

    private TextField distance;

    private TextField cost;

    private String[] textFieldsStrings = new String[5];

    private VerticalLayout fieldsLayout = new VerticalLayout();

    private Label notifications;

    private int countOfTextFields = 1;

    public void setcountOfTextFields(int number) {countOfTextFields = number;}

    public int getcountOfTextFields() {return countOfTextFields;}

    @PostConstruct
    public void init() {

        VerticalLayout orderMaker = new VerticalLayout();
        orderMaker.setSpacing(false);
        MarginInfo orderMakerMerginInfo = new MarginInfo(true, true, false, false);
        orderMaker.setMargin(orderMakerMerginInfo);

        notifications = new Label();
        notifications.setVisible(false);
        orderMaker.addComponent(notifications);
        orderMaker.setComponentAlignment(notifications, Alignment.TOP_CENTER);

        HorizontalLayout orderForm = getOrderForm();
        MarginInfo orderFormMerginInfo = new MarginInfo(false, true, false, true);
        orderForm.setMargin(orderFormMerginInfo);
        orderForm.setSpacing(false);
        orderMaker.addComponent(orderForm);
        orderMaker.setComponentAlignment(orderForm, Alignment.MIDDLE_CENTER);

        HorizontalLayout layoutWithMakeOrderBtn = setButton();
        MarginInfo orderMakerBtnMerginInfo = new MarginInfo(true, false, false, true);
        layoutWithMakeOrderBtn.setMargin(orderMakerBtnMerginInfo);
        layoutWithMakeOrderBtn.setSpacing(false);
        orderMaker.addComponent(layoutWithMakeOrderBtn);
        orderMaker.setComponentAlignment(layoutWithMakeOrderBtn, Alignment.BOTTOM_LEFT);

        setCompositionRoot(orderMaker);

    }

    private HorizontalLayout getOrderForm() {
        HorizontalLayout root = new HorizontalLayout();

        HorizontalLayout distanceAndCost = new HorizontalLayout();

        VerticalLayout buttonsLayout = new VerticalLayout();

        buttonsLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        fields[0] = new TextField("Starting Point (Address 1)");
        fields[0].setIcon(VaadinIcons.HOME_O);
        fields[4] = new TextField("Destination (Address 2)");
        fields[4].setIcon(VaadinIcons.FLAG_CHECKERED);

        fieldsLayout.addComponents(fields[0], fields[4]);

        distance = new TextField("Distance");
        distance.setIcon(VaadinIcons.ARROWS_LONG_H);
        cost = new TextField("Cost");
        cost.setIcon(VaadinIcons.MONEY);
        cost.setEnabled(false);

        distanceAndCost.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        distanceAndCost.addComponents(distance, cost);

        fieldsLayout.addComponent(distanceAndCost);

        Button btnAddTextField = new Button("Add address");
        btnAddTextField.setDescription("Please enter the starting point and destination.\n" +
                "If you want to add interjacent point - push this button");
        Button.ClickListener fieldCounter = new TextFieldCounter();
        btnAddTextField.addClickListener(fieldCounter);

        Button btnDeleteTextField = new Button("Delete address");
        btnDeleteTextField.setDescription("If you want to delete last interjacent point - push this button");
        Button.ClickListener fieldDeleter = new TextFieldDeleter();
        btnDeleteTextField.addClickListener(fieldDeleter);

        buttonsLayout.addComponent(btnAddTextField);
        buttonsLayout.addComponent(btnDeleteTextField);

        root.addComponent(fieldsLayout);
        root.setComponentAlignment(fieldsLayout, Alignment.MIDDLE_CENTER);

        root.addComponent(buttonsLayout);
        root.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);

        return root;
    }

    private HorizontalLayout setButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button makeOrderButton = new Button("Make an order", VaadinIcons.TAXI);
        makeOrderButton.setDescription("Press this button to make your order");
        Button.ClickListener ordermakerListener = new OrderMakerListener();
        makeOrderButton.addClickListener(ordermakerListener);

        horizontalLayout.addComponent(makeOrderButton);
        return horizontalLayout;
    }

    class TextFieldCounter implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            int count = getcountOfTextFields();
            if (count != 4) {
                fields[count] = new TextField("Address " + (count+1));
                fields[count].setIcon(VaadinIcons.ROAD);
                fields[4].setCaption("Destination (Address " + (count+2) + ")");
                fieldsLayout.addComponent(fields[count], count);

                setcountOfTextFields(++count);
            } else {
                notifications.setValue("You can't create more then three interjacent points");
                notifications.setVisible(true);
            }
        }
    }

    class TextFieldDeleter implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            int count = getcountOfTextFields();
            if (count != 1) {
                fields[4].setCaption("Destination (Address " + count + ")");
                if (fields[count-1].getValue().isEmpty()) fields[count-1].setValue(" ");
                fieldsLayout.removeComponent(fields[count-1]);

                notifications.setVisible(false);
                setcountOfTextFields(count-1);
            }
        }
    }

    class OrderMakerListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] != null) textFieldsStrings[i] = fields[i].getValue();
            }
            if (!(distance.getValue().isEmpty()) && !(isFieldsEmpty(textFieldsStrings))) {
                notifications.setVisible(false);
                try {
                    String distanceS = distance.getValue();
                    if (distanceS.contains(",")) distanceS = distanceS.replace(',', '.');

                    BigDecimal dist = BigDecimal.valueOf(Double.parseDouble(distanceS));
                    cost.setValue(String.valueOf(dist.multiply(BigDecimal.valueOf(5))));
                    clientService.makeOrder(BigInteger.valueOf(154), dist, textFieldsStrings);

                } catch (NumberFormatException ex) {
                    notifications.setVisible(true);
                    notifications.setValue("Text field \"Distance\" may contain numbers only");
                }
            } else {
                notifications.setValue("Please set all fields");
                notifications.setVisible(true);
            }
        }
    }

    private boolean isFieldsEmpty(String[] textFieldsStrings) {
        for (String textFieldString : textFieldsStrings) {
            if (textFieldString != null && textFieldString.isEmpty()) return true;
        }
        return false;
    }

}

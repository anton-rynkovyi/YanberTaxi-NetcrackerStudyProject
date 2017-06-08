package com.netcracker.project.study.vaadin.client.components;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


@SpringComponent
public class OrderMaker extends CustomComponent {

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    ClientOrdersGrid clientOrdersGrid;

    private TextField[] fields = new TextField[5];

    private TextField distance;

    private TextField cost;

    private String[] textFieldsStrings = new String[5];

    private VerticalLayout fieldsLayout = new VerticalLayout();

    private Label notifications;

    private int countOfTextFields = 1;

    Window window;

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

        HorizontalLayout layoutWithRadioButton = setRadioButton();
        MarginInfo radioButtonBtnMerginInfo = new MarginInfo(true, false, false, true);
        layoutWithRadioButton.setMargin(radioButtonBtnMerginInfo);
        layoutWithRadioButton.setSpacing(false);
        orderMaker.addComponent(layoutWithRadioButton);
        orderMaker.setComponentAlignment(layoutWithRadioButton, Alignment.BOTTOM_LEFT);


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

        Button cancelOrderButton = new Button("Cancel the order", VaadinIcons.CLOSE_BIG);
        cancelOrderButton.setDescription("Press this button to cancel your order");
        Button.ClickListener orderCancelListener = new OrderCancelListener();
        cancelOrderButton.addClickListener(orderCancelListener);


        horizontalLayout.addComponent(makeOrderButton);
        horizontalLayout.addComponent(cancelOrderButton);


        return horizontalLayout;
    }

    private HorizontalLayout setRadioButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();


        RadioButtonGroup<Integer> single = new RadioButtonGroup<>("Single Selection");
        single.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        RadioButtonGroup.ValueChangeListener singleListener = new RadioButtonListener();
        single.setItems(1,2,3,4,5);

        horizontalLayout.addComponent(single);


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
                /*notifications.setValue("You can't create more then three interjacent points");
                notifications.setVisible(true);*/
                initWindow("<b>You can't create more then three interjacent points</b> ");
                UI.getCurrent().addWindow(window);
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
                if (orderService.getActiveOrdersByClientId(new BigInteger("88")).size()>0) {
                   /* notifications.setValue("You have an active order. You can't simultaneously create multiple orders");
                    notifications.setVisible(true);*/
                    initWindow("<b>You have an active order. You can't simultaneously create multiple orders</b> ");
                    UI.getCurrent().addWindow(window);

                } else {
                    notifications.setVisible(false);
                    try {
                        String distanceS = distance.getValue();
                        if (distanceS.contains(",")) distanceS = distanceS.replace(',', '.');

                        BigDecimal dist = BigDecimal.valueOf(Double.parseDouble(distanceS));
                        cost.setValue(String.valueOf(dist.multiply(BigDecimal.valueOf(5))));
                        clientService.makeOrder(BigInteger.valueOf(88), dist, textFieldsStrings);
                        /*notifications.setValue("Order create");
                        notifications.setVisible(true);*/
                        initWindow("<b>Order create</b> ");
                        UI.getCurrent().addWindow(window);
                        clientOrdersGrid.init();

                    } catch (NumberFormatException ex) {
                       /* notifications.setVisible(true);
                        notifications.setValue("Text field \"Distance\" may contain numbers only");*/
                        initWindow("<b>Text field \"Distance\" may contain numbers only</b> ");
                        UI.getCurrent().addWindow(window);
                    }
                }
            } else {
                /*notifications.setValue("Please set all fields");
                notifications.setVisible(true);*/
                initWindow("<b>Please set all fields</b> ");
                UI.getCurrent().addWindow(window);
            }
        }
    }

    class OrderCancelListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
                List<Order> orderList = orderService.getActiveOrdersByClientId(new BigInteger("88"));
                if (orderList.size()>0) {
                    for (Order order : orderList) {
                        orderService.changeStatus(OrderStatus.CANCELED, order.getObjectId());
                    }
                    initWindow("<b>Current order canceled</b> ");
                    UI.getCurrent().addWindow(window);
                    /*notifications.setValue("Current order canceled");
                    notifications.setVisible(true);*/
                    clientOrdersGrid.init();
                } else {
                    initWindow("<b>You don't have an active order</b> ");
                    UI.getCurrent().addWindow(window);
                    /*notifications.setValue("You don't have an active order");
                    notifications.setVisible(true);*/
                }
        }
    }

    class RadioButtonListener implements RadioButtonGroup.ValueChangeListener {

        @Override
        public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
            Order order = orderService.getOrder(new BigInteger("89"));
            clientService.sendDriverRating(order,new BigInteger(valueChangeEvent.toString()));
        }
    }

    private boolean isFieldsEmpty(String[] textFieldsStrings) {
        for (String textFieldString : textFieldsStrings) {
            if (textFieldString != null && textFieldString.isEmpty()) return true;
        }
        return false;
    }

    private void initWindow(String text){
        window = new Window(" Information");
        window.setIcon(FontAwesome.INFO_CIRCLE);
        window.center();
        window.setModal(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        Label content = new Label(text, ContentMode.HTML);
        verticalLayout.addComponent(content);
        window.setContent(verticalLayout);
    }

}

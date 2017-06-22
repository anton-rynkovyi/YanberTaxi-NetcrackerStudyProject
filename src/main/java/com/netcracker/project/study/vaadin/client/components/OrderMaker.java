package com.netcracker.project.study.vaadin.client.components;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.client.components.grids.ClientCurrentOrderGrid;
import com.netcracker.project.study.vaadin.client.components.grids.ClientOrdersGrid;
import com.netcracker.project.study.vaadin.client.views.ClientView;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

@ViewScope
@SpringComponent
public class OrderMaker extends CustomComponent {

    @Autowired
    ClientService clientService;

    @Autowired
    OrderService orderService;

    @Autowired
    ClientOrdersGrid clientOrdersGrid;

    @Autowired
    private ClientCurrentOrderGrid clientCurrentOrderGrid;

    private TextField[] fields = new TextField[5];

    private VerticalLayout fieldsLayout = new VerticalLayout();

    private int countOfTextFields = 1;

    private Window orderMakerWindow;

    private Toastr toastr;

    private Client client;

    private Button newOrderButton, cancelOrderButton;

    private void setcountOfTextFields(int number) {countOfTextFields = number;}

    private int getcountOfTextFields() {return countOfTextFields;}


    @PostConstruct
    public void init() {

        VerticalLayout orderMaker = new VerticalLayout();
        orderMaker.setSpacing(false);
        MarginInfo orderMakerMerginInfo = new MarginInfo(false, true, false, false);
        orderMaker.setMargin(orderMakerMerginInfo);

        HorizontalLayout orderForm = getOrderForm();
        MarginInfo orderFormMerginInfo = new MarginInfo(false, true, false, true);
        orderForm.setMargin(orderFormMerginInfo);
        orderForm.setSpacing(false);
        orderMaker.addComponent(orderForm);
        orderMaker.setComponentAlignment(orderForm, Alignment.MIDDLE_CENTER);

        HorizontalLayout layoutWithMakeOrderBtn = setButton();
        MarginInfo orderMakerBtnMerginInfo = new MarginInfo(false, false, true, true);
        layoutWithMakeOrderBtn.setMargin(orderMakerBtnMerginInfo);
        layoutWithMakeOrderBtn.setSpacing(true);
        orderMaker.addComponent(layoutWithMakeOrderBtn);
        orderMaker.setComponentAlignment(layoutWithMakeOrderBtn, Alignment.BOTTOM_LEFT);

        toastr = new Toastr();
        orderMaker.addComponent(toastr);
        orderMaker.setComponentAlignment(toastr, Alignment.TOP_RIGHT);

        /*HorizontalLayout layoutWithRadioButton = setRadioButton();
        MarginInfo radioButtonBtnMerginInfo = new MarginInfo(true, false, false, true);
        layoutWithRadioButton.setMargin(radioButtonBtnMerginInfo);
        layoutWithRadioButton.setSpacing(false);
        orderMaker.addComponent(layoutWithRadioButton);
        orderMaker.setComponentAlignment(layoutWithRadioButton, Alignment.BOTTOM_LEFT);*/

        setCompositionRoot(orderMaker);
    }

    public void setClient(Client client) {this.client = client;}

    public void disableNewOrderButton(Button button) {newOrderButton = button;}

    public void enableCancelOrderButton(Button button) {cancelOrderButton = button;}

    public void closeOrderMakerWindow(Window orderMakerWindow) {this.orderMakerWindow = orderMakerWindow;}

    private HorizontalLayout getOrderForm() {
        HorizontalLayout root = new HorizontalLayout();

        VerticalLayout buttonsLayout = new VerticalLayout();

        buttonsLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        fields[0] = new TextField("Starting Point (Address 1)");
        fields[0].setIcon(VaadinIcons.HOME_O);
        fields[4] = new TextField("Destination (Address 2)");
        fields[4].setIcon(VaadinIcons.FLAG_CHECKERED);

        fieldsLayout.addComponents(fields[0], fields[4]);

        Button btnAddTextField = new Button("Add address", VaadinIcons.PLUS);
        btnAddTextField.setDescription("Please enter the starting point and destination.\n" +
                "If you want to add interjacent point - push this button");
        Button.ClickListener fieldCounter = new TextFieldCounter();
        btnAddTextField.addClickListener(fieldCounter);
        btnAddTextField.addStyleName(MaterialTheme.BUTTON_FRIENDLY);

        Button btnDeleteTextField = new Button("Delete address", VaadinIcons.MINUS);
        btnDeleteTextField.setDescription("If you want to delete last interjacent point - push this button");
        Button.ClickListener fieldDeleter = new TextFieldDeleter();
        btnDeleteTextField.addClickListener(fieldDeleter);
        btnDeleteTextField.addStyleName(MaterialTheme.BUTTON_DANGER);

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

    /*private HorizontalLayout setRadioButton(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();


        RadioButtonGroup<Integer> single = new RadioButtonGroup<>("Single Selection");
        single.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        RadioButtonGroup.ValueChangeListener singleListener = new RadioButtonListener();
        single.setItems(1,2,3,4,5);

        horizontalLayout.addComponent(single);


        return horizontalLayout;
    }*/
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
                Toast routesCreateToast = ToastBuilder.of(ToastType.Info, "<b>You can't create more then three interjacent points</b> ")
                        .caption("")
                        .options(ToastOptionsBuilder.having()
                                .preventDuplicates(true)
                                .position(ToastPosition.Top_Right)
                                .build())
                        .build();
                toastr.toast(routesCreateToast);
            }
        }
    }

    class TextFieldDeleter implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            int count = getcountOfTextFields();
            if (count != 1) {
                fields[4].setCaption("Destination (Address " + count + ")");
                if (!(fields[count-1].getValue().isEmpty())) fields[count-1].setValue("");
                fieldsLayout.removeComponent(fields[count-1]);
                fields[count-1] = null;

                setcountOfTextFields(count-1);
            } else {
                Toast deleteRoutesTextFieldsToast = ToastBuilder.of(ToastType.Info, "<b>You can't delete this routes points</b> ")
                        .caption("")
                        .options(ToastOptionsBuilder.having()
                                .preventDuplicates(true)
                                .position(ToastPosition.Top_Right)
                                .build())
                        .build();
                toastr.toast(deleteRoutesTextFieldsToast);
            }
        }
    }

    class OrderMakerListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            String[] textFieldsStrings = new String[5];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] != null) textFieldsStrings[i] = fields[i].getValue();
            }
            if (!(isFieldsEmpty(textFieldsStrings))) {
                if (orderService.getActiveOrdersByClientId(client.getObjectId()).size() > 0
                        || orderService.getPerformingOrdersByClientId(client.getObjectId()).size() > 0 ) {
                    Toast toast = ToastBuilder.of(ToastType.Warning, "<b>You have an active order. You can't simultaneously create multiple orders</b> ")
                            .caption("Attention")
                            .options(ToastOptionsBuilder.having()
                                    .preventDuplicates(true)
                                    .position(ToastPosition.Top_Right)
                                    .build())
                            .build();
                    toastr.toast(toast);
                } else {
                    clientService.makeOrder(client.getObjectId(), textFieldsStrings);

                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i] != null) fields[i].clear();
                    }
                    orderMakerWindow.close();
                    cancelOrderButton.setVisible(true);
                    newOrderButton.setVisible(false);

                    clientOrdersGrid.init();
                    clientCurrentOrderGrid.init();

                    Toast orderMakerToast = ToastBuilder.of(ToastType.Success, "Your order was created successfully</b> ")
                            .caption("Succes")
                            .options(ToastOptionsBuilder.having()
                                    .preventDuplicates(true)
                                    .position(ToastPosition.Top_Right)
                                    .build())
                            .build();
                    toastr.toast(orderMakerToast);
                }
            } else {
                Toast setAllFieldsToast = ToastBuilder.of(ToastType.Warning, "<b>You haven't fill all information.\nPlease set all fields</b> ")
                        .caption("Attention")
                        .options(ToastOptionsBuilder.having()
                                .preventDuplicates(true)
                                .position(ToastPosition.Top_Right)
                                .build())
                        .build();
                toastr.toast(setAllFieldsToast);
            }
        }
    }

    /*class RadioButtonListener implements RadioButtonGroup.ValueChangeListener {

        @Override
        public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
            Order order = orderService.getOrder(BigInteger.valueOf(244));
            clientService.sendDriverRating(order,new BigInteger(valueChangeEvent.toString()));
        }
    }*/

    private boolean isFieldsEmpty(String[] textFieldsStrings) {
        for (String textFieldString : textFieldsStrings) {
            if (textFieldString != null && textFieldString.isEmpty()) return true;
        }
        return false;
    }
}

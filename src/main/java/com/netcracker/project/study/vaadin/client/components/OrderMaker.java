package com.netcracker.project.study.vaadin.client.components;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.client.components.grids.ClientCurrentOrderGrid;
import com.netcracker.project.study.vaadin.client.components.grids.ClientOrdersGrid;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.*;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;

import javax.annotation.PostConstruct;

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

        setCompositionRoot(orderMaker);
    }

    public void setClient(Client client) {this.client = client;}

    public void disableNewOrderButton(Button button) {newOrderButton = button;}

    public void enableCancelOrderButton(Button button) {cancelOrderButton = button;}

    public void closeOrderMakerWindow(Window orderMakerWindow) {
        this.orderMakerWindow = orderMakerWindow;
        orderMakerWindow.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i] != null) fields[i].clear();
                }
                for (int i = 1; i < 4; i++){
                    if (fields[i] != null) {
                        fieldsLayout.removeComponent(fields[i]);
                        fields[i] = null;
                    }
                    setcountOfTextFields(1);
                }
            }
        });
    }

    public void showSuccesToaster(Toastr toastr) {this.toastr = toastr;}

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
        btnAddTextField.setWidth(170, Unit.PIXELS);
        btnAddTextField.setDescription("Please enter the starting point and destination.\n" +
                "If you want to add interjacent point - push this button");
        Button.ClickListener fieldCounter = new TextFieldCounter();
        btnAddTextField.addClickListener(fieldCounter);
        btnAddTextField.addStyleName(MaterialTheme.BUTTON_FRIENDLY);

        Button btnDeleteTextField = new Button("Delete address", VaadinIcons.MINUS);
        btnDeleteTextField.setWidth(170, Unit.PIXELS);
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
                makeAndPushToast(ToastType.Info, ToastPosition.Top_Right,"<b>You can't create more then three interjacent points</b> ");
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
                makeAndPushToast(ToastType.Info, ToastPosition.Top_Right,"<b>You can't delete this routes points</b> ");
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
                    makeAndPushToast(ToastType.Warning, ToastPosition.Top_Right,"<b>You have an active order." + ""
                           + " You can't simultaneously create multiple orders</b> ");
                } else {
                    clientService.makeOrder(client.getObjectId(), textFieldsStrings);
                    makeAndPushToast(ToastType.Success, ToastPosition.Top_Right,"<b>Your order was created successfully</b> ");

                    orderMakerWindow.close();
                    cancelOrderButton.setVisible(true);
                    newOrderButton.setVisible(false);

                    clientOrdersGrid.init();
                    clientCurrentOrderGrid.init();
                }
            } else {
                makeAndPushToast(ToastType.Warning, ToastPosition.Top_Right,"<b>You haven't fill" + ""
                       + " all information.\nPlease set all fields</b> " );
            }
        }
    }

    private void makeAndPushToast(ToastType toastType, ToastPosition toastPosition, String toastText){
        Toast toast = ToastBuilder.of(toastType, "<b>"+ toastText + "</b>")
                .options(ToastOptionsBuilder.having()
                        .preventDuplicates(true)
                        .position(toastPosition)
                        .build())
                .build();
        toastr.toast(toast);
    }

    private boolean isFieldsEmpty(String[] textFieldsStrings) {
        for (String textFieldString : textFieldsStrings) {
            if (textFieldString != null && textFieldString.isEmpty()) return true;
        }
        return false;
    }
}

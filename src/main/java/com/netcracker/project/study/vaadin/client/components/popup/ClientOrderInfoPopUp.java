package com.netcracker.project.study.vaadin.client.components.popup;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.model.order.OrderStatusList;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.Toast;
import org.vaadin.addons.ToastPosition;
import org.vaadin.addons.ToastType;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.addons.builder.ToastOptionsBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ClientOrderInfoPopUp extends VerticalLayout {

    @Autowired
    OrderService orderService;

    private Order order;

    private RadioButtonGroup rateRudioButtons;

    private Button btnComment;

    private TextArea textArea;

    private Toastr toastr;

    public void init(Order order, Toastr toastr) {
        this.order = order;
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);
        rootLayout.addComponent(setOrderInfoLayout());
        addComponent(rootLayout);

        this.toastr = toastr;
        addComponent(toastr);
    }

    private VerticalLayout setOrderInfoLayout() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSpacing(false);
        rootLayout.setMargin(false);

        VerticalLayout allInfo = new VerticalLayout();
        HorizontalLayout routesAndDetalies = new HorizontalLayout();
        VerticalLayout orderForm = new VerticalLayout();
        orderForm.setSpacing(false);
        orderForm.setMargin(new MarginInfo(false, false, false, true));

        HorizontalLayout orderNumberInfo = getOrderNumberInfoLayout(order);
        HorizontalLayout driverNameInfo = getDriverNameInfoLayout(order);
        HorizontalLayout statusInfo = getOrderStatusInfoLayout(order);
        HorizontalLayout orderCostInfo = getOrderCostInfoLayout(order);
        HorizontalLayout distanceInfo = getDistanceInfoLayout(order);
        HorizontalLayout[] driverRatingInfo = getDriverRatingLayoutWithPermission(order);

        orderForm.addComponents(orderNumberInfo, driverNameInfo, statusInfo, orderCostInfo, distanceInfo);
        for (int i = 0; i < driverRatingInfo.length; i++){
            orderForm.addComponent(driverRatingInfo[i]);
        }

        Label[] routes = getRoutes(order.getObjectId());
        VerticalLayout allRoutes = getLayoutWithRoutes(routes);
        allRoutes.setSpacing(false);
        allRoutes.setMargin(new MarginInfo(false, false, false, true));

        routesAndDetalies.addComponents(orderForm, allRoutes);
        allInfo.addComponent(routesAndDetalies);

        btnComment = getBtnComment();

        textArea = getTextAreaForComment();

        getCommentPermission(order, textArea, btnComment);

        allInfo.addComponent(textArea);
        allInfo.setComponentAlignment(textArea, Alignment.BOTTOM_CENTER);
        allInfo.addComponent(btnComment);
        allInfo.setComponentAlignment(btnComment, Alignment.BOTTOM_LEFT);

        Panel orderPanel = new Panel(allInfo);
        orderPanel.setWidth(600, Unit.PIXELS);
        orderPanel.setHeightUndefined();
        orderPanel.setWidthUndefined();
        rootLayout.addComponent(orderPanel);

        return rootLayout;
    }

    private Button getBtnComment(){
        Button button = new Button("Leave a comment", VaadinIcons.COMMENT_ELLIPSIS);
        button.setStyleName(MaterialTheme.BUTTON_ICON_ALIGN_RIGHT);
        button.setDescription("Press this button to leave your comment about the driver");
        Button.ClickListener commentLeaverListener = new CommentLeaverListener();
        button.addClickListener(commentLeaverListener);

        return button;
    }

    private TextArea getTextAreaForComment(){
        TextArea textArea = new TextArea();
        textArea.setIcon(VaadinIcons.COMMENT);
        textArea.setEnabled(true);
        textArea.setWidth(800, Unit.PIXELS);
        textArea.setCaption("Comment for Driver");

        return textArea;
    }

    private HorizontalLayout getOrderNumberInfoLayout(Order order) {
        HorizontalLayout orderNumberInfo = new HorizontalLayout();
        Label orderNumber = new Label(" №: " + order.getObjectId());
        Label orderNumberIcon = new Label();
        orderNumberIcon.setIcon(VaadinIcons.HASH);
        orderNumberInfo.addComponents(orderNumberIcon, orderNumber);

        return orderNumberInfo;
    }

    private HorizontalLayout getDriverNameInfoLayout(Order order){
        HorizontalLayout driverNameInfo = new HorizontalLayout();
        Driver driver = order.getDriverOnOrder();
        String driverFullName = driver != null ? driver.getFirstName() + " " + driver.getLastName() : "absent";
        Label driverName = new Label(" Driver: <i>" + driverFullName + "</i>", ContentMode.HTML);
        Label driverNameIcon = new Label();
        driverNameIcon.setIcon(VaadinIcons.MALE);
        driverNameInfo.addComponents(driverNameIcon, driverName);

        return driverNameInfo;
    }

    private HorizontalLayout getOrderStatusInfoLayout(Order order){
        HorizontalLayout statusInfo = new HorizontalLayout();
        Label status = new Label("Order status: <i>" + OrderStatusEnum.getStatusValue(order.getStatus()) + "</i>", ContentMode.HTML);
        Label statusIcon = new Label();
        statusIcon.setIcon(VaadinIcons.RHOMBUS);
        statusInfo.addComponents(statusIcon, status);

        return statusInfo;
    }

    private HorizontalLayout getOrderCostInfoLayout(Order order){
        HorizontalLayout orderCostInfo = new HorizontalLayout();
        BigDecimal orderCost = order.getCost() != null ? order.getCost() : BigDecimal.ZERO;
        Label cost = new Label("Cost: <i>" + orderCost + " hrn" + "</i>", ContentMode.HTML);
        Label costIcon = new Label();
        costIcon.setIcon(VaadinIcons.MONEY);
        orderCostInfo.addComponents(costIcon, cost);

        return orderCostInfo;
    }

    private HorizontalLayout getDistanceInfoLayout(Order order){
        HorizontalLayout distanceInfo = new HorizontalLayout();
        BigInteger orderDistance = order.getDistance() != null ? order.getDistance() : BigInteger.ZERO;
        Label distance = new Label("Distance: <i>" + orderDistance + " km</i>", ContentMode.HTML);
        Label distanceIcon = new Label();
        distanceIcon.setIcon(VaadinIcons.ARROWS_LONG_H);
        distanceInfo.addComponents(distanceIcon, distance);

        return distanceInfo;
    }

    private RadioButtonGroup getRadioButton(){

        RadioButtonGroup<Integer> single = new RadioButtonGroup<>("Choose driver rating");
        single.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        single.setItems(1,2,3,4,5);

        return single;
    }

    private Button getRateButton(){
        Button btnRate = new Button("Rate driver", VaadinIcons.STAR);
        btnRate.addStyleName(MaterialTheme.BUTTON_ICON_ALIGN_RIGHT);
        btnRate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (rateRudioButtons.getValue() != null) {
                    int rating = Integer.parseInt(rateRudioButtons.getValue().toString());
                    orderService.setDriverRating(order, rating);
                    makeAndPushToast(ToastType.Success, ToastPosition.Top_Right, "You have successfully evaluated the driver");
                    init(order, toastr);
                } else {
                    makeAndPushToast(ToastType.Warning, ToastPosition.Top_Right, "You don't choose rating");
                }
            }
        });
        return btnRate;
    }

    private HorizontalLayout[] getDriverRatingLayout(Order order, String labelText){
        HorizontalLayout[] driverRatingInfo;
        if (order.getDriverRating() == null){
            driverRatingInfo = new HorizontalLayout[4];

            Label label = new Label(labelText);
            label.setStyleName(MaterialTheme.LABEL_COLORED);

            driverRatingInfo[0] = new HorizontalLayout(label);
            HorizontalLayout stars = getDriverRatingStars(0);
            driverRatingInfo[1] = new HorizontalLayout(stars);
            rateRudioButtons = getRadioButton();
            driverRatingInfo[2] = new HorizontalLayout(rateRudioButtons);
            driverRatingInfo[3] = new HorizontalLayout(getRateButton());
        } else {
            driverRatingInfo = new HorizontalLayout[1];
            driverRatingInfo[0] = getDriverRatingStars(order.getDriverRating().intValue());
        }
        return driverRatingInfo;
    }

    private HorizontalLayout[] getDriverRatingLayoutWithPermission(Order order){
        HorizontalLayout[] driverRatingLayout;
        if (!(order.getStatus().equals(OrderStatusList.CANCELED))) {
            if (order.getDriverRating() != null) {
                driverRatingLayout = getDriverRatingLayout(order, "");
            } else
            if (order.getStatus().equals(OrderStatusList.PERFORMED)) {
                driverRatingLayout = getDriverRatingLayout(order,"You may rate the driver");
            } else {
                driverRatingLayout = getDriverRatingLayout(order,"You may rate the driver when order has status \"Perfomed\"");
                driverRatingLayout[2].setEnabled(false);
                driverRatingLayout[3].setEnabled(false);
            }
        } else {
            driverRatingLayout = getDriverRatingLayout(order, "You can't rate the driver because order was canceled");
            driverRatingLayout[2].setVisible(false);
            driverRatingLayout[3].setVisible(false);
        }
        return driverRatingLayout;
    }

    private HorizontalLayout getDriverRatingStars(int driverRating){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        int total = 5;

        Label ratingHeader = new Label("Driver rating: ",ContentMode.HTML);
        Label driverRatingIcon = new Label();
        driverRatingIcon.setIcon(VaadinIcons.USER_STAR);
        horizontalLayout.addComponents(driverRatingIcon, ratingHeader);

        for(int i = 0;i < driverRating; i++){
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR);
            horizontalLayout.addComponent(star);
        }

        int remainedRating = total - driverRating;

        for(int i = 0;i < remainedRating; i++){
            Label star = new Label();
            star.setIcon(VaadinIcons.STAR_O);
            horizontalLayout.addComponent(star);
        }
        return horizontalLayout;
    }

    private Label[] getRoutes(BigInteger orderId){
        List<Route> routes = orderService.getRoutes(orderId);
        Label[] routesLables = new Label[routes.size()];

        for(int i = 0; i < routes.size(); i++){
            String value = "<b>Address " + (i+1) + ": </b>";
            if (i == 0) value = "<b>Starting Point (Address 1): </b>";
            if (i == routes.size() - 1) value = "<b>Destination (Address " + routes.size() + "): </b>";
            Label label = new Label(value + routes.get(i).getCheckPoint(), ContentMode.HTML);
            label.setIcon(VaadinIcons.MAP_MARKER);
            if (i == 0) label.setIcon(VaadinIcons.HOME_O);
            if (i == routes.size() - 1) label.setIcon(VaadinIcons.FLAG_CHECKERED);
            routesLables[i] = label;
        }

        return routesLables;
    }

    private VerticalLayout getLayoutWithRoutes(Label[] routes) {
        VerticalLayout layoutWithRoutes = new VerticalLayout();
        for (Label route : routes) {
            layoutWithRoutes.addComponent(route);
        }
        return layoutWithRoutes;
    }

    private void getCommentPermission(Order order, TextArea textArea, Button btnComment){
        if (!(order.getStatus().equals(OrderStatusList.CANCELED))) {
            if (order.getDriverMemo() != null) {
                textArea.setValue(order.getDriverMemo());
                textArea.setReadOnly(true);
                btnComment.setVisible(false);
            } else
            if (order.getStatus().equals(OrderStatusList.PERFORMED)) {
                textArea.setPlaceholder("You are not leaving comment for this driver.\nYou may click on text field and leave your comment");
            } else {
                textArea.setPlaceholder("You may leave your comment when order has status \"Perfomed\"");
                textArea.setReadOnly(true);
                btnComment.setEnabled(false);
            }
        } else {
            textArea.setPlaceholder("You can't leave a comment because order was canceled.");
            textArea.setReadOnly(true);
            btnComment.setEnabled(false);
        }
    }

    class CommentLeaverListener implements Button.ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            String comment = textArea.getValue().trim();
            if (comment.isEmpty()){
                makeAndPushToast(ToastType.Warning, ToastPosition.Top_Right, "You can't leave empty comment");
            } else {
                orderService.setCommentAboutDriver(order, comment);
                makeAndPushToast(ToastType.Success, ToastPosition.Top_Right, "Your comment added successfully");
                init(order, toastr);
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
}

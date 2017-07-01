package com.netcracker.project.study.vaadin.driver.events;

import java.math.BigInteger;

public class CancelClientOrderEvent {

    private BigInteger orderId;

    public CancelClientOrderEvent(BigInteger orderId) {
        this.orderId = orderId;
    }

    public BigInteger getOrderId() {
        return orderId;
    }
}

package com.netcracker.project.study.model.order.order_log;

import java.sql.Time;

/**
 * Created by Mark on 27.04.2017.
 */
public class OrderLog {
    public final int OBJECT_TYPE_ID=5;

    private int orderId;
    private String status;
    private Time timeStamp;

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public Time getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(Time timeStamp){
        this.timeStamp=timeStamp;
    }
}

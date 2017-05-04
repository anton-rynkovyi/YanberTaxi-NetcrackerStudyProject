package com.netcracker.project.study.model.order.route;

import com.netcracker.project.study.model.Model;

public class Route extends Model implements RouteAttr{

    private int orderId;
    private String checkPoint;
    private String showOrder;

    public Route(long objectId){
        this.objectId = objectId;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public String getCheckPoint(){
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint){
        this.checkPoint=checkPoint;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }
}

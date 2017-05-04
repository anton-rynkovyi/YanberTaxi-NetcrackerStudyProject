package com.netcracker.project.study.model.order.route;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;

@ObjectType(objectTypeId = 7)
public class Route extends Model implements RouteAttr{

    @Attribute(attrId = 36)
    @Reference(objectTypeId = 3)
    private int orderId;

    @Attribute(attrId = 37)
    private String checkPoint;

    @Attribute(attrId = 38)
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

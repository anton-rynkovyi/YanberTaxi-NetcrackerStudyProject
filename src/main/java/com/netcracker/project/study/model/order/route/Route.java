package com.netcracker.project.study.model.order.route;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
import com.netcracker.project.study.model.order.OrderAttr;

@ObjectType(objectTypeId = RouteAttr.OBJECT_TYPE_ID)
public class Route extends Model{

    @Reference(attrId = RouteAttr.ORDER_ID_ATTR)
    private int orderId;

    @Attribute(attrId = RouteAttr.CHECK_POINT_ATTR)
    private String checkPoint;

    @Attribute(attrId = RouteAttr.SHOW_ORDER_ATTR)
    private String showOrder;

    public Route() {}

    public Route(String name) {
        super(name);
    }

    public Route(String name, String description) {
        super(name, description);
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

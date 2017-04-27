package com.netcracker.project.study.model.order;

/**
 * Created by Mark on 25.04.2017.
 */
public class Order {
    public final int OBJECT_TYPE_ID=3;

    private int driverId;
    private int clientId;
    private String status;
    private int cost;
    private int distance;
    private int driverRating;
    private String driverMemo;

    public int getDriverId(){
        return  driverId;
    }

    public void setDriverId(int driverId){
        this.driverId=driverId;
    }

    public int getClientId(){
        return  clientId;
    }

    public void setClientId(int clietnId){
        this.clientId=clietnId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public int getCost(){
        return cost;
    }

    public void setCost(int cost){
        this.cost=cost;
    }

    public int getDistance(){
        return distance;
    }

    public void setDistance(int distance){
        this.distance=distance;
    }

    public int getDriverRating(){
        return driverRating;
    }

    public void setDriverRating(int driverRating){
        this.driverRating=driverRating;
    }

    public String getDriverMemo(){
        return  driverMemo;
    }

    public void setDriverMemo(String driverMemo){
        this.driverMemo=driverMemo;
    }

}

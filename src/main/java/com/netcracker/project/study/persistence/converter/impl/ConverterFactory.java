package com.netcracker.project.study.persistence.converter.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.PersistenceEntity;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConverterFactory implements Converter {

    public ConverterFactory(){}

    /**
     * Convert some model to PersistenceEntity
     * @param model
     * @return PersistenceEntity
     */
    @Override
    public PersistenceEntity convertToEntity(Model model) {

        PersistenceEntity entity = new PersistenceEntity();

        Map<Integer,Object>attributes = new HashMap<>();
        entity.setObjectId(model.getObjectId());
        if(model instanceof Driver){
            Driver driver = (Driver)model;
            entity.setObjectTypeId((driver.OBJECT_TYPE_ID));
            attributes.put(1,(driver.getLastName()));
            attributes.put(2,(driver.getFirstName()));
            attributes.put(3,(driver.getMiddleName()));
            attributes.put(4,(driver.getPhoneNumber()));
            attributes.put(5,(driver.getEmail()));
            attributes.put(6,(driver.getHireDate()));
            attributes.put(7,(driver.getExperience()));
            attributes.put(8,(driver.getRating()));
        }else if(model instanceof Client){
            Client client = (Client)model;
            entity.setObjectTypeId(client.OBJECT_TYPE_ID);
            attributes.put(11,client.getLastName());
            attributes.put(12,client.getFirstName());
            attributes.put(13,client.getMiddleName());
            attributes.put(14,client.getPhoneNumber());
            attributes.put(15,client.getPoints());
        }else if(model instanceof Order){
            Order order = (Order)model;
            entity.setObjectTypeId(order.OBJECT_TYPE_ID);
            attributes.put(16,order.getClientId());
            attributes.put(17,order.getDriverId());
            attributes.put(18,order.getStatus());
            attributes.put(19,order.getCost());
            attributes.put(20,order.getDistance());
            attributes.put(21,order.getDriverRating());
            attributes.put(22,order.getDriverMemo());
        }else if(model instanceof Car){
            Car car = (Car)model;
            entity.setObjectTypeId(car.OBJECT_TYPE_ID);
            attributes.put(23,car.getMakeOfCar());
            attributes.put(24,car.getModelType());
            attributes.put(25,car.getReleaseDate());
            attributes.put(26,car.getSeatsCount());
            attributes.put(27,car.isChildSeat());
            attributes.put(28,car.getDriverId());
            attributes.put(29,car.getStateNumber());
        }else if(model instanceof OrderStatus){
            OrderStatus orderStatus = (OrderStatus)model;
            entity.setObjectTypeId(orderStatus.OBJECT_TYPE_ID);
            attributes.put(30,orderStatus.getOrderId());
            attributes.put(31,orderStatus.getStatus());
            attributes.put(32,orderStatus.getTimeStamp());
        }else if(model instanceof DriverStatus){
            DriverStatus driverStatus = (DriverStatus)model;
            entity.setObjectTypeId(driverStatus.OBJECT_TYPE_ID);
            attributes.put(33,driverStatus.getDriverId());
            attributes.put(34,driverStatus.getStatus());
            attributes.put(35,driverStatus.getTimeStamp());
        }else if(model instanceof Route){
            Route route = (Route)model;
            entity.setObjectTypeId(route.OBJECT_TYPE_ID);
            attributes.put(36,route.getOrderId());
            attributes.put(37,route.getCheckPoint());
            attributes.put(38,route.getShowOrder());
        }
        entity.setAttributes(attributes);

        return entity;
    }


    /**
     * Create model from entity
     * @param entity
     * @return
     */
    @Override
    public Model convertToModel(PersistenceEntity entity) {
        long objTypeId = entity.getObjectTypeId();
        Map<Integer,Object> attributes = entity.getAttributes();
        switch((int)objTypeId){
            case 1:
                Driver driver = new Driver(entity.getObjectId());
                driver.setLastName((String)attributes.get(1));
                driver.setFirstName((String)attributes.get(2));
                driver.setMiddleName((String)attributes.get(3));
                driver.setPhoneNumber((String)attributes.get(4));
                driver.setEmail((String)attributes.get(5));
                driver.setHireDate((Date)attributes.get(6));
                driver.setExperience((int)attributes.get(7));
                driver.setRating((int)attributes.get(8));
                driver.setStatus((String)attributes.get(9));
                driver.setUnbanDate((Date)attributes.get(10));
                return driver;
            case 2:
                Client client = new Client(entity.getObjectId());
                client.setLastName((String)attributes.get(11));
                client.setFirstName((String)attributes.get(12));
                client.setMiddleName((String)attributes.get(13));
                client.setPhoneNumber((String)attributes.get(14));
                client.setPoints((int)attributes.get(15));
                return client;
            case 3:
                Order order = new Order(entity.getObjectId());
                order.setClientId((int)attributes.get(16));
                order.setDriverId((int)attributes.get(17));
                order.setStatus((String)attributes.get(18));
                order.setCost((int)attributes.get(19));
                order.setDistance((int)attributes.get(20));
                order.setDriverRating((int)attributes.get(21));
                order.setDriverMemo((String)attributes.get(22));
                return order;
            case 4:
                Car car = new Car(entity.getObjectId());
                car.setMakeOfCar((String)attributes.get(23));
                car.setModelType((String)attributes.get(24));
                car.setReleaseDate((Date)attributes.get(25));
                car.setSeatsCount((int)attributes.get(26));
                car.setChildSeat((boolean)attributes.get(27));
                car.setDriverId((int)attributes.get(28));
                car.setStateNumber((int)attributes.get(29));
                return car;
            case 5:
                OrderStatus orderStatus = new OrderStatus(entity.getObjectId());
                orderStatus.setOrderId((int)attributes.get(30));
                orderStatus.setStatus((String)attributes.get(31));
                orderStatus.setTimeStamp((Time)attributes.get(32));
                return orderStatus;
            case 6:
                DriverStatus driverStatus = new DriverStatus(entity.getObjectId());
                driverStatus.setDriverId((int)attributes.get(33));
                driverStatus.setStatus((String)attributes.get(34));
                driverStatus.setTimeStamp((Time)attributes.get(35));
                return driverStatus;
            case 7:
                Route route = new Route(entity.getObjectId());
                route.setOrderId((int)attributes.get(36));
                route.setCheckPoint((String)attributes.get(37));
                route.setShowOrder((String)attributes.get(38));
                return route;
            default:
                return null;
        }
    }
}

package com.netcracker.project.study.persistence.converter.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
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

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Component
public class ConverterFactory implements Converter {

    public ConverterFactory() {
    }

    @Override
    public PersistenceEntity convertToEntity(Model model) throws IllegalAccessException, NoSuchFieldException {

        Field name;
        Field description;
        Field objectId;
        Field parentId;
        Class modelClass = model.getClass();
        PersistenceEntity entity = new PersistenceEntity();

        if (modelClass.isAnnotationPresent(ObjectType.class)) {
            ObjectType objectType = (ObjectType) modelClass.getAnnotation(ObjectType.class);
            entity.setObjectTypeId(objectType.objectTypeId());
        }

        Map<Long, Object> attributes = new HashMap<>();
        Map <Long,Long> references = new HashMap<>();

        Field[] fields = modelClass.getDeclaredFields();

        Attribute attributeAnnotation;
        Reference referenceAnnotation;
        Object fieldValue;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                attributeAnnotation = field.getAnnotation(Attribute.class);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    fieldValue = field.get(model);
                    field.setAccessible(false);
                }else{
                    fieldValue = field.get(model);
                }
                attributes.put(attributeAnnotation.attrId(), fieldValue);

            }

            if(field.isAnnotationPresent(Reference.class)){
                attributeAnnotation = field.getAnnotation(Attribute.class);
                referenceAnnotation = field.getAnnotation(Reference.class);
                references.put(attributeAnnotation.attrId(),referenceAnnotation.objectTypeId());
            }
        }

        Class superClass = modelClass.getSuperclass();

        name = superClass.getDeclaredField("name");
        description = superClass.getDeclaredField("description");
        objectId = superClass.getDeclaredField("objectId");
        parentId = superClass.getDeclaredField("parentId");

        name.setAccessible(true);
        description.setAccessible(true);
        objectId.setAccessible(true);
        parentId.setAccessible(true);

        entity.setName((String) name.get(model));
        entity.setObjectId((Long) objectId.get(model));
        entity.setDescription((String) description.get(model));
        entity.setParentId((Long)parentId.get(model));

        entity.setAttributes(attributes);
        entity.setReferences(references);

        name.setAccessible(false);
        description.setAccessible(false);
        objectId.setAccessible(false);
        parentId.setAccessible(false);

        return entity;
    }

    @Override
    public Model convertToModel(PersistenceEntity entity) {
        long objTypeId = entity.getObjectTypeId();
        Map<Long, Object> attributes = entity.getAttributes();

        switch ((int) objTypeId) {
            case 1:
                Driver driver = new Driver();
                driver.setLastName((String) attributes.get(1l));
                driver.setFirstName((String) attributes.get(2l));
                driver.setMiddleName((String) attributes.get(3l));
                driver.setPhoneNumber((String) attributes.get(4l));
                driver.setEmail((String) attributes.get(5l));
                driver.setHireDate((Timestamp) attributes.get(6l));
                driver.setExperience(Integer.parseInt(attributes.get(7l)+""));
                driver.setRating(Integer.parseInt(attributes.get(7l)+""));
                driver.setStatus((String) attributes.get(9l));
                driver.setUnbanDate((Date) attributes.get(10l));
                return driver;
            case 2:
                Client client = new Client();
                client.setLastName((String) attributes.get(11l));
                client.setFirstName((String) attributes.get(12l));
                client.setMiddleName((String) attributes.get(13l));
                client.setPhoneNumber((String) attributes.get(14l));
                client.setPoints(Integer.parseInt(attributes.get(15l)+""));
                return client;
            case 3:
                Order order = new Order();
                order.setClientId(Integer.parseInt(attributes.get(16l)+""));
                order.setDriverId(Integer.parseInt(attributes.get(16l)+""));
                order.setStatus((String) attributes.get(18l));
                order.setCost(Integer.parseInt(attributes.get(16l)+""));
                order.setDistance(Integer.parseInt(attributes.get(16l)+""));
                order.setDriverRating(Integer.parseInt(attributes.get(16l)+""));
                order.setDriverMemo((String) attributes.get(22l));
                return order;
            case 4:
                Car car = new Car();
                car.setMakeOfCar((String) attributes.get(23l));
                car.setModelType((String) attributes.get(24l));
                car.setReleaseDate((Date) attributes.get(25l));
                car.setSeatsCount(Integer.parseInt(attributes.get(16l)+""));
                car.setChildSeat((boolean) attributes.get(27l));
                car.setDriverId(Integer.parseInt(attributes.get(16l)+""));
                car.setStateNumber(Integer.parseInt(attributes.get(16l)+""));
                return car;
            case 5:
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderId(Integer.parseInt(attributes.get(16l)+""));
                orderStatus.setStatus((String) attributes.get(31l));
                orderStatus.setTimeStamp((Time) attributes.get(32l));
                return orderStatus;
            case 6:
                DriverStatus driverStatus = new DriverStatus();
                driverStatus.setDriverId(Integer.parseInt(attributes.get(16l)+""));
                driverStatus.setStatus((String) attributes.get(34l));
                driverStatus.setTimeStamp((Time) attributes.get(35l));
                return driverStatus;
            case 7:
                Route route = new Route(entity.getObjectId());
                route.setOrderId(Integer.parseInt(attributes.get(16l)+""));
                route.setCheckPoint((String) attributes.get(37l));
                route.setShowOrder((String) attributes.get(38l));
                return route;
            default:
                return null;
        }
    }
}

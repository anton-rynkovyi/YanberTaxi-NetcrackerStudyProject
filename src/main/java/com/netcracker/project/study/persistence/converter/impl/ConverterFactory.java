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
import java.sql.Time;
import java.sql.Date;
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
                driver.setLastName((String) attributes.get(1));
                driver.setFirstName((String) attributes.get(2));
                driver.setMiddleName((String) attributes.get(3));
                driver.setPhoneNumber((String) attributes.get(4));
                driver.setEmail((String) attributes.get(5));
                driver.setHireDate((Date) attributes.get(6));
                driver.setExperience((int) attributes.get(7));
                driver.setRating((int) attributes.get(8));
                driver.setStatus((String) attributes.get(9));
                driver.setUnbanDate((Date) attributes.get(10));
                return driver;
            case 2:
                Client client = new Client();
                client.setLastName((String) attributes.get(11));
                client.setFirstName((String) attributes.get(12));
                client.setMiddleName((String) attributes.get(13));
                client.setPhoneNumber((String) attributes.get(14));
                client.setPoints((int) attributes.get(15));
                return client;
            case 3:
                Order order = new Order();
                order.setClientId((int) attributes.get(16));
                order.setDriverId((int) attributes.get(17));
                order.setStatus((String) attributes.get(18));
                order.setCost((int) attributes.get(19));
                order.setDistance((int) attributes.get(20));
                order.setDriverRating((int) attributes.get(21));
                order.setDriverMemo((String) attributes.get(22));
                return order;
            case 4:
                Car car = new Car();
                car.setMakeOfCar((String) attributes.get(23));
                car.setModelType((String) attributes.get(24));
                car.setReleaseDate((Date) attributes.get(25));
                car.setSeatsCount((int) attributes.get(26));
                car.setChildSeat((boolean) attributes.get(27));
                car.setDriverId((int) attributes.get(28));
                car.setStateNumber((int) attributes.get(29));
                return car;
            case 5:
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderId((int) attributes.get(30));
                orderStatus.setStatus((String) attributes.get(31));
                orderStatus.setTimeStamp((Time) attributes.get(32));
                return orderStatus;
            case 6:
                DriverStatus driverStatus = new DriverStatus();
                driverStatus.setDriverId((int) attributes.get(33));
                driverStatus.setStatus((String) attributes.get(34));
                driverStatus.setTimeStamp((Time) attributes.get(35));
                return driverStatus;
            case 7:
                Route route = new Route(entity.getObjectId());
                route.setOrderId((int) attributes.get(36));
                route.setCheckPoint((String) attributes.get(37));
                route.setShowOrder((String) attributes.get(38));
                return route;
            default:
                return null;
        }
    }
}

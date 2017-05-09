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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            if (field.isAccessible()) {
                fieldValue = field.get(model);
            } else {
                field.setAccessible(true);
                fieldValue = field.get(model);
                field.setAccessible(false);
            }

            if (field.isAnnotationPresent(Attribute.class)) {
                attributeAnnotation = field.getAnnotation(Attribute.class);
                attributes.put(attributeAnnotation.attrId(), fieldValue);
            }
            if (field.isAnnotationPresent(Reference.class)) {
                referenceAnnotation = field.getAnnotation(Reference.class);
                references.put(referenceAnnotation.attrId(), (long) fieldValue);
            }
        }

        entity.setName(model.getName());
        entity.setObjectId(model.getObjectId());
        entity.setDescription(model.getDescription());
        entity.setParentId(model.getParentId());

        entity.setAttributes(attributes);
        entity.setReferences(references);

        return entity;
    }

    @Override
    public <T extends Model> T convertToModel(PersistenceEntity entity, Class clazz) throws IllegalAccessException, InstantiationException {
        Class modelClass = clazz;
        if (clazz == Model.class) return null;
        T model = (T) modelClass.newInstance();

        model.setName(entity.getName());
        model.setObjectId(entity.getObjectId());
        model.setDescription(entity.getDescription());
        model.setParentId(entity.getParentId());

        Map<Long, Object> attributes = entity.getAttributes();
        Map <Long,Long> references = entity.getReferences();
        Field[] fields = modelClass.getDeclaredFields();
        Attribute attributeAnnotation;
        Reference referenceAnnotation;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                attributeAnnotation = field.getAnnotation(Attribute.class);
                Object fieldValue = attributes.get(attributeAnnotation.attrId());
                setFieldsInModel(fieldValue, field, modelClass, model);
            }

            if (field.isAnnotationPresent(Reference.class)) {
                referenceAnnotation = field.getAnnotation(Reference.class);
                Object fieldValue = references.get(referenceAnnotation.attrId());
                setFieldsInModel(fieldValue, field, modelClass, model);
            }
        }
        return model;
    }

    private void setFieldsInModel(Object fieldValue, Field field, Class modelClass, Model model) {
        if (fieldValue != null) {
            String fieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

            try {
                Method method = modelClass.getDeclaredMethod("set" + fieldName, field.getType());
                System.out.println(method);
                method.invoke(model, fieldValue);
            } catch (NoSuchMethodException imp) {
                imp.printStackTrace();
            } catch (InvocationTargetException imp) {
                imp.printStackTrace();
            } catch (IllegalAccessException imp) {
                imp.printStackTrace();
            }
        }
    }
}

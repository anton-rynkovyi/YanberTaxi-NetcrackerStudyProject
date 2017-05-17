package com.netcracker.project.study.persistence.converter.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;
import com.netcracker.project.study.model.annotations.Exception.NoSuchAnnotationException;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.PersistenceEntity;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Component
public class ConverterFactory implements Converter {

    private Map<BigInteger, Object> attributes;
    private Map<BigInteger, BigInteger> references;

    private final Timestamp EMPTY_LONG = new Timestamp(-1);
    private final String EMPTY_STRING = "-1";
    private final BigInteger EMPTY_BIG_INTEGER = BigInteger.valueOf(-1);

    public ConverterFactory() {
        attributes = new HashMap<>();
        references = new HashMap<>();
    }

    /**
     * Puts field with Attribute annotation in attributes map
     *
     * @param attrId - attribute id of corresponding field
     * @param model  - a model which current field belongs to
     * @param field  - field to put
     */
    private void putAttribute(BigInteger attrId, Model model, Field field) {
        Object fieldValue;
        fieldValue = getValue(model, field);
        if (field.isAnnotationPresent(AttrValue.class)) {
            attributes.put(attrId, fieldValue != null ? fieldValue.toString() : EMPTY_STRING);
        } else if (field.isAnnotationPresent(AttrDate.class)) {
            attributes.put(attrId, fieldValue != null ? Timestamp.valueOf(fieldValue.toString()) : EMPTY_LONG);
        } else if (field.isAnnotationPresent(AttrList.class)) {
            attributes.put(attrId, fieldValue != null ? fieldValue : EMPTY_BIG_INTEGER);
        }
    }

    /**
     * Puts field with Reference annotation in reference map
     *
     * @param attrId - attribute id of corresponding field
     * @param model  - model which current field belongs to
     * @param field  - field to put
     */
    private void putReference(BigInteger attrId, Model model, Field field) {
        Object fieldValue;
        fieldValue = getValue(model, field);
        if (fieldValue != null) {
            references.put(attrId, (BigInteger) fieldValue);
        }
    }

    /**
     * Gets value of field
     *
     * @param model - a model which current field belongs to
     * @param field - a field which it gets value of
     * @return value of corresponding field with Object type
     */
    private Object getValue(Model model, Field field) {
        Object value;
        PropertyDescriptor property;
        try {
            property = new PropertyDescriptor(field.getName(), model.getClass());
            Method getMethod = property.getReadMethod();
            value = getMethod.invoke(model);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Unable to find the field");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The field doesn't have public get method.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * @param field - a field which must be set with a value
     * @param model - a model which current field belongs to
     * @param value - value of corresponding field with Object type
     */
    private void setValue(Field field, Model model, Object value) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), model.getClass());
            Method setMethod = propertyDescriptor.getWriteMethod();
            if (setMethod != null) {
                setMethod.invoke(model, value);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException("Unable to find the field");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The field doesn't have public set method.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PersistenceEntity convertToEntity(Model model) throws IllegalAccessException, NoSuchFieldException {
        Class modelClass = model.getClass();
        PersistenceEntity entity = new PersistenceEntity();
        attributes = new HashMap<>();
        references = new HashMap<>();

        if (modelClass.isAnnotationPresent(ObjectType.class)) {
            ObjectType objectType = (ObjectType) modelClass.getAnnotation(ObjectType.class);
            BigInteger objectTypeId = BigInteger.valueOf(objectType.objectTypeId());
            entity.setObjectTypeId(objectTypeId);
        } else {
            throw new NoSuchAnnotationException("Model doesn't have " + ObjectType.class.getSimpleName() + " annotation");
        }

        Field[] fields = modelClass.getDeclaredFields();
        Attribute attributeAnnotation;
        Reference referenceAnnotation;
        BigInteger attrId;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                attributeAnnotation = field.getAnnotation(Attribute.class);
                attrId = BigInteger.valueOf(attributeAnnotation.attrId());
                putAttribute(attrId, model, field);
            }

            if (field.isAnnotationPresent(Reference.class)) {
                referenceAnnotation = field.getAnnotation(Reference.class);
                attrId = BigInteger.valueOf(referenceAnnotation.attrId());
                putReference(attrId, model, field);
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

        Map<BigInteger, Object> attrMap = entity.getAttributes();
        Map<BigInteger, BigInteger> refMap = entity.getReferences();
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                Object fieldValue = attrMap.get(BigInteger.valueOf(attributeAnnotation.attrId()));
                setValue(field, model, fieldValue);
            }

            if (field.isAnnotationPresent(Reference.class)) {
                Reference referenceAnnotation = field.getAnnotation(Reference.class);
                Object fieldValue = refMap.get(BigInteger.valueOf(referenceAnnotation.attrId()));
                setValue(field, model, fieldValue);
            }
        }
        return model;
    }

}

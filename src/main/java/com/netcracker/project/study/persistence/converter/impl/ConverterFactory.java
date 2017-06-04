package com.netcracker.project.study.persistence.converter.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;
import com.netcracker.project.study.model.annotations.Exception.NoSuchAnnotationException;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Component
public class ConverterFactory implements Converter {

    @Autowired
    PersistenceFacade persistenceFacade;

    private final Date EMPTY_LONG = new Date(-1);
    private final String EMPTY_STRING = "-1";
    private final BigInteger EMPTY_BIG_INTEGER = BigInteger.valueOf(-1);

    /**
     * Puts field with Attribute annotation in attributes map
     *
     * @param attrId - attribute id of corresponding field
     * @param model  - a model which current field belongs to
     * @param field  - field to put
     */
    private void putAttribute(Map<BigInteger, Object> attributes, BigInteger attrId, Model model, Field field) {
        Object fieldValue = getValue(model, field);
        if (field.isAnnotationPresent(AttrValue.class)) {
            attributes.put(attrId, fieldValue != null ? fieldValue.toString() : EMPTY_STRING);
        } else if (field.isAnnotationPresent(AttrDate.class)) {
            attributes.put(attrId, fieldValue != null ? ((Date) fieldValue) : EMPTY_LONG);
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
    private void putReference(Map<BigInteger, BigInteger> references, BigInteger attrId, Model model, Field field) {
        Object fieldValue;
        fieldValue = getValue(model, field);
        if (fieldValue != null) {
            if (!(Model.class.isAssignableFrom(field.getType()))) {
                references.put(attrId, (BigInteger) fieldValue);
            }
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

        Class fieldType = field.getType();
        if (field.isAnnotationPresent(AttrValue.class)) {
            if (BigInteger.class.isAssignableFrom(fieldType)) {
                value = value != null ? BigInteger.valueOf(Long.parseLong(String.valueOf(value))) : null;
            } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
                value = value != null ? BigDecimal.valueOf(Double.parseDouble(String.valueOf(value))) : null;
            } else if (boolean.class.isAssignableFrom(fieldType)) {
                value = value != null ? Boolean.valueOf(String.valueOf(value)) : Boolean.valueOf(false);
            }
        } else if (field.isAnnotationPresent(AttrDate.class)) {
            java.util.Date date = value != null ?
                    new Date(Timestamp.valueOf(String.valueOf(value)).getTime()) : null;
            value = value != null ? date : null;
        } else if (field.isAnnotationPresent(AttrList.class)) {
            value = value != null&&!value.equals("null") ? BigInteger.valueOf(Long.parseLong(String.valueOf(value))) : null;
        } else if (Model.class.isAssignableFrom(fieldType)) {
            value = value != null ? persistenceFacade.getOne(BigInteger.valueOf(Long.parseLong(String.valueOf(value))), fieldType) : null;
        }

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
    public PersistenceEntity convertToEntity(Model model) {
        Class modelClass = model.getClass();
        PersistenceEntity entity = new PersistenceEntity();
        Map<BigInteger, Object> attributes = new HashMap<>();
        Map<BigInteger, BigInteger> references = new HashMap<>();

        if (modelClass.isAnnotationPresent(ObjectType.class)) {
            ObjectType objectType = (ObjectType) modelClass.getAnnotation(ObjectType.class);
            BigInteger objectTypeId = BigInteger.valueOf(objectType.objectTypeId());
            entity.setObjectTypeId(objectTypeId);
        } else {
            throw new NoSuchAnnotationException("Model doesn't have " + ObjectType.class.getSimpleName() + " annotation");
        }

        Field[] fields = modelClass.getDeclaredFields();
        BigInteger attrId;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                attrId = BigInteger.valueOf(attributeAnnotation.attrId());
                putAttribute(attributes, attrId, model, field);
            }

            if (field.isAnnotationPresent(Reference.class)) {
                Reference referenceAnnotation = field.getAnnotation(Reference.class);
                attrId = BigInteger.valueOf(referenceAnnotation.attrId());
                putReference(references, attrId, model, field);
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
    public <T extends Model> T convertToModel(PersistenceEntity entity, Class clazz) {
        Class modelClass = clazz;
        if (clazz == Model.class) throw new IllegalArgumentException("You can't create object with Model type");
        T model = null;
        try {
            model = (T) modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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

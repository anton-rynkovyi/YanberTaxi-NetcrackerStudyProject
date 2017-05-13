package com.netcracker.project.study.persistence;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PersistenceEntity {

    private BigInteger objectId;
    private BigInteger objectTypeId;
    private BigInteger parentId;

    private String name;
    private String description;

    private Map<BigInteger, Object> attributes;

    private Map<BigInteger, BigInteger> references;

    public PersistenceEntity(){
        attributes = new HashMap<>();
        references = new HashMap<>();
    }

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public BigInteger getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(BigInteger objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<BigInteger, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<BigInteger, Object> entity) {
        this.attributes = entity;
    }

    public Map<BigInteger, BigInteger> getReferences() {
        return references;
    }

    public void setReferences(Map<BigInteger, BigInteger> references) {
        this.references = references;
    }


    @Override
    public String toString() {
        return "PersistenceEntity{" +
                "objectId=" + objectId +
                ", objectTypeId=" + objectTypeId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", attributes=" + attributes +
                ", references=" + references +
                '}';
    }
}

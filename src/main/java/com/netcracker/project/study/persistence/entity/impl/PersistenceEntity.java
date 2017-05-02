package com.netcracker.project.study.persistence.entity.impl;

import java.util.HashMap;
import java.util.Map;

public class PersistenceEntity {

    private long objectId;
    private int objectTypeId;
    private int parentId;

    private Map<Integer, Object> attributes;

    public PersistenceEntity(){
        attributes = new HashMap<>();
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public int getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(int objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, Object> entity) {
        this.attributes = entity;
    }

}

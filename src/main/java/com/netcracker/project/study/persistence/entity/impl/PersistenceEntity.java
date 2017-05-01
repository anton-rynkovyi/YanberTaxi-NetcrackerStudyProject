package com.netcracker.project.study.persistence.entity.impl;

import java.util.HashMap;
import java.util.Map;

public class PersistenceEntity {

    private int objectId;
    private int objectTypeId;
    private int parentId;

    private Map<Integer, Object> entity;

    public PersistenceEntity(){entity = new HashMap<>();}

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
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

    public Map<Integer, Object> getEntity() {
        return entity;
    }

    public void setEntity(Map<Integer, Object> entity) {
        this.entity = entity;
    }
}

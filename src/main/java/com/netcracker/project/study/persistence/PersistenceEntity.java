package com.netcracker.project.study.persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceEntity {

    private long objectId;
    private long objectTypeId;
    private long parentId;

    private Map<Integer, Object> attributes;

    private Map<Long, Long> references;

    public PersistenceEntity(){
        attributes = new HashMap<>();
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, Object> entity) {
        this.attributes = entity;
    }

    public Map<Long, Long> getReferences() {
        return references;
    }

    public void setReferences(Map<Long, Long> references) {
        this.references = references;
    }
}

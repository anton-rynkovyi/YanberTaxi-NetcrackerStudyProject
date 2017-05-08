package com.netcracker.project.study.persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceEntity {

    private long objectId;
    private long objectTypeId;
    private long parentId;

    private String name;
    private String description;

    private Map<Long, Object> attributes;

    private Map<Long, Long> references;

    public PersistenceEntity(){
        attributes = new HashMap<>();
        references = new HashMap<>();
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

    public Map<Long, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Long, Object> entity) {
        this.attributes = entity;
    }

    public Map<Long, Long> getReferences() {
        return references;
    }

    public void setReferences(Map<Long, Long> references) {
        this.references = references;
    }
}

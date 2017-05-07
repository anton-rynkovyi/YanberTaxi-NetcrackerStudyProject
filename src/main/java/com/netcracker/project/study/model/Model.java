package com.netcracker.project.study.model;

import com.netcracker.project.study.model.annotations.Attribute;

public abstract class Model {

    @Attribute(attrId = -1)
    private String name;

    @Attribute(attrId = -2)
    private String description;

    @Attribute(attrId = -3)
    private long objectId;

    public Model() {}

    public Model(long objectId) {
        this.objectId = objectId;
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

    public long getObjectId() {
        return objectId;
    }

}

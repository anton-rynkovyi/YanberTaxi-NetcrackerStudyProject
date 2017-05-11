package com.netcracker.project.study.model;

import com.netcracker.project.study.model.annotations.Attribute;

public abstract class Model implements ObjectFakeAttr{

    @Attribute(attrId = OBJECT_NAME_ATTR)
    private String name;

    @Attribute(attrId = OBJECT_DESC_ATTR)
    private String description;

    @Attribute(attrId = OBJECT_ID_ATTR)
    private long objectId;

    @Attribute(attrId = PARENT_ID_ATTR)
    private long parentId;


    public Model() {}

    public Model(String name) {this.name = name; }

    public Model(String name, String description) {
        this.name = name;
        this.description = description;
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

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}

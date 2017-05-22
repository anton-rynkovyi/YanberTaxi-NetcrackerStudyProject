package com.netcracker.project.study.model;

import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.driver.Driver;

import java.math.BigInteger;

public abstract class Model implements ObjectFakeAttr{

    @Attribute(attrId = OBJECT_NAME_ATTR)
    private String name;

    @Attribute(attrId = OBJECT_DESC_ATTR)
    private String description;

    @Attribute(attrId = OBJECT_ID_ATTR)
    private BigInteger objectId;

    @Attribute(attrId = PARENT_ID_ATTR)
    private BigInteger parentId;


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

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

}

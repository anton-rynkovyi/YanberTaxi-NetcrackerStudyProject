package com.netcracker.project.study.model.user;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.AttrList;
import com.netcracker.project.study.model.annotations.AttrValue;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

import java.math.BigInteger;

public class User extends Model {

    @Attribute(attrId = LOGIN)
    private String login;

    @Attribute(attrId = OBJECT_ID_USER)
    private  BigInteger objectId;

    @Attribute(attrId = PASSWORD)
    private  String password;

    @Attribute(attrId = ROLE)
    private  BigInteger role;

    public User() {}

    public User(String name) {
        super(name);
    }

    public User(String name,String description) {
        super(name,description);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigInteger getRole() {
        return role;
    }

    public void setRole(BigInteger role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "objectId=" + objectId +
                ", login=" + login +
                ", password=" + password +
                ", role='" + role + '\'' +
                '}';
    }

}

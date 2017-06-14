package com.netcracker.project.study.model.user;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.AttrList;
import com.netcracker.project.study.model.annotations.AttrValue;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

import java.math.BigInteger;

@ObjectType(objectTypeId = UserAttr.OBJECT_TYPE_ID)
public class User extends Model implements UserAttr {

    @Attribute(attrId = UserAttr.LOGIN_ATTR)
    private @AttrValue String login;

    @Attribute(attrId = UserAttr.OBJECT_ID_ATTR)
    private @AttrValue BigInteger objectId;

    @Attribute(attrId = UserAttr.PASSWORD_ATTR)
    private @AttrValue String password;

    @Attribute(attrId = UserAttr.ROLE_ATTR)
    private @AttrList BigInteger role;

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

package com.netcracker.project.study.model.admin;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.annotations.AttrValue;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@ObjectType(objectTypeId = AdminAttr.OBJECT_TYPE_ID)
public class Admin extends Model implements AdminAttr {

    @Attribute(attrId = AdminAttr.PHONE_NUMBER_ATTR)
    private @AttrValue String phoneNumber;

    @Attribute(attrId = AdminAttr.EMAIL_ATTR)
    private @AttrValue String email;

    public Admin(){}

    public Admin(String name) {
        super(name);
    }

    public Admin(String name, String description) {
        super(name, description);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

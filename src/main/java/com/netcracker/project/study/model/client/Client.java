package com.netcracker.project.study.model.client;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.annotations.AttrValue;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigInteger;
import java.util.List;

@ObjectType(objectTypeId = ClientAttr.OBJECT_TYPE_ID)
public class Client extends Model implements ClientAttr, UserDetails {

    @Attribute(attrId = ClientAttr.LAST_NAME_ATTR)
    private @AttrValue String lastName;

    @Attribute(attrId = ClientAttr.FIRST_NAME_ATTR)
    private @AttrValue String firstName;

    @Attribute(attrId = ClientAttr.MIDDLE_NAME_ATTR)
    private @AttrValue String middleName;

    @Attribute(attrId = ClientAttr.PHONE_NUMBER_ATTR)
    private @AttrValue String phoneNumber;

    @Attribute(attrId = ClientAttr.POINTS_ATTR)
    private @AttrValue BigInteger points;

    private List<Role> authorities;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public Client() {}

    public Client(String name) {
        super(name);
    }

    public Client(String name, String description) {
        super(name, description);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public BigInteger getPoints() {
        return points;
    }

    public void setPoints(BigInteger points) {
        this.points = points;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.phoneNumber = username;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "Client{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", points=" + points +
                '}';
    }
}

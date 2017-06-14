package com.netcracker.project.study.model.driver;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.annotations.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


@ObjectType(objectTypeId = DriverAttr.OBJECT_TYPE_ID)
public class Driver extends Model implements DriverAttr, UserDetails{

    @Attribute(attrId = DriverAttr.LAST_NAME_ATTR)
    private @AttrValue String lastName;

    @Attribute(attrId = DriverAttr.FIRST_NAME_ATTR)
    private @AttrValue String firstName;

    @Attribute(attrId = DriverAttr.MIDDLE_NAME_ATTR)
    private @AttrValue String middleName;

    @Attribute(attrId = DriverAttr.PHONE_NUMBER_ATTR)
    private @AttrValue String phoneNumber;

    @Attribute(attrId = DriverAttr.RATING_ATTR)
    private @AttrValue BigDecimal rating;

    @Attribute(attrId = DriverAttr.EXPERIENCE_ATTR)
    private @AttrValue BigInteger experience;

    @Attribute(attrId = DriverAttr.EMAIL_ATTR)
    private @AttrValue String email;

    @Attribute(attrId = DriverAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = DriverAttr.HIRE_DATE_ATTR)
    private @AttrDate Date hireDate;

    @Attribute(attrId = DriverAttr.UNBAN_DATE_ATTR)
    private @AttrDate Date unbanDate;


    private List<Role> authorities;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;


    public Driver() {}

    public Driver(String name) {
        super(name);
    }

    public Driver(String name, String description) {
        super(name, description);
    }

    public Driver(String username, String password,
                  boolean accountNonExpired, boolean accountNonLocked,
                  boolean credentialsNonExpired, boolean enabled, List authorities) {
        this.email = username;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.authorities = authorities;
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public BigInteger getExperience() {
        return experience;
    }

    public void setExperience(BigInteger experience) {
        this.experience = experience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getUnbanDate() {
        return unbanDate;
    }

    public void setUnbanDate(Date unbanDate) {
        this.unbanDate = unbanDate;
    }


    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.email = username;
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
        return email;
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
        return "Driver{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", rating=" + rating +
                ", experience=" + experience +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", hireDate=" + hireDate +
                ", unbanDate=" + unbanDate +
                '}';
    }
}

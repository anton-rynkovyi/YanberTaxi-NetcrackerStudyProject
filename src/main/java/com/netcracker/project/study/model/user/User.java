package com.netcracker.project.study.model.user;



import java.math.BigInteger;

public class User  {

    private BigInteger userId;

    private String login;

    private  BigInteger objectId;


    private  String password;


    private  String role;

    public User() {}


    public User(String login,String password) {
        this.login = login;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
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

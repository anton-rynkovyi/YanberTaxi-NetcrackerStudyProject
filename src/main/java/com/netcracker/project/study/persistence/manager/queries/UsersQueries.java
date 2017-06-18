package com.netcracker.project.study.persistence.manager.queries;


public class UsersQueries {

    public static final String CREATE_USERS = ""+
            "INSERT INTO USERS" +
            "(object_id, login, password, role) " +
            "VALUES(?, ?, ?, ?)";

    public static final String SELECT_USERS = ""+
            "SELECT * " +
            "FROM USERS " +
            "WHERE login = ? and password = ?";

}

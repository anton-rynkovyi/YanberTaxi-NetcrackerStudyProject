package com.netcracker.project.study.persistence.manager.queries;


public class UsersQueries {

    public static final String CREATE_USERS = ""+
            "INSERT INTO USERS" +
            "(object_id, login, password, role, enabled) " +
            "VALUES(?, ?, ?, ?, ?)";

    public static final String DELETE_USERS = ""+
            "DELETE FROM USERS " +
            "WHERE object_id = ?";

    public static final String UPDATE_USERS = ""+
            "MERGE INTO USERS " +
            "USING DUAL ON (object_id=?) "+
            "WHEN NOT MATCHED THEN " +
            "INSERT (object_id, login, password, role, enabled) " +
            "VALUES (?,?,?,?,?) " +
            "WHEN MATCHED THEN " +
            "UPDATE SET login=?, password=?, role=?, enabled=?";

    public static final String SELECT_USERS = ""+
            "SELECT * " +
            "FROM USERS " +
            "WHERE login = ? and password = ?";

}

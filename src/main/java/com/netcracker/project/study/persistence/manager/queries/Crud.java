package com.netcracker.project.study.persistence.manager.queries;

public class Crud {

    public static final String CREATE_OBJECTS = ""+
            "INSERT INTO OBJECTS" +
            "(parent_id, object_type_id, name, description)" +
            " VALUES(?,?,?,?)";
    public static final String CREATE_ATTRIBUTES = ""+
            "INSERT INTO ATTRIBUTES" +
            "(value, date_value, list_value_id, object_id, attr_id)" +
            " VALUES(?,?,?,?,?)";
    public static final String CREATE_OBJREFERENCE = ""+
            "INSERT INTO OBJREFERENCE" +
            "(REFERENCE, OBJECT_ID, ATTR_ID)" +
            " VALUES(?,?,?)";
    public static final String UPDATE_OBJECTS = ""+
            "UPDATE OBJECTS " +
            "SET parent_id=?, object_type_id=?, name=?, description=? " +
            "WHERE object_id=?";
    public static final String UPDATE_OBJREFERENCE = ""+
            "UPDATE OBJREFERENCE " +
            "SET  REFERENCE=?" +
            "WHERE object_id=?" +
            "AND attr_id =?";
    public static final String UPDATE_ATTRIBUTES = ""+
            "UPDATE ATTRIBUTES " +
            "SET value=?, date_value=?, list_value_id=? " +
            "WHERE object_id=? " +
            "AND attr_id=?";
    public static final String DELETE_FROM_ATTRIBUTES = ""+
            "DELETE " +
            "FROM ATTRIBUTES " +
            "WHERE object_id=?";
    public static final String DELETE_FROM_OBJREFERENCE = ""+
            "DELETE " +
            "FROM OBJREFERENCE " +
            "WHERE object_id=?";
    public static final String DELETE_FROM_OBJECTS = ""+
            "DELETE " +
            "FROM OBJECTS " +
            "WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS_BY_ID = ""+
            "SELECT * " +
            "FROM OBJECTS " +
            "WHERE object_id=?";
    public static final String SELECT_FROM_ATTRIBUTES_BY_ID = ""+
            "SELECT attr.value, attr.date_value, lists.list_value_id as list_value_id, attr.attr_id " +
            "from  attributes attr " +
            "LEFT JOIN  lists " +
            "ON attr.List_value_id = LISTS.list_value_id " +
            "WHERE attr.OBJECT_ID = ?";
    public static final String SELECT_FROM_OBJREFERENCE = ""+
            "SELECT * " +
            "FROM OBJREFERENCE " +
            "WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS = ""+
            "SELECT * " +
            "FROM Objects " +
            "WHERE object_type_id = ?";
    public static final String GENERATE_MAX_OBJECT_ID = ""+
            "SELECT " +
            "NVL(MAX(object_id),0)+1 AS object_id " +
            "FROM objects";

}

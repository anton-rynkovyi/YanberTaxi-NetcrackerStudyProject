package com.netcracker.project.study.persistence.manager.impl;

import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.sql.Types.*;

@Component("persistenceManager")
public class PersistenceManager implements Manager {

    public static final String CREATE_OBJECTS = ""+
            "INSERT INTO OBJECTS" +
            "(parent_id, object_type_id, name, description,object_id)" +
            " VALUES(?,?,?,?,?)";
    public static final String CREATE_ATTRIBUTES = ""+
            "INSERT INTO ATTRIBUTES" +
            "(attr_id, value, date_value, list_value_id,object_id)" +
            " VALUES(?,?,?,?,?)";
    public static final String CREATE_OBJREFERENCE = ""+
            "INSERT INTO OBJREFERENCE" +
            "(ATTR_ID, REFERENCE, OBJECT_ID)" +
            " VALUES(?,?,?)";
    public static final String UPDATE_OBJECTS = ""+
            "UPDATE OBJECTS " +
            "SET parent_id=?, object_type_id=?, name=?, description=? " +
            "WHERE object_id=?";
    public static final String UPDATE_OBJREFERENCE = ""+
            "UPDATE OBJREFERENCE " +
            "SET ATTR_ID=? REFERENCE=? OBJECT_ID=? " +
            "WHERE object_id=? ";
    public static final String UPDATE_ATTRIBUTES = ""+
            "UPDATE ATTRIBUTES " +
            "SET attr_id=? value=? date_value=? list_value_id=? " +
            "WHERE object_id=? ";
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
            "WHERE object_id=? ";
    public static final String SELECT_FROM_ATTRIBUTES_BY_ID = ""+
            "SELECT * " +
            "FROM Attributes " +
            "WHERE object_id=? ";
    public static final String SELECT_FROM_OBJECTS = ""+
            "SELECT * " +
            "FROM Objects";
    public static final String GENERATE_MAX_OBJECT_ID = ""+
            "select " +
            "nvl(MAX(object_id),0)+1 as object_id " +
            "from objects";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedTemplate;

    //private InitialContext ctx;

    @Autowired
    public PersistenceManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        persistenceEntity = jdbcTemplate.queryForObject(GENERATE_MAX_OBJECT_ID, rowMapperObjectId);
        jdbcTemplate.update(CREATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
       for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(CREATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            jdbcTemplate.update(CREATE_OBJREFERENCE, getPreparedStatementSetterRef(entry,persistenceEntity));
        }
        return persistenceEntity;
    }

    @Override
    public void update(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update(UPDATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(UPDATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(UPDATE_OBJREFERENCE, getPreparedStatementSetterRef(entry,persistenceEntity));
        }
    }

    @Override
    public void delete(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update(DELETE_FROM_OBJECTS, persistenceEntity.getObjectId());
        jdbcTemplate.update(DELETE_FROM_ATTRIBUTES, persistenceEntity.getObjectId());
        jdbcTemplate.update(DELETE_FROM_OBJREFERENCE, persistenceEntity.getObjectId());
    }

    private RowMapper<PersistenceEntity> rowMapper = new RowMapper<PersistenceEntity>() {
        @Override
        public PersistenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(rs.getLong("object_id"));
            persistenceEntity.setObjectTypeId(rs.getInt("object_type_id"));
            persistenceEntity.setParentId(rs.getInt("parent_id"));
            return persistenceEntity;
        }
    };

    private RowMapper<PersistenceEntity> rowMapperObjectId = new RowMapper<PersistenceEntity>() {
        @Override
        public PersistenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(rs.getLong("object_id"));
            return persistenceEntity;
        }
    };
    @Override
    public PersistenceEntity getOne(long objectId) {
        PersistenceEntity persistenceEntity = jdbcTemplate.queryForObject(SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID,objectId);
        Map<Long, Object> attributes = getAttributes(rows);
        persistenceEntity.setAttributes(attributes);
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(int objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_OBJECTS);

        //check empty
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }

        List<PersistenceEntity> persistenceEntityList = new ArrayList<PersistenceEntity>();
        for (Map row : rows) {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(Long.parseLong(String.valueOf(row.get("object_id"))));
            persistenceEntity.setObjectTypeId(Long.parseLong(String.valueOf(row.get("object_type_id"))));
            if(String.valueOf(row.get("parent_id"))=="null") {
                persistenceEntity.setParentId(0);
            } else {
                persistenceEntity.setParentId(Long.parseLong(String.valueOf(row.get("parent_id"))));
            }
            List<Map<String, Object>> rowss = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID,persistenceEntity.getObjectId());
            Map<Long, Object> attributes = getAttributes(rowss);
            persistenceEntity.setAttributes(attributes);
            persistenceEntityList.add(persistenceEntity);

        }
        return persistenceEntityList;
    }

    private Map<Long, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Long, Object> attributes = null;
        for (Map row : rowss) {
            long attrId = (Long)row.get("attr_id");
            Object value = null;
            if ((attrId >= 1 && attrId<=5) || (attrId>=7 && attrId<=9) || (attrId>=11 && attrId<=17) || (attrId>=19 && attrId<=24)){
                value = row.get("value");
            }
            if (attrId== 6 || attrId==10 || attrId==25) {
                value = row.get("date_value");
            }
            if (attrId== 18 || attrId==31 || attrId==34) {
                value = row.get("list_value_id");
            }
            attributes.put(attrId,value) ;
        }
        return attributes;
    }

    private PreparedStatementSetter getPreparedStatementSetterObjects(final PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (persistenceEntity.getParentId() ==0) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setLong(++i, persistenceEntity.getParentId());
                }
                ps.setLong(++i, persistenceEntity.getObjectTypeId());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
                ps.setLong(++i, persistenceEntity.getObjectId());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry,PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                int attrId = (Integer)entry.getKey();
                ps.setInt(++i,attrId);
                if ((attrId>=1 && attrId<=5) || (attrId>=7 && attrId<=9) || (attrId>=11 && attrId<=17) || (attrId>=19 && attrId<=24)){
                    ps.setString(++i, String.valueOf(entry.getValue()));
                    ps.setNull(++i, DATE);
                    ps.setNull(++i, NUMERIC);
                }
                if (attrId== 6 || attrId==10 || attrId==25) {
                    ps.setString(++i, null);
                    ps.setDate(++i, Date.valueOf(String.valueOf(entry.getValue())));
                    ps.setNull(++i, NUMERIC);
                }
                if (attrId== 18 || attrId==31 || attrId==34) {
                    ps.setString(++i, null);
                    ps.setNull(++i, DATE);
                    ps.setInt(++i, Integer.getInteger(String.valueOf(entry.getValue())));
                }
                ps.setLong(++i, persistenceEntity.getObjectId());

            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterRef(Map.Entry entry,PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setInt(++i,(Integer)entry.getKey());
                ps.setLong(++i, Long.parseLong(String.valueOf(entry.getValue())));
                ps.setLong(++i, persistenceEntity.getObjectId());

            }
        };
    }

}

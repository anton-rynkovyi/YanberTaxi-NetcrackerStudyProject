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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("persistenceManager")
public class PersistenceManager implements Manager {

    public static final String CREATE_OBJECTS = "INSERT INTO OBJECTS(parent_id, object_type_id, name, description,object_id) VALUES(?,?,?,?,?)";
    public static final String CREATE_ATTRIBUTES = "INSERT INTO ATTRIBUTES(attr_id, value, date_value, list_value_id,object_id) VALUES(?,?,?,?,?)";
    public static final String UPDATE_OBJECTS = "UPDATE OBJECTS SET parent_id=?, object_type_id=?, name=?, description=? WHERE object_id=?";
    public static final String UPDATE_ATTRIBUTES = "UPDATE ATTRIBUTES SET attr_id=? value=? date_value=? list_value_id=? WHERE object_id=? ";
    public static final String DELETE_FROM_OBJECTS = "DELETE FROM OBJECTS WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS_BY_ID = "SELECT * FROM OBJECTS WHERE object_id=? ";
    public static final String SELECT_FROM_ATTRIBUTES_BY_ID = "SELECT * FROM Attributes WHERE object_id=? ";
    public static final String SELECT_FROM_OBJECTS = "SELECT * FROM Objects";

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
        String sql = CREATE_OBJECTS;
        jdbcTemplate.update(sql, getPreparedStatementSetterObjects(persistenceEntity));
       /*for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            String sqlAttr = CREATE_ATTRIBUTES;
            jdbcTemplate.update(sqlAttr, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }*/
        return persistenceEntity;
    }

    @Override
    public void update(PersistenceEntity persistenceEntity) {
        String sql = UPDATE_OBJECTS;
        jdbcTemplate.update(sql, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            String sqlAttr = "";
            sqlAttr = UPDATE_ATTRIBUTES;
            jdbcTemplate.update(sqlAttr, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
    }

    @Override
    public void delete(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update(DELETE_FROM_OBJECTS, persistenceEntity.getObjectId());
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

    @Override
    public PersistenceEntity getOne(long objectId) {
        PersistenceEntity persistenceEntity;
        persistenceEntity = jdbcTemplate.queryForObject(SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
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

        List<PersistenceEntity> persistenceEntityList = null;
        PersistenceEntity persistenceEntity = null;
        for (Map row : rows) {
            persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId((Long)row.get("object_id"));
            persistenceEntity.setObjectTypeId((Integer) row.get("object_type_id"));
            persistenceEntity.setParentId((Integer)row.get("parent_id"));
            persistenceEntityList.add(persistenceEntity);

        }
        for (int i=0; i<persistenceEntityList.size(); i++){
            PersistenceEntity pe = null;
            pe = persistenceEntityList.get(i);
            long id = pe.getObjectId();
            List<Map<String, Object>> rowss = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID,id);
            Map<Long, Object> attributes = getAttributes(rowss);
            pe.setAttributes(attributes);
            persistenceEntityList.set(i,pe);
        }

        return persistenceEntityList;
    }

    private Map<Long, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Long, Object> attributes = null;
        for (Map r : rowss) {
            long k = (Long)r.get("attr_id");
            Object value = null;
            if ((k >= 1 && k<=5) || (k>=7 && k<=9) || (k>=11 && k<=17) || (k>=19 && k<=24)){
                value = r.get("value");
            }
            if (k== 6 || k==10 || k==25) {
                value = r.get("date_value");
            }
            if (k== 18 || k==31 || k==34) {
                value = r.get("list_value_id");
            }
            attributes.put(k,value) ;
        }
        return attributes;
    }
    private PreparedStatementSetter getPreparedStatementSetterObjects(final PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (persistenceEntity.getParentId() ==0) {
                    ps.setNull(++i, Types.NUMERIC);
                } else {
                    ps.setLong(++i, persistenceEntity.getParentId());
                }
                ps.setLong(++i, persistenceEntity.getObjectTypeId());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
                if (persistenceEntity.getObjectId() ==0) {
                    ps.setNull(++i, Types.NUMERIC);
                } else {
                    ps.setLong(++i, persistenceEntity.getObjectId());
                }




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
                    ps.setDate(++i, null);
                    ps.setNull(++i, Types.NUMERIC);
                }
                if (attrId== 6 || attrId==10 || attrId==25) {
                    ps.setString(++i, null);
                    ps.setDate(++i, Date.valueOf(String.valueOf(entry.getValue())));
                    ps.setNull(++i, Types.NUMERIC);
                }
                if (attrId== 18 || attrId==31 || attrId==34) {
                    ps.setString(++i, null);
                    ps.setDate(++i, null);
                    ps.setInt(++i, Integer.getInteger(String.valueOf(entry.getValue())));
                }
                ps.setLong(++i, persistenceEntity.getObjectId());

            }
        };
    }

}

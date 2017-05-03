package com.netcracker.project.study.persistence.manager.impl;

import com.netcracker.project.study.persistence.entity.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class PersistenceManager implements Manager {

    private static final String CREATE_OBJECTS = "INSERT INTO OBJECTS(parent_id, object_type_id, name, description,object_id) VALUES(?,?,?,?,?)";
    private static final String CREATE_ATTRIBUTES = "INSERT INTO ATTRIBUTES(attr_id, value, date_value, list_value_id,object_id) VALUES(?,?,?,?,?)";
    public static final String UPDATE_OBJECTS = "UPDATE OBJECTS SET parent_id=?, object_type_id=?, name=?, description=? WHERE object_id=?";
    public static final String UPDATE_ATTRIBUTES = "UPDATE ATTRIBUTES SET attr_id=? value=? date_value=? list_value_id=? WHERE object_id=? ";
    public static final String DELETE_FROM_OBJECTS = "DELETE FROM OBJECTS WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS_BY_ID = "SELECT * FROM OBJECTS WHERE object_id=? ";
    public static final String SELECT_FROM_ATTRIBUTES_BY_ID = "SELECT * FROM Attributes WHERE object_id=? ";
    public static final String SELECT_FROM_OBJECTS = "SELECT * FROM Objects";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    public PersistenceManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        String sql = CREATE_OBJECTS;
        jdbcTemplate.update(sql, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            String sqlAttr = "";
            sqlAttr = CREATE_ATTRIBUTES;
            jdbcTemplate.update(sqlAttr, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
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
    public PersistenceEntity getOne(int objectId) {
        PersistenceEntity persistenceEntity;
        persistenceEntity = jdbcTemplate.queryForObject(SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID,objectId);
        Map<Integer, Object> attributes = getAttributes(rows);
        persistenceEntity.setAttributes(attributes);
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(int objectTypeId) {
        List<PersistenceEntity> persistenceEntityList = null;
        PersistenceEntity persistenceEntity = null;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_OBJECTS);
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
            Map<Integer, Object> attributes = getAttributes(rowss);
            pe.setAttributes(attributes);
            persistenceEntityList.set(i,pe);
        }

        return persistenceEntityList;
    }

    private Map<Integer, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Integer, Object> attributes = null;
        for (Map r : rowss) {
            int k = (Integer)r.get("attr_id");
            Object value = null;
            if ((k>=1 && k<=5) || (k>=7 && k<=9) || (k>=11 && k<=17) || (k>=19 && k<=24)){
                value = (Object)r.get("value");
            }
            if (k== 6 || k==10 || k==25) {
                value = (Object)r.get("date_value");
            }
            if (k== 18 || k==31 || k==34) {
                value = (Object)r.get("list_value_id");
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
                ps.setInt(++i, persistenceEntity.getParentId());
                ps.setInt(++i, persistenceEntity.getObjectTypeId());
                ps.setString(++i,  "Name ".concat(Integer.toString(i)));
                ps.setString(++i, null);
                ps.setLong(++i, persistenceEntity.getObjectId());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry,PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                int k = (Integer)entry.getKey();
                ps.setInt(++i,k);
                if ((k>=1 && k<=5) || (k>=7 && k<=9) || (k>=11 && k<=17) || (k>=19 && k<=24)){
                    ps.setString(++i, String.valueOf(entry.getValue()));
                    ps.setDate(++i, null);
                    ps.setInt(++i, 0);
                }
                if (k== 6 || k==10 || k==25) {
                    ps.setString(++i, null);
                    ps.setDate(++i, Date.valueOf(String.valueOf(entry.getValue())));
                    ps.setInt(++i, 0);
                }
                if (k== 18 || k==31 || k==34) {
                    ps.setString(++i, null);
                    ps.setDate(++i, null);
                    ps.setInt(++i, Integer.getInteger(String.valueOf(entry.getValue())));
                }
                ps.setLong(++i, persistenceEntity.getObjectId());

            }
        };
    }
}

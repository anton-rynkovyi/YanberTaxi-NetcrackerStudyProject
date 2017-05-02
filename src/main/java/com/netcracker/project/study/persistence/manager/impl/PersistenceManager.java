package com.netcracker.project.study.persistence.manager.impl;

import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.netcracker.project.study.persistence.entity.impl.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.Manager;
import java.util.HashMap;
import java.util.Map;


public class PersistenceManager implements Manager {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        String sql = "INSERT INTO OBJECTS(parent_id, object_type_id, name, description,object_id) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            String sqlAttr = "";
            sqlAttr = "INSERT INTO ATTRIBUTES(attr_id, value, date_value, list_value_id,object_id) VALUES(?,?,?,?,?)";
            jdbcTemplate.update(sqlAttr, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
        return persistenceEntity;
    }

    @Override
    public void update(PersistenceEntity persistenceEntity) {
        String sql = "UPDATE OBJECTS SET parent_id=?, object_type_id=?, name=?, description=? WHERE object_id=?";
        jdbcTemplate.update(sql, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            String sqlAttr = "";
            sqlAttr = "UPDATE ATTRIBUTES SET attr_id=? value=? date_value=? list_value_id=? WHERE object_id=? ";
            jdbcTemplate.update(sqlAttr, getPreparedStatementSetterAttributes(entry,persistenceEntity));
        }
    }

    @Override
    public void delete(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update("DELETE FROM OBJECTS WHERE object_id=?", persistenceEntity.getObjectId());
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
        persistenceEntity = jdbcTemplate.queryForObject("SELECT * FROM OBJECTS WHERE object_id=? ", rowMapper, objectId);
        Map<Integer, Object> attributes = null;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM Attributes WHERE object_id=? ",objectId);
        for (Map row : rows) {
            int k = (Integer)row.get("attr_id");
            Object value = null;
            if ((k>=1 && k<=5) || (k>=7 && k<=9) || (k>=11 && k<=17) || (k>=19 && k<=24)){
                value = (Object)row.get("value");
            }
            if (k== 6 || k==10 || k==25) {
                value = (Object)row.get("date_value");
            }
            if (k== 18 || k==31 || k==34) {
                value = (Object)row.get("list_value_id");
            }
            attributes.put(k,value) ;
        }
        persistenceEntity.setAttributes(attributes);
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(int objectTypeId) {
        List<PersistenceEntity> persistenceEntityList = null;
        PersistenceEntity persistenceEntity = null;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM Objects");
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
            Map<Integer, Object> attributes = null;
            List<Map<String, Object>> rowss = jdbcTemplate.queryForList("SELECT * FROM Attributes WHERE object_id=? ",id);
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
            pe.setAttributes(attributes);
            persistenceEntityList.set(i,pe);
        }

        return persistenceEntityList;
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

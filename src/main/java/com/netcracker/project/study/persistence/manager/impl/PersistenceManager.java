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
import java.sql.Date;
import java.util.*;

import static java.sql.Types.*;

@Component("persistenceManager")
public class PersistenceManager implements Manager {

    public static final String CREATE_OBJECTS = ""+
            "INSERT INTO OBJECTS" +
            "(parent_id, object_type_id, name, description, object_id)" +
            " VALUES(?,?,?,?,?)";
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
            "Select attr.value, attr.Date_value,LISTS.value as list_value_id, attr.attr_id " +
            "from  Attributes attr " +
            "LEFT JOIN  LISTS " +
            "ON attr.List_value_id = LISTS.list_value_id " +
            "where attr.OBJECT_ID = ?";
    public static final String SELECT_FROM_OBJREFERENCE = ""+
            "SELECT * " +
            "FROM OBJREFERENCE " +
            "WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS = ""+
            "SELECT * " +
            "FROM Objects " +
            "WHERE object_type_id = ?";
    public static final String GENERATE_MAX_OBJECT_ID = ""+
            "select " +
            "nvl(MAX(object_id),0)+1 as object_id " +
            "from objects";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedTemplate;


    @Autowired
    public PersistenceManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        persistenceEntity.setObjectId(jdbcTemplate.queryForObject(GENERATE_MAX_OBJECT_ID, rowMapperObjectId).getObjectId());
        jdbcTemplate.update(CREATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(CREATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, persistenceEntity));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("0")) ) {
                jdbcTemplate.update(CREATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, persistenceEntity));
            }
        }
        return persistenceEntity;
    }


    @Override
    public void update(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update(UPDATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(UPDATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, persistenceEntity));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("0"))) {
                jdbcTemplate.update(UPDATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, persistenceEntity));
            }
        }
    }


    @Override
    public void delete(long objectId) {
        jdbcTemplate.update(DELETE_FROM_OBJECTS, objectId);
        jdbcTemplate.update(DELETE_FROM_ATTRIBUTES, objectId);
        jdbcTemplate.update(DELETE_FROM_OBJREFERENCE, objectId);
        jdbcTemplate.update("commit");
    }


    private RowMapper<PersistenceEntity> rowMapper = new RowMapper<PersistenceEntity>() {
        @Override
        public PersistenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(rs.getLong("object_id"));
            persistenceEntity.setObjectTypeId(rs.getInt("object_type_id"));
            persistenceEntity.setParentId(rs.getInt("parent_id"));
            persistenceEntity.setName(rs.getString("name"));
            persistenceEntity.setDescription(rs.getString("description"));
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
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID, objectId);
        Map<Long, Object> attributes = getAttributes(rows);
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(SELECT_FROM_OBJREFERENCE, objectId);
        Map<Long, Long> references = getReferences(rowsRef);
        persistenceEntity.setReferences(references);
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(long objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_OBJECTS, new Object[]{objectTypeId});
        if (rows.isEmpty()) return Collections.emptyList();
        List<PersistenceEntity> persistenceEntityList = new ArrayList<>();
        for (Map row : rows) {
            PersistenceEntity persistenceEntity = getOne(Long.parseLong(row.get("object_id")+""));
            persistenceEntityList.add(persistenceEntity);
        }
        return persistenceEntityList;
    }


    private Map<Long, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Long, Object> attributes = new HashMap();
        for (Map row : rowss) {
            long attrId = Long.parseLong(String.valueOf(row.get("attr_id")));
            Object value = null;

            if (row.get("value") != null) {
                value = row.get("value");
                try {
                    value = Integer.parseInt(value+"");
                } catch (Exception e) {}
            } else if (row.get("date_value") != null) {
                value = Timestamp.valueOf(row.get("date_value")+"");
            } else if (row.get("list_value_id") != null) {
                value = row.get("list_value_id")+"";
            }

            attributes.put(attrId, value);
        }
        return attributes;
    }

    private Map<Long, Long> getReferences(List<Map<String, Object>> rowss) {
        Map<Long, Long> reference = new HashMap();
        for (Map row : rowss) {
            long attrId = Long.parseLong(String.valueOf(row.get("attr_id")));
            Long value = null;
            if (row.get("reference") != null) {
                value = Long.parseLong(row.get("reference")+"");
            }

            reference.put(attrId, value);
        }
        return reference;
    }

    private PreparedStatementSetter getPreparedStatementSetterObjects(final PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (persistenceEntity.getParentId() == 0) {
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

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry, PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                long attrId = (long) entry.getKey();

                if ((attrId>=1 && attrId<=5) || (attrId>=7 && attrId<=8) || (attrId>=11 && attrId<=17) || (attrId>=19 && attrId<=24)){
                    ps.setString(++i, String.valueOf(entry.getValue()));
                    ps.setNull(++i, DATE);
                    ps.setNull(++i, NUMERIC);
                }else if (attrId == 6 || attrId == 10 || attrId == 25) {
                    ps.setString(++i, null);
                    ps.setTimestamp(++i, entry.getValue() != null ? Timestamp.valueOf(String.valueOf(entry.getValue())) : null);
                    ps.setNull(++i, NUMERIC);
                }else if (attrId == 9 || attrId == 18 || attrId == 31 || attrId == 34) {
                    ps.setString(++i, null);
                    ps.setNull(++i, DATE);
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NUMERIC);
                    } else {
                        ps.setInt(++i, (Integer) entry.getValue());
                    }

                }
                ps.setLong(++i, persistenceEntity.getObjectId());
                ps.setLong(++i,attrId);
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterRef(Map.Entry entry, PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setLong(++i, entry.getValue() == null ? 0l : Long.parseLong(String.valueOf(entry.getValue())));
                ps.setLong(++i, persistenceEntity.getObjectId());
                ps.setLong(++i, (Long) entry.getKey());
                // todo
            }
        };
    }

}
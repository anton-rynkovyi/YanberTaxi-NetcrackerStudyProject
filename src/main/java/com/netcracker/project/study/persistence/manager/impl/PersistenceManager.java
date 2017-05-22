package com.netcracker.project.study.persistence.manager.impl;

import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.Manager;
import com.netcracker.project.study.persistence.manager.queries.Crud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.sql.Types.*;

@Component("persistenceManager")
public class PersistenceManager implements Manager {


    private JdbcTemplate jdbcTemplate;


    @Autowired
    public PersistenceManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(Crud.CREATE_OBJECTS, new String[] {"object_id"});
                int i = 0;
                if (persistenceEntity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, persistenceEntity.getParentId().toString());
                }
                ps.setString(++i, persistenceEntity.getObjectTypeId().toString());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
                return ps;
            }
        }, keyHolder);
        persistenceEntity.setObjectId(BigInteger.valueOf(keyHolder.getKey().longValue()));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(Crud.CREATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, persistenceEntity,true));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                jdbcTemplate.update(Crud.CREATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, persistenceEntity,true));
            }
        }
        return persistenceEntity;
    }


    @Override
    public void update(PersistenceEntity persistenceEntity) {
        jdbcTemplate.update(Crud.UPDATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(Crud.UPDATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, persistenceEntity,false));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                jdbcTemplate.update(Crud.UPDATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, persistenceEntity,false));
            }
        }
    }


    @Override
    public void delete(BigInteger objectId) {
        jdbcTemplate.update(Crud.DELETE_FROM_OBJECTS, objectId.longValue());
        jdbcTemplate.update(Crud.DELETE_FROM_ATTRIBUTES, objectId.longValue());
        jdbcTemplate.update(Crud.DELETE_FROM_OBJREFERENCE, objectId.longValue());
        jdbcTemplate.update("commit");
    }


    private RowMapper<PersistenceEntity> rowMapper = new RowMapper<PersistenceEntity>() {
        @Override
        public PersistenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(rs.getBigDecimal("object_id").toBigInteger());
            persistenceEntity.setObjectTypeId(rs.getBigDecimal("object_type_id").toBigInteger());
            persistenceEntity.setParentId(rs.getBigDecimal("parent_id") != null ?
                    rs.getBigDecimal("parent_id").toBigInteger() : null);
            persistenceEntity.setName(rs.getString("name"));
            persistenceEntity.setDescription(rs.getString("description"));
            return persistenceEntity;
        }
    };

    private RowMapper<PersistenceEntity> rowMapperObjectId = new RowMapper<PersistenceEntity>() {
        @Override
        public PersistenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersistenceEntity persistenceEntity = new PersistenceEntity();
            persistenceEntity.setObjectId(rs.getBigDecimal("object_id").toBigInteger());
            return persistenceEntity;
        }
    };

    @Override
    public PersistenceEntity getOne(BigInteger objectId) {
        PersistenceEntity persistenceEntity = jdbcTemplate.queryForObject(Crud.SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId.longValue());
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Crud.SELECT_FROM_ATTRIBUTES_BY_ID, objectId.longValue());
        Map<BigInteger, Object> attributes = getAttributes(rows);
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(Crud.SELECT_FROM_OBJREFERENCE, objectId.longValue());
        Map<BigInteger, BigInteger> references = getReferences(rowsRef);
        persistenceEntity.setAttributes(attributes);
        persistenceEntity.setReferences(references);
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(BigInteger objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Crud.SELECT_FROM_OBJECTS, new Object[]{objectTypeId.longValue()});
        if (rows.isEmpty()) return Collections.emptyList();
        List<PersistenceEntity> persistenceEntityList = new ArrayList<>();
        for (Map row : rows) {
            PersistenceEntity persistenceEntity = getOne(BigInteger.valueOf(Long.parseLong(row.get("object_id")+"")));
            persistenceEntityList.add(persistenceEntity);
        }
        return persistenceEntityList;
    }


    private Map<BigInteger, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<BigInteger, Object> attributes = new HashMap();
        for (Map row : rowss) {
            BigInteger attrId = new BigInteger(String.valueOf(row.get("attr_id")));
            Object value = null;

            if (row.get("value") != null) {
               value = String.valueOf(row.get("value"));
            } else if (row.get("date_value") != null) {
                value = String.valueOf(row.get("date_value"));
            } else if (row.get("list_value_id") != null) {
                value = String.valueOf(row.get("list_value_id"));
            }
            attributes.put(attrId, value);
        }
        return attributes;
    }

    private Map<BigInteger, BigInteger> getReferences(List<Map<String, Object>> rowss) {
        Map<BigInteger, BigInteger> reference = new HashMap();
        for (Map row : rowss) {
            BigInteger attrId = new BigInteger(String.valueOf(row.get("attr_id")));
            BigInteger value = null;
            if (row.get("reference") != null) {
                value = new BigInteger(row.get("reference")+"");
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
                ps.setString(++i, persistenceEntity.getObjectId().toString());
                if (persistenceEntity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, persistenceEntity.getParentId().toString());
                }
                ps.setString(++i, persistenceEntity.getObjectTypeId().toString());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
                ps.setString(++i, persistenceEntity.getObjectId().toString());
                if (persistenceEntity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, persistenceEntity.getParentId().toString());
                }
                ps.setString(++i, persistenceEntity.getObjectTypeId().toString());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry, PersistenceEntity persistenceEntity,boolean create) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                BigInteger attrId = new BigInteger(String.valueOf(entry.getKey()));
                if (create) {
                    prepareValue(ps,entry,0);
                    ps.setString(4, persistenceEntity.getObjectId().toString());
                    ps.setString(5, attrId.toString());
                } else{
                    ps.setString(1, persistenceEntity.getObjectId().toString());
                    ps.setString(2, attrId.toString());
                    prepareValue(ps,entry,2);
                    ps.setString(6, persistenceEntity.getObjectId().toString());
                    ps.setString(7, attrId.toString());
                    prepareValue(ps,entry,7);
                }
            };
            private void prepareValue(PreparedStatement ps,Map.Entry entry,int i) throws SQLException {
                if (entry.getValue().getClass().getSimpleName().equals("String")) {
                    if (entry.getValue().equals("-1")) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setNull(++i, DATE);
                    ps.setNull(++i, NUMERIC);

                } else  if (entry.getValue().getClass().getSimpleName().equals("Date")) {
                    ps.setString(++i, null);
                    if (((Date) entry.getValue()).getTime() == new Date(-1).getTime()) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setTimestamp(++i, new Timestamp(((Date) entry.getValue()).getTime()));
                    }
                    ps.setNull(++i, NUMERIC);

                } else if (entry.getValue().getClass().getSimpleName().equals("BigInteger")) {
                    ps.setString(++i, null);
                    ps.setNull(++i, DATE);
                    if (BigInteger.valueOf(Long.parseLong(String.valueOf(entry.getValue()))).compareTo(BigInteger.valueOf(-1)) == 0) {
                        ps.setNull(++i, NUMERIC);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                }
            };
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterRef(Map.Entry entry, PersistenceEntity persistenceEntity,boolean create) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (create) {
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setString(++i, String.valueOf(persistenceEntity.getObjectId()));
                    ps.setString(++i, String.valueOf(entry.getKey()));
                } else {
                    ps.setString(++i, String.valueOf(persistenceEntity.getObjectId()));
                    ps.setString(++i, String.valueOf(entry.getKey()));
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setString(++i, String.valueOf(persistenceEntity.getObjectId()));
                    ps.setString(++i, String.valueOf(entry.getKey()));
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                }
            }
        };
    }

}
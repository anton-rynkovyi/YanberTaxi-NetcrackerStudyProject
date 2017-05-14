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
import java.math.BigInteger;
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
            "select " +
            "nvl(MAX(object_id),0)+1 as object_id " +
            "from objects";

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public PersistenceManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public PersistenceEntity create(PersistenceEntity persistenceEntity) {
        persistenceEntity.setObjectId(jdbcTemplate.queryForObject(GENERATE_MAX_OBJECT_ID, rowMapperObjectId).getObjectId());
        jdbcTemplate.update(CREATE_OBJECTS, getPreparedStatementSetterObjects(persistenceEntity));
        for (Map.Entry entry : persistenceEntity.getAttributes().entrySet()) {
            jdbcTemplate.update(CREATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, persistenceEntity));
        }
        for (Map.Entry entry : persistenceEntity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
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
            if (!(entry.getValue().toString().equals("-1"))) {
                jdbcTemplate.update(UPDATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, persistenceEntity));
            }
        }
    }


    @Override
    public void delete(BigInteger objectId) {
        jdbcTemplate.update(DELETE_FROM_OBJECTS, objectId);
        jdbcTemplate.update(DELETE_FROM_ATTRIBUTES, objectId);
        jdbcTemplate.update(DELETE_FROM_OBJREFERENCE, objectId);
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
        PersistenceEntity persistenceEntity = jdbcTemplate.queryForObject(SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_ATTRIBUTES_BY_ID, objectId);
        Map<BigInteger, Object> attributes = getAttributes(rows);
        //System.out.println(attributes); //todo SOUT
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(SELECT_FROM_OBJREFERENCE, objectId);
        Map<BigInteger, BigInteger> references = getReferences(rowsRef);
        persistenceEntity.setAttributes(attributes);
        persistenceEntity.setReferences(references);
        //System.out.println("ATTR: " + persistenceEntity); //todo SOUT
        return persistenceEntity;
    }

    @Override
    public List<PersistenceEntity> getAll(BigInteger objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_FROM_OBJECTS, new Object[]{objectTypeId});
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
                value = row.get("value");
                try {
                    value = new BigInteger(value+"");
                } catch (Exception e) {}
            } else if (row.get("date_value") != null) {
                value = Timestamp.valueOf(row.get("date_value")+"");
            } else if (row.get("list_value_id") != null) {
                value = new BigInteger((row.get("list_value_id")+""));
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
                if (persistenceEntity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, persistenceEntity.getParentId().toString());
                }
                ps.setString(++i, persistenceEntity.getObjectTypeId().toString());
                ps.setString(++i, persistenceEntity.getName());
                ps.setString(++i, persistenceEntity.getDescription());
                ps.setString(++i, persistenceEntity.getObjectId().toString());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry, PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                BigInteger attrId = new BigInteger(String.valueOf(entry.getKey()));
                //System.out.println(entry.getValue() + " : " + entry.getValue().getClass().getSimpleName()); //todo SOUT

                if (entry.getValue().getClass().getSimpleName().equals("String")) {
                    if (entry.getValue().equals("-1")) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setNull(++i, DATE);
                    ps.setNull(++i, NUMERIC);

                } else  if (entry.getValue().getClass().getSimpleName().equals("Timestamp")) {
                    ps.setString(++i, null);
                    if (Timestamp.valueOf(String.valueOf(entry.getValue())).compareTo(new Timestamp(-1)) == 0) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setTimestamp(++i, Timestamp.valueOf(String.valueOf(entry.getValue())));
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

                ps.setString(++i, persistenceEntity.getObjectId().toString());
                ps.setString(++i, attrId.toString());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterRef(Map.Entry entry, PersistenceEntity persistenceEntity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (entry.getValue() == null) {
                    ps.setNull(++i, NULL);
                } else {
                    ps.setString(++i, String.valueOf(entry.getValue()));
                }
                ps.setString(++i, String.valueOf(persistenceEntity.getObjectId()));
                ps.setString(++i, String.valueOf(entry.getKey()));
                //todo
            }
        };
    }

}
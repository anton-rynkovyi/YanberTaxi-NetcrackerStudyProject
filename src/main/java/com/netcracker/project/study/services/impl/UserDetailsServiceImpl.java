package com.netcracker.project.study.services.impl;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.manager.queries.Crud;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.sun.istack.NotNull;
import org.apache.xpath.operations.Mod;
import java.lang.String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.access.method.P;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.sql.Types.NUMERIC;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    OrdersViewForDrivers ordersViewForDrivers;

    private JdbcTemplate jdbcTemplate;

    public static final String CREATE_USERS = ""+
            "INSERT INTO USERS" +
            "(object_id, login, password,role)" +
            " VALUES(?,?,?,?,?)";

    public static final String SELECT_USERS = ""+
            "SELECT * " +
            "FROM USERS " +
            "WHERE login = ? and password = ?";

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Driver user = persistenceFacade.getOne(BigInteger.valueOf(202), Driver.class);
        //Driver user = new Driver();
        //user.setObjectId(BigInteger.valueOf(202));
        user.setUsername("driver");
        user.setPassword("123");
        user.setAuthorities(ImmutableList.of(Role.ROLE_DRIVER));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
       //ordersViewForDrivers.init(user);

        return user;
    }

    public com.netcracker.project.study.model.user.User createUser(com.netcracker.project.study.model.user.User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(CREATE_USERS, new String[] {"user_id"});
                int i = 0;
                ps.setString(++i, user.getObjectId().toString());
                ps.setString(++i, user.getLogin());
                ps.setString(++i, user.getPassword());
                ps.setString(++i, user.getRole());
                return ps;
            }
        }, keyHolder);
        user.setUserId(BigInteger.valueOf(keyHolder.getKey().longValue()));
        return user;
    }







    public com.netcracker.project.study.model.user.User selectUser(String login, String password) {
        com.netcracker.project.study.model.user.User user = jdbcTemplate.queryForObject(SELECT_USERS,new Object[] {login,password},rowMapper);
        return user;
    }

   @NotNull
    private RowMapper<com.netcracker.project.study.model.user.User> rowMapper = new RowMapper<com.netcracker.project.study.model.user.User>() {
        @Override
        public com.netcracker.project.study.model.user.User mapRow(ResultSet rs, int rowNum) throws SQLException {
            com.netcracker.project.study.model.user.User user = new com.netcracker.project.study.model.user.User();
            user.setUserId(rs.getBigDecimal("user_id").toBigInteger());
            user.setObjectId(rs.getBigDecimal("object_id").toBigInteger());
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }
    };









    public <T extends Model> T findUserByPhoneNumber(String phoneNumber) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.value = '" + phoneNumber + "'";
        List<Model> users = persistenceFacade.getSome(query, Model.class);
        if (users != null && !users.isEmpty()) {

        }
        return null;
    }
}

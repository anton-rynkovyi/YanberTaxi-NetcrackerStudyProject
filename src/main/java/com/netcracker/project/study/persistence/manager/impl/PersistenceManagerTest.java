package com.netcracker.project.study.persistence.manager.impl;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class PersistenceManagerTest {
    @Autowired
    private PersistenceManager persistenceManager;
    @Autowired
    private ConverterFactory converterFactory;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {
        Driver driver = new Driver(20);
        driver.setEmail("abc.com");
        driver.setFirstName("Misha");
        PersistenceEntity persistenceEntity = converterFactory.convertToEntity(driver);
        persistenceManager.create(persistenceEntity);
        // todo проверить айди не 0
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void getOne() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

}
package com.netcracker.project.study;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        ApplicationContext ctx =
                new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        PersistenceFacade facade = (PersistenceFacade) ctx.getBean(PersistenceFacade.class);

/*
        Client client = new Client("NEW CLIENT");
        client.setFirstName("Anton");
        facade.create(client);
        System.out.println(client);
        Client client1 = facade.getOne(client.getObjectId(), Client.class);
        System.out.println(client1);*/

        Driver driver = new Driver("NEW DRIVER");
        driver.setFirstName("Anton");
        driver.setHireDate(new Timestamp(System.currentTimeMillis()));
        driver.setStatus(2);
        driver.setHireDate(new Timestamp(System.currentTimeMillis()));
        driver.setRating(3);

        facade.create(driver);
        System.out.println(driver);

        Driver driver1 = facade.getOne(driver.getObjectId(), Driver.class);
        System.out.println(driver1);


       /*List<Driver> drivers = facade.getAll(Driver.OBJECT_TYPE_ID, Driver.class);

        for (int i = 0; i < drivers.size(); i++) {
            System.out.println(drivers.get(i));
        }*/

    }
}

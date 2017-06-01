package com.netcracker.project.study;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.impl.AdminServiceImpl;
import com.netcracker.project.study.services.impl.DriverServiceImpl;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        ApplicationContext ctx =
                new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        PersistenceFacade facade = (PersistenceFacade) ctx.getBean(PersistenceFacade.class);
        AdminService adminService = ctx.getBean(AdminServiceImpl.class);
        DriverService driverService = ctx.getBean(DriverServiceImpl.class);

        /*for (int i = 0; i < 1000; i++) {
            Client client = new Client("NEW CLIENTssssssssssss", "It's client!");
            client.setLastName("Rynkovoy");
            client.setFirstName("Anton");
            client.setMiddleName("Andreevich");
            client.setPoints(BigInteger.valueOf(50));
            facade.create(client);
        }*/

       /* Driver driver = facade.getOne(BigInteger.valueOf(11), Driver.class);
        System.out.println(driver);*/

       /* final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.warn("MAIN");
        Driver driver = new Driver("ESSS", "It's driver");
        driver.setFirstName("Anton");
        driver.setLastName("Rynkovoy");
        driver.setHireDate(new Date(System.currentTimeMillis()));
        driver.setStatus(new BigInteger("1"));
        driver.setRating(BigDecimal.valueOf(3));
        facade.create(driver);
        System.out.println(driver);

        /*Driver driver = facade.getOne(BigInteger.valueOf(16), Driver.class);
        System.out.println("HD: " + driver.getHireDate());
        System.out.println("UD: " + driver.getUnbanDate());*/


        /*driver.setName("Update driver");
        driver.setStatus(BigInteger.valueOf(5));
        driver.setRating(null);
        driver.setFirstName("BBBBBBBBBBBBBBBBBBBBBB");

        facade.update(driver);*/
        //facade.delete(driver.getObjectId());


       /*Order order = new Order("NEW ORDER");
       order.setCost(BigDecimal.valueOf(46.55));
       order.setStatus(Driver.FREE);
       order.setDriverRating(BigInteger.valueOf(2));
       order.setDriverMemo("MEMO");
       facade.create(order);*/

       /*Order order = facade.getOne(BigInteger.valueOf(1), Order.class);
       System.out.println(order.getCost());*/

     /*   Driver driver = facade.getOne(BigInteger.valueOf(4), Driver.class);
        System.out.println();*/


      /* order.setName("SFFSFSFSFSFFSS");
       order.setDriverId(BigInteger.valueOf(6));
       facade.update(order);*/

       /*Order order = new Order("MyOrder", "It's my order");
       order.setDriverMemo("Driver MEMO");
       order.setCost(BigDecimal.valueOf(50.45));
       order.setStatus(BigInteger.valueOf(3));
       System.out.println("PE: " + facade.create(order));*/

      /* Order order1 = facade.getOne(BigInteger.valueOf(6), Order.class);
       System.out.println(order1);*/

        //facade.delete(BigInteger.valueOf(11));
        /*String sqlQuery = "select obj.OBJECT_ID from objects obj, Attributes attr " +
                "where obj.object_id = attr.object_id " +
                "and obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "and attr.list_value_id = " + DriverStatusValues.APPROVAL.intValue();*/




     /* List<Driver> orderList = adminService.getDriversWithoutApproval();
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println(orderList.get(i).getObjectId() + ": " + orderList.get(i));
        }*/


       /* Driver driver = new Driver();
        driver.setName("Driver Vadim");
        driver.setFirstName("Vadim");
        driver.setLastName("Martsun");
        driver.setEmail("vadimmartsun@gmail.com");
        driver.setMiddleName("Vladimirovich");
        driver.setExperience(BigInteger.valueOf(5));
        driver.setPhoneNumber("(068)067-68-53");
        driver.setStatus(BigInteger.valueOf(3));
        adminService.createModel(driver);

        Car car = new Car();
        car.setName("Vadim Car");
        car.setChildSeat(true);
        car.setStateNumber("BH80567");
        car.setModelType("Amulet");
        car.setMakeOfCar("Chery");
        car.setDriverId(driver.getObjectId());
        car.setReleaseDate(Date.valueOf("2006-01-01"));
        adminService.createModel(car);*/


        /*Client client = new Client();
        client.setName("Client Blazko");
        client.setFirstName("Alexandr");
        client.setLastName("Blazko");
        client.setPhoneNumber("093-40-35-932");
        facade.create(client);*/

        /*Order order = new Order();
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(67));
        order.setDistance(BigInteger.valueOf(27));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order);

        Order order1 = new Order();
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(123));
        order.setDistance(BigInteger.valueOf(77));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order1);

        Order order2 = new Order();
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(30));
        order.setDistance(BigInteger.valueOf(12));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order2);*/


        Order order = new Order();
        order.setName("Order_21");
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(303));
        order.setDistance(BigInteger.valueOf(133));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order);

        Order order1 = new Order();
        order.setName("Order_31");
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(205));
        order.setDistance(BigInteger.valueOf(142));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order1);
        Order order2 = new Order();
        order.setName("Order_41");
        order.setClientId(BigInteger.valueOf(4));
        order.setDriverId(BigInteger.valueOf(1));
        order.setCost(BigDecimal.valueOf(190));
        order.setDistance(BigInteger.valueOf(123));
        order.setStatus(BigInteger.valueOf(6));
        facade.create(order2);

       /*Order order = facade.getOne(BigInteger.valueOf(10),Order.class);
        System.out.println(order.getName());
        System.out.println(order.getClientId());
        System.out.println(order.getDriverId());
        System.out.println(order.getCost());*/

        //driverService.acceptOrder(BigInteger.valueOf(10),BigInteger.valueOf(1));



        List<Order>orders = adminService.allModelsAsList(Order.class);
        ListIterator<Order> listIterator = orders.listIterator();
        while(listIterator.hasNext()){
            System.out.println(listIterator.next());
        }


       // Order order = facade.getOne(BigInteger.valueOf(23),Order.class);
       // System.out.print(order.getDistance());
       // driverService.acceptOrder(order,BigInteger.valueOf(1));

       // facade.create(client);

      /*  Driver driver = facade.getOne(BigInteger.valueOf(111), Driver.class);
        System.out.println(driver.getLastName());*/

        //List<Car> carList = adminService.getCarByDriver(driver);
        //System.out.println(carList.size());

    /*    System.out.println(carList.get(0).getDriverId());
        System.out.println(carList.get(0).getMakeOfCar());*/
    }
}

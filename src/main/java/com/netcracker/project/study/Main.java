package com.netcracker.project.study;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.admin.AdminAttr;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.services.impl.AdminServiceImpl;
import com.netcracker.project.study.services.impl.DriverServiceImpl;
import com.netcracker.project.study.services.impl.OrderServiceImpl;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        ApplicationContext ctx =
                new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        PersistenceFacade facade =  ctx.getBean(PersistenceFacade.class);
        AdminService adminService = ctx.getBean(AdminServiceImpl.class);
        UserDetailsServiceImpl userDetailsService = ctx.getBean(UserDetailsServiceImpl.class);
        DriverService driverService = ctx.getBean(DriverServiceImpl.class);
        OrderService orderService = ctx.getBean(OrderServiceImpl.class);
        PersistenceManager manager = ctx.getBean(PersistenceManager.class);


        DriverService driverService1 = ctx.getBean(DriverService.class);
        Driver driverList = driverService1.getDriverByPhoneNumber("(111)111-11-11");
        System.out.println(driverList);


    /*    Driver driver = userDetailsService.findDriverByUserName("(666)666-66-66");
        System.out.println(driver.getStatus());*/




       /* Driver driver = new Driver();
        facade.create(driver);
        driverService.changeStatus(DriverStatusList.FREE,driver);
        driverService.changeStatus(DriverStatusList.PERFORMING_ORDER,driver);*/

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

       /*final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.warn("MAIN");
        Driver driver = new Driver("ESSS", "It's driver");
        driver.setFirstName("Anton");
        driver.setLastName("Rynkovoy");
        driver.setHireDate(new Date(System.currentTimeMillis()));
        driver.setStatus(new BigInteger("1"));
        driver.setRating(BigDecimal.valueOf(3));
        facade.create(driver);
        System.out.println(driver);*/

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
                "and attr.list_value_id = " + DriverStatusList.APPROVAL.intValue();*/




     /* List<Driver> orderList = adminService.getDriversWithoutApproval();
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println(orderList.get(i).getObjectId() + ": " + orderList.get(i));
        }*/


        /*Driver driver = new Driver();
        driver.setName("Driver Vadim");
        driver.setFirstName("Vadim");
        driver.setLastName("Martsun");
        driver.setEmail("vadimmartsun@gmail.com");
        driver.setMiddleName("Vladimirovich");
        driver.setExperience(BigInteger.valueOf(5));
        driver.setPhoneNumber("(068)067-68-53");
        driver.setStatus(BigInteger.valueOf(2));
        adminService.createModel(driver);

        Car car = new Car();
        car.setChildSeat(true);
        car.setDriverId(driver.getObjectId());
        car.setModelType("CX9");
        car.setMakeOfCar("Mazda");
        car.setStateNumber("BH4454TC");
        car.setReleaseDate(java.sql.Date.valueOf("2012-01-01"));
        adminService.createModel(car);

        Driver driver1 = new Driver();
        driver1.setFirstName("Vadim");
        driver1.setLastName("Martsun");
        driver1.setEmail("vadimmartsun@gmail.com");
        driver1.setMiddleName("Vladimirovich");
        driver1.setExperience(BigInteger.valueOf(5));
        driver1.setPhoneNumber("(063)395-02-04");
        driver1.setStatus(BigInteger.valueOf(3));
        adminService.createModel(driver1);
        Car car1 = new Car();
        car1.setChildSeat(true);
        car1.setDriverId(driver1.getObjectId());
        car1.setModelType("6");
        car1.setMakeOfCar("Mazda");
        car1.setStateNumber("BH4373TC");
        car1.setReleaseDate(java.sql.Date.valueOf("2004-01-01"));
        adminService.createModel(car1);
        User userDriver = new User();
        //userDriver.setUserDriver();
        userDriver.setObjectId(driver1.getObjectId());
        userDriver.setAuthorities(ImmutableList.of(Role.ROLE_DRIVER));
        userDriver.setUsername(driver1.getPhoneNumber());
        userDriver.setPassword("ryn123");
        userDriver.setEnabled(true);
        manager.createUser(userDriver);


        Driver driver2 = new Driver();
        driver2.setFirstName("Ed");
        driver2.setLastName("Belchik");
        driver2.setEmail("eugenedoroganov@gmail.com");
        driver2.setMiddleName("Alexandrovich");
        driver2.setExperience(BigInteger.valueOf(5));
        driver2.setPhoneNumber("(068)066-78-53");
        driver2.setStatus(BigInteger.valueOf(3));
        adminService.createModel(driver2);
        Car car2 = new Car();
        car2.setChildSeat(false);
        car2.setDriverId(driver2.getObjectId());
        car2.setModelType("i8");
        car2.setMakeOfCar("BMW");
        car2.setStateNumber("YANBER");
        car2.setReleaseDate(java.sql.Date.valueOf("2017-01-01"));
        adminService.createModel(car2);
        User userDriver1 = new User();
        //userDriver.setUserDriver();
        userDriver1.setObjectId(driver2.getObjectId());
        userDriver1.setAuthorities(ImmutableList.of(Role.ROLE_DRIVER));
        userDriver1.setUsername(driver2.getPhoneNumber());
        userDriver1.setPassword("bel123");
        userDriver1.setEnabled(true);
        manager.createUser(userDriver1);


        Client client = new Client();
        client.setLastName("Cristiano");
        client.setFirstName("Ronaldo");
        client.setPhoneNumber("(011)111-11-11");
        client.setPoints(BigInteger.valueOf(32));
        adminService.createModel(client);
        User userClient = new User();
        userClient.setObjectId(client.getObjectId());
        userClient.setAuthorities(ImmutableList.of(Role.ROLE_CLIENT));
        userClient.setUsername(client.getPhoneNumber());
        userClient.setPassword("ivan123");
        userClient.setEnabled(true);
        manager.createUser(userClient);


        Client client1 = new Client();
        client1.setLastName("Lionel");
        client1.setFirstName("Messi");
        client1.setPhoneNumber("(022)222-22-22");
        client1.setPoints(BigInteger.valueOf(59));
        adminService.createModel(client1);
        User userClient1 = new User();
        userClient1.setObjectId(client1.getObjectId());
        userClient1.setAuthorities(ImmutableList.of(Role.ROLE_CLIENT));
        userClient1.setUsername(client1.getPhoneNumber());
        userClient1.setPassword("leo123");
        userClient1.setEnabled(true);
        manager.createUser(userClient1);

        Client client2 = new Client();
        client1.setLastName("Lionel");
        client1.setFirstName("Messi");
        client1.setPhoneNumber("(077)777-77-77");
        client1.setPoints(BigInteger.valueOf(59));
        adminService.createModel(client1);
        User userClient2 = new User();
        userClient1.setObjectId(client1.getObjectId());
        userClient1.setAuthorities(ImmutableList.of(Role.ROLE_CLIENT));
        userClient1.setUsername(client1.getPhoneNumber());
        userClient1.setPassword("leo123");
        userClient1.setEnabled(true);
        manager.createUser(userClient1);
*/

      /* Admin admin = new Admin();
       admin.setPhoneNumber("(063)611-67-90");
       admin.setEmail("admin@mail.com");
       facade.create(admin);

        User userClient1 = new User();
        userClient1.setObjectId(admin.getObjectId());
        userClient1.setAuthorities(ImmutableList.of(Role.ROLE_ADMIN));
        userClient1.setUsername(admin.getPhoneNumber());
        userClient1.setPassword("admin");
        userClient1.setEnabled(true);
        manager.createUser(userClient1);*/

        /*String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.attr_id = " + DriverAttr.EMAIL_ATTR + " " +
                "AND attr.value = 'admin@mail.com' " +
                "OR attr.attr_id = " + AdminAttr.EMAIL_ATTR + " " +
                "AND attr.value = 'admin@mail.com'";
        List<Driver> model = facade.getSome(query, Driver.class);
        System.out.println(model.get(0).getPhoneNumber());*/

      /*  Car car = new Car();
        car.setName("Vadim Car");
        car.setChildSeat(true);
        car.setStateNumber("BH80567");
        car.setModelType("Amulet");
        car.setMakeOfCar("Chery");
        car.setDriverId(driver.getObjectId());
        car.setReleaseDate(Date.valueOf("2006-01-01"));
        adminService.createModel(car);*/


       /* Client client = new Client();
        client.setName("Client Blazko");
        client.setFirstName("Alexandr");
        client.setLastName("Blazko");
        client.setPhoneNumber("093-40-35-932");
        facade.create(client);

        Client client1 = new Client();
        client.setName("Client Anton");
        client.setFirstName("Anton");
        client.setLastName("Rynkovoy");
        client.setPhoneNumber("63-611-67-90");
        facade.create(client1);*/


       /* Order order = new Order();
        order.setName(" order");
        order.setClientId(BigInteger.valueOf(4));
        order.setCost(BigDecimal.valueOf(105.90));
        order.setDistance(BigInteger.valueOf(4783));
        order.setStatus(OrderStatusList.NEW);
        order.setDriverMemo("NEW - Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
        facade.create(order);*/

        /*Order order1 = new Order();
        order1.setName("yoba1");
        order1.setClientId(BigInteger.valueOf(4));
        order1.setCost(BigDecimal.valueOf(123));
        order1.setDistance(BigInteger.valueOf(77));
        order1.setStatus(BigInteger.valueOf(6));
        facade.create(order1);

        Order order2 = new Order();
        order2.setName("yoba2");
        order2.setClientId(BigInteger.valueOf(5));
        order2.setCost(BigDecimal.valueOf(30));
        order2.setDistance(BigInteger.valueOf(12));
        order2.setStatus(BigInteger.valueOf(6));
        facade.create(order2);
/*
        Route route = new Route();
        route.setName("lala");
        route.setOrderId(BigInteger.valueOf(6));
        route.setCheckPoint("dddw");
        Route route1 = new Route();
        route1.setName("lalass");
        route1.setOrderId(BigInteger.valueOf(7));
        route1.setCheckPoint("dddwfsfs");

        Route route2 = new Route();
        route2.setName("lala");
        route2.setOrderId(BigInteger.valueOf(8));
        route2.setCheckPoint("dddw");

        facade.create(route);
        facade.create(route1);
        facade.create(route2);*/

       /*Order order = facade.getOne(BigInteger.valueOf(10),Order.class);
        System.out.println(order.getName());
        System.out.println(order.getClientId());
        System.out.println(order.getDriverId());
        System.out.println(order.getCost());*/

        //driverService.acceptOrder(BigInteger.valueOf(10),BigInteger.valueOf(1));

       /* List<Order>orders = orderService.getOrdersByDriverId(BigInteger.valueOf(11));
        System.out.println(orders.size());*/


        /*List<Order>orders = adminService.allModelsAsList(Order.class);
        ListIterator<Order> listIterator = orders.listIterator();
        while(listIterator.hasNext()){
            System.out.println(listIterator.next());
        }*/


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
/*
        List<Driver> driverList = adminService.getDriversByStatusId(DriverStatusList.FREE);
        for (int i = 0; i < driverList.size(); i++) {
            System.out.println(driverList.get(i));
        }*/
    }
}

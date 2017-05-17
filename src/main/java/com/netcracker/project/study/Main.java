package com.netcracker.project.study;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.vaadin.event.dd.acceptcriteria.Or;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        ApplicationContext ctx =
                new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        PersistenceFacade facade = (PersistenceFacade) ctx.getBean(PersistenceFacade.class);


        for (int i = 0; i < 1000; i++) {
            Client client = new Client("NEW CLIENTssssssssssss", "It's client!");
            client.setLastName("Rynkovoy");
            client.setFirstName("Anton");
            client.setMiddleName("Andreevich");
            client.setPoints(BigInteger.valueOf(50));
            facade.create(client);
        }


        /*Driver driver = new Driver("Angelica", "It's driver");
        driver.setFirstName("Grisha");
        driver.setLastName("Leps");
        driver.setHireDate(new Timestamp(System.currentTimeMillis()));
        driver.setStatus(new BigInteger("2"));
        driver.setRating(BigInteger.valueOf(3));
        facade.create(driver);

       driver.setName("Update driver");
        driver.setStatus(BigInteger.valueOf(5));
        driver.setRating(null);
        driver.setFirstName("AAAAAAAAAA");

        facade.update(driver);*/

      /* Order order = new Order("NEW ORDER");
       order.setCost(BigInteger.valueOf(1000));
       order.setStatus(Driver.FREE);
       order.setDriverId(BigInteger.valueOf(11));
       order.setDriverRating(BigInteger.valueOf(2));
       order.setDriverMemo("MEMO");
       //facade.create(order);
       order.setName("SFFSFSFSFSFFSS");
       order.setDriverId(BigInteger.valueOf(6));
       facade.update(order);*/
       /*Order order = new Order("MyOrder", "It's my order");
       order.setDriverMemo("Driver MEMO");
       order.setCost(BigInteger.valueOf(50));
       order.setDriverId(BigInteger.valueOf(11));
       order.setStatus(BigInteger.valueOf(3));
       System.out.println("PE: " + facade.create(order));

      /* Order order1 = facade.getOne(order.getObjectId(), Order.class);
       System.out.println(order1);*/


       /* List<Order> orderList = facade.getAll(BigInteger.valueOf(Order.OBJECT_TYPE_ID), Order.class);
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println(orderList.get(i).getObjectId() + ": " + orderList.get(i));
        }*/
    }
}

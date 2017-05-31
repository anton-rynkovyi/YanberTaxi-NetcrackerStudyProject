package com.netcracker.project.study;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.impl.AdminServiceImpl;
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
import java.util.Timer;
import java.util.TimerTask;

public class Main {




    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InterruptedException {
        ApplicationContext ctx =
                new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        PersistenceFacade facade = (PersistenceFacade) ctx.getBean(PersistenceFacade.class);
        AdminService adminService = ctx.getBean(AdminServiceImpl.class);
        Main main = new Main();

        Client client = new Client();
        facade.create(client);

        Order order = new Order();
        order.setDriverId(BigInteger.valueOf(115));
        order.setClientId(client.getObjectId());
        facade.create(order);
        System.out.println(order);


        List<Order> orderList = facade.getAll(BigInteger.valueOf(OrderAttr.OBJECT_TYPE_ID), Order.class);
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println(orderList.get(i));
        }


        /*Order order = facade.getOne(BigInteger.valueOf(6), Order.class);
        System.out.println(order);
*/

        /*Ser ser = new Ser();
        ser.giveBan("Task1: ", 1, 1000);*/

    }
}

/*
class Ser{
    public void giveBan(String str, int a, long speed){

        Timer timer = new Timer();


        class BanTimer extends TimerTask {

            String str;
            int a;


            public BanTimer(String str, int a) {
                this.str = str;
                this.a = a;
            }

            @Override
            public void run() {
                System.out.println(a++);
                if(a==5){
                    timer.cancel();
                }
            }
        }

        timer.scheduleAtFixedRate(new BanTimer(str, a), 0, speed);
    }

*/






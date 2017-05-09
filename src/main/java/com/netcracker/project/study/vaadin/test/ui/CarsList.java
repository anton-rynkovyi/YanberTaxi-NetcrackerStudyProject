package com.netcracker.project.study.vaadin.test.ui;

import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.manager.Manager;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
@SpringUI(path = "")
public class CarsList extends UI{

    @Autowired
    PersistenceManager manager;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        /*Main layout*/
        VerticalLayout layoutMain = new VerticalLayout();
        layoutMain.setSizeFull();
        setContent(layoutMain);

        /*List<Car> cars = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Car car = new Car();
            car.setObjectId(i);
            car.setModelType("Model: " + i);
            cars.add(car);
        }*/

       /* ConverterFactory converter = new ConverterFactory();

        Grid<Car> grid = new Grid<>();

        PersistenceFacade facade = new PersistenceFacade();
        List<PersistenceEntity> carsEntity = facade.getAll(CarAttr.OBJECT_TYPE_ID);
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            cars.add((Car) converter.convertToModel(carsEntity.get(i)));
            System.out.println("ID :" + cars.get(i).getObjectId()
                                +" | Name: " + cars.get(i).getMakeOfCar()
                                +" | Model: " + cars.get(i).getModelType());
        }


        grid.setItems(cars);
        grid.addColumn(Car::getObjectId).setCaption("Id");
        grid.addColumn(Car::getModelType).setCaption("Model");
        grid.addColumn(Car::getName).setCaption("Name");

        layoutMain.addComponent(grid);*/
    }
}

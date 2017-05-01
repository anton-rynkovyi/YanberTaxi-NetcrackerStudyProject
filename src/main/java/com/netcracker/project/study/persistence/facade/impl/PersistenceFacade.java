package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PersistenceFacade {

    @Autowired
    private PersistenceManager manager;
    @Autowired
    private ConverterFactory converter;

    public PersistenceFacade(){}

  /*public void update(Object o){
        PersistenceEntity e = converter.convertToEntity(o);
        //manager.update(e);
    }*/
}




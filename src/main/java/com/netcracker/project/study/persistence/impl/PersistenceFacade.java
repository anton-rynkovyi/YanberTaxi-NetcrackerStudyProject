package com.netcracker.project.study.persistence.impl;


public class PersistenceFacade {


    private PersistenceManager manager;
    private ConverterFactory converter;

    public PersistenceFacade(){
        manager = new PersistenceManager();
        converter = new ConverterFactory();
    }

  /*  public void update(Object o){
        PersistenceEntity e = converter.convertToEntity(o);
        //manager.update(e);
    }*/
}




package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.entity.impl.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PersistenceFacade {

    @Autowired
    private PersistenceManager manager;

    @Autowired
    private ConverterFactory converter;

    public PersistenceFacade(){}

    // ?
    public void create(Model model){
        PersistenceEntity entity = converter.convertToEntity(model);
        manager.create(entity);
    }

    public void update(Model model) {
        manager.update(converter.convertToEntity(model));
    }

    public void delete(Model model) {
        manager.delete(converter.convertToEntity(model));
    }
}




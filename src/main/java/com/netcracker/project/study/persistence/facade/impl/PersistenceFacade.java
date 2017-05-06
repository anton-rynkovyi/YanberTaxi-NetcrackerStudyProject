package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.facade.Facade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersistenceFacade implements Facade {

    @Autowired
    private PersistenceManager manager;

    @Autowired
    private ConverterFactory converter;

    public PersistenceFacade(){}


    @Override
    public PersistenceEntity create(Model model) {
        return manager.create(converter.convertToEntity(model));
    }

    @Override
    public void update(Model model) {
        manager.update(converter.convertToEntity(model));
    }

    @Override
    public void delete(long objectId) {
        //manager.delete();
    }

    @Override
    public PersistenceEntity getOne(long objectId) {
        return manager.getOne(objectId);
    }

    @Override
    public List<PersistenceEntity> getAll(int objectTypeId) {
        return getAll(objectTypeId);
    }
}




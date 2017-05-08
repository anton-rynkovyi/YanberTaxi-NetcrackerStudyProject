package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.facade.Facade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("persistenceFacade")
public class PersistenceFacade implements Facade {

    @Autowired
    private PersistenceManager manager;

    @Autowired
    private ConverterFactory converter;

    @Override
    public PersistenceEntity create(Model model) throws NoSuchFieldException, IllegalAccessException {
        //System.out.println(converter.convertToEntity(model));
        PersistenceEntity entity = manager.create(converter.convertToEntity(model));
        model.setObjectId(entity.getObjectId());
        return entity;
    }

    @Override
    public void update(Model model) throws NoSuchFieldException, IllegalAccessException {
        //System.out.println(converter.convertToEntity(model));
        manager.update(converter.convertToEntity(model));
    }

    @Override
    public void delete(long objectId) throws NoSuchFieldException, IllegalAccessException {
        manager.delete(objectId);
    }

    @Override
    public PersistenceEntity getOne(long objectId) {
        return manager.getOne(objectId);
    }

    @Override
    public List<PersistenceEntity> getAll(long objectTypeId) {
        return manager.getAll(objectTypeId);
    }

    public PersistenceManager getManager() {
        return manager;
    }

    public void setManager(PersistenceManager manager) {
        this.manager = manager;
    }

    public ConverterFactory getConverter() {
        return converter;
    }

    public void setConverter(ConverterFactory converter) {
        this.converter = converter;
    }
}




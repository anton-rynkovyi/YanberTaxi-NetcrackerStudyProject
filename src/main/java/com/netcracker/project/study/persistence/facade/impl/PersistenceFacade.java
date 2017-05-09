package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.facade.Facade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("persistenceFacade")
public class PersistenceFacade<T> implements Facade {

    @Autowired
    private PersistenceManager manager;

    @Autowired
    private ConverterFactory converter;

    @Override
    public PersistenceEntity create(Model model) throws NoSuchFieldException, IllegalAccessException {
        PersistenceEntity entity = manager.create(converter.convertToEntity(model));
        model.setObjectId(entity.getObjectId());
        return entity;
    }

    @Override
    public void update(Model model) throws NoSuchFieldException, IllegalAccessException {
        manager.update(converter.convertToEntity(model));
    }

    @Override
    public void delete(long objectId) throws NoSuchFieldException, IllegalAccessException {
        manager.delete(objectId);
    }

    @Override
    public Driver getOne(long objectId, Class modelClass) throws InstantiationException, IllegalAccessException {
        PersistenceEntity entity = manager.getOne(objectId);
        Driver model = converter.convertToModel(entity, modelClass);
        return model;
    }

    @Override
    public List<Model> getAll(long objectTypeId) throws InstantiationException, IllegalAccessException {
        List<PersistenceEntity> entities = manager.getAll(objectTypeId);
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
           Model model = converter.convertToModel(entities.get(i), Client.class);
           model.setObjectId(entities.get(i).getObjectId());
           model.setName(entities.get(i).getName());
           model.setDescription(entities.get(i).getDescription());
           models.add(model);
        }
        return models;
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




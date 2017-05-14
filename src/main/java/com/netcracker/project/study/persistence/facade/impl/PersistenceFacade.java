package com.netcracker.project.study.persistence.facade.impl;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.converter.impl.ConverterFactory;
import com.netcracker.project.study.persistence.PersistenceEntity;
import com.netcracker.project.study.persistence.facade.Facade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository("persistenceFacade")
public class PersistenceFacade implements Facade {

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
    public void delete(BigInteger objectId) throws NoSuchFieldException, IllegalAccessException {
        manager.delete(objectId);
    }

    @Override
    public <T extends Model> T getOne(BigInteger objectId, Class modelClass) throws InstantiationException, IllegalAccessException {
        PersistenceEntity entity = manager.getOne(objectId);
        T model = converter.convertToModel(entity, modelClass);
        return model;
    }

    @Override
    public List getAll(BigInteger objectTypeId, Class modelClass) throws InstantiationException, IllegalAccessException {
        List<PersistenceEntity> entities = manager.getAll(objectTypeId);
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Model model = converter.convertToModel(entities.get(i), modelClass);
            models.add(model);
        }
        return models;
    }
}




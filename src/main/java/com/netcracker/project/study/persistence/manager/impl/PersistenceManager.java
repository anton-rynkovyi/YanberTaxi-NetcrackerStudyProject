package com.netcracker.project.study.persistence.manager.impl;

import com.netcracker.project.study.persistence.entity.PersistenceEntity;
import com.netcracker.project.study.persistence.manager.Manager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistenceManager implements Manager {


    @Override
    public int create(PersistenceEntity entity) {
        return 0;
    }

    @Override
    public void update(PersistenceEntity entity) {

    }

    @Override
    public void delete(PersistenceEntity entity) {

    }

    @Override
    public PersistenceEntity getOne(int objectId) {
        return null;
    }

    @Override
    public List<PersistenceEntity> getAll(int objectTypeId) {
        return null;
    }
}

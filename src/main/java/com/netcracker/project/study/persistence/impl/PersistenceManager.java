package com.netcracker.project.study.persistence.impl;

import com.netcracker.project.study.persistence.Manager;

import java.util.List;

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

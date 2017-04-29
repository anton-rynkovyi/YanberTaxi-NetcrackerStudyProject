package com.netcracker.project.study.persistence;

import com.netcracker.project.study.persistence.impl.PersistenceEntity;

import java.util.List;

public interface Manager {

    int create(PersistenceEntity entity);

    void update(PersistenceEntity entity);

    void delete(PersistenceEntity entity);

    PersistenceEntity getOne(int objectId);

    List<PersistenceEntity> getAll(int objectTypeId);

}

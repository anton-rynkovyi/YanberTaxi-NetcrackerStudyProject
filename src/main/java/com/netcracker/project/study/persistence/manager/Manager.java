package com.netcracker.project.study.persistence.manager;

import com.netcracker.project.study.persistence.entity.impl.PersistenceEntity;

import java.util.List;

public interface Manager {

    int create(PersistenceEntity entity);

    void update(PersistenceEntity entity);

    void delete(PersistenceEntity entity);

    PersistenceEntity getOne(int objectId);

    List<PersistenceEntity> getAll(int objectTypeId);

}

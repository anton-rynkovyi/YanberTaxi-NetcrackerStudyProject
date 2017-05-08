package com.netcracker.project.study.persistence.manager;

import com.netcracker.project.study.persistence.PersistenceEntity;

import java.util.List;

public interface Manager {

    PersistenceEntity create(PersistenceEntity entity);

    void update(PersistenceEntity entity);

    void delete(long objectId);

    PersistenceEntity getOne(long objectId);

    List<PersistenceEntity> getAll(long objectTypeId);

}

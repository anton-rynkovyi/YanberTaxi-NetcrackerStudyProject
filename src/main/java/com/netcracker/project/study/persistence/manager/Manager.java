package com.netcracker.project.study.persistence.manager;

import com.netcracker.project.study.persistence.PersistenceEntity;

import java.math.BigInteger;
import java.util.List;

public interface Manager {

    PersistenceEntity create(PersistenceEntity entity);

    void update(PersistenceEntity entity);

    void delete(BigInteger objectId);

    PersistenceEntity getOne(BigInteger objectId);

    List<PersistenceEntity> getAll(BigInteger objectTypeId);

    List<PersistenceEntity> getSome(String sqlQuery);

}

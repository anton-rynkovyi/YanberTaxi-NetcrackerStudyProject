package com.netcracker.project.study.persistence.facade;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.PersistenceEntity;

import java.math.BigInteger;
import java.util.List;

public interface Facade{

    PersistenceEntity create(Model model);

    void update(Model model);

    void delete(BigInteger objectId);

    <T extends Model> T getOne(BigInteger objectId, Class modelClass);

    List<? extends Model> getAll(BigInteger objectTypeId, Class modelClass);

    List<? extends Model> getSome(String sqlQuery, Class modelClass);
}

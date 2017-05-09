package com.netcracker.project.study.persistence.facade;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.persistence.PersistenceEntity;

import java.util.List;

public interface Facade{

    PersistenceEntity create(Model model) throws NoSuchFieldException, IllegalAccessException;

    void update(Model model) throws NoSuchFieldException, IllegalAccessException;

    void delete(long objectId) throws NoSuchFieldException, IllegalAccessException;

    <T extends Model> T getOne(long objectId, Class modelClass) throws InstantiationException, IllegalAccessException;

    List<? extends Model> getAll(long objectTypeId, Class modelClass) throws InstantiationException, IllegalAccessException;
}

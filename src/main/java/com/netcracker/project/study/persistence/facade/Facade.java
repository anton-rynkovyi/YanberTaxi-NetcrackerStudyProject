package com.netcracker.project.study.persistence.facade;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.PersistenceEntity;

import java.util.List;

public interface Facade{

    PersistenceEntity create(Model model) throws NoSuchFieldException, IllegalAccessException;

    void update(Model model) throws NoSuchFieldException, IllegalAccessException;

    void delete(long objectId) throws NoSuchFieldException, IllegalAccessException;

    Model getOne(long objectId);

    List<Model> getAll(long objectTypeId);
}

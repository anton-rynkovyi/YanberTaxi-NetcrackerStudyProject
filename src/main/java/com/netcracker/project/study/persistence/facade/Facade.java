package com.netcracker.project.study.persistence.facade;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.PersistenceEntity;

import java.util.List;

public interface Facade{

    PersistenceEntity create(Model model);

    void update(Model model);

    void delete(long objectId);

    PersistenceEntity getOne(long objectId);

    List<PersistenceEntity> getAll(int objectTypeId);
}

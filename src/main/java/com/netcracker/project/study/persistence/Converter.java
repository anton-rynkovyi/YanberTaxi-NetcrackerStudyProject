package com.netcracker.project.study.persistence;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.impl.PersistenceEntity;



public interface Converter {

    PersistenceEntity convertToEntity(Model model);

    Object convertToModel(PersistenceEntity entity);
}

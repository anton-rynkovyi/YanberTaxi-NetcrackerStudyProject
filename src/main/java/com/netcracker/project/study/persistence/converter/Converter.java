package com.netcracker.project.study.persistence.converter;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.entity.PersistenceEntity;



public interface Converter {

    PersistenceEntity convertToEntity(Model model);

    Object convertToModel(PersistenceEntity entity);
}

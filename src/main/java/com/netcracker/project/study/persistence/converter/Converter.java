package com.netcracker.project.study.persistence.converter;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.PersistenceEntity;



public interface Converter {

    PersistenceEntity convertToEntity(Model model) throws IllegalAccessException, InstantiationException, NoSuchFieldException;

    Object convertToModel(PersistenceEntity entity);
}

package com.netcracker.project.study.persistence.converter.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.converter.Converter;
import com.netcracker.project.study.persistence.entity.impl.PersistenceEntity;

public class ConverterFactory implements Converter {

    private PersistenceEntity entity;

    public ConverterFactory(){this(new PersistenceEntity());}

    public ConverterFactory(PersistenceEntity entity){this.entity = entity;}

    @Override
    public PersistenceEntity convertToEntity(Model model) {
        return null;
    }

    @Override
    public Model convertToModel(PersistenceEntity entity) {
        return null;
    }
}

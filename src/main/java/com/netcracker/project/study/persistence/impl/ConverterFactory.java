package com.netcracker.project.study.persistence.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class ConverterFactory implements Converter {


    @Autowired
    private PersistenceEntity entity;

    public ConverterFactory(){}

    @Override
    public PersistenceEntity convertToEntity(Model model) {
        return null;
    }

    @Override
    public Model convertToModel(PersistenceEntity entity) {
        return null;
    }
}

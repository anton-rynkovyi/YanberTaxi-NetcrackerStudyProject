package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.driver.Driver;

import java.math.BigInteger;
import java.util.List;

public interface AdminService {

    boolean isVerificate(Driver driver);

    void showTracking();

    void giveBan(Driver driver, int days);

    <T extends Model> List<T> allModelsAsList(Class modelClass);

    BigInteger deleteModel(Model model);

    <T extends Model> T createModel(Model model);

    void updateModel(Model model);

    void unbanDriver(Driver driver);

    void setBanTask();

    <T extends Model> T getModelById(BigInteger modelId, Class modelClass);

    void fireDriver(Driver driver);
}

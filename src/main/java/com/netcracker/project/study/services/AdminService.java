package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.driver.Driver;

import java.math.BigInteger;
import java.util.List;

public interface AdminService {

    boolean isVerificate(Driver driver);

    void showTracking();

    void showDriverInfo(Driver driver);

    void giveBan(Driver driver);

    <T extends Model> List<T> allModelsAsList(Class modelClass);

    BigInteger deleteModel(Model model);

    <T extends Model> T createModel(Model model);

    void updateModel(Model model);
}

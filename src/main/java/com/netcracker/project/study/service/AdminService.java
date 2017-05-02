package com.netcracker.project.study.service;

import com.netcracker.project.study.model.driver.Driver;

public interface AdminService {

    boolean isVerificate(Driver driver);

    void showTracking();

    void showDriverInfo(Driver driver);

    void giveBan(Driver driver);
}

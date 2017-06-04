package com.netcracker.project.study.services.tools;

import com.netcracker.project.study.services.AdminService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class BanJob extends QuartzJobBean {

    @Autowired
    AdminService adminService;

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        adminService.setBanTask();
    }
}
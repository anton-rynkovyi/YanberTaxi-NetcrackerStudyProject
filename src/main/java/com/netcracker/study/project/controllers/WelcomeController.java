package com.netcracker.study.project.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Anton Rynkovoy on 04/12/2017.
 */

@Controller
@RequestMapping("/")
public class WelcomeController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}

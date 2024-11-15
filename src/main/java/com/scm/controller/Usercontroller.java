package com.scm.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class Usercontroller {
    private Logger logger= LoggerFactory.getLogger(Usercontroller.class);

    @Autowired
    private UserService userService;


    //dashboard
    @RequestMapping(value = "/dashboard",method = RequestMethod.GET)
    public String userDashboard() {
        return "user/dashboard";
    }



    //user profile
    @RequestMapping(value = "/profile", method = {RequestMethod.GET, RequestMethod.POST})
    public String userProfile(Model model,Authentication authentication) {

        return "user/profile";
    }

    //add contact
    //view contact
    //edit
    //deltere

}

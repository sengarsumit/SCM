package com.scm.controller;


import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

    private Logger logger=org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUseInformation(Model model, Authentication authentication) {
        if(authentication==null)
        {
            return;
        }
        System.out.println("Added logged in information to the model");
        String username= Helper.getEmailOfLoggedInUser(authentication);
        logger.info("user logged in: {}",username);
        User user= userService.getUserByEmail(username);
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        model.addAttribute("loggedInUser",user);
    }
}

package com.scm.controller;


import com.scm.entities.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("verify-email")
    public  String verifyEmail(@RequestParam("token") String token, HttpSession session)
    {
       User user= userService.getUserByEmailToken(token);
        if (user == null) {
            session.setAttribute("message",Message.builder().type(MessageType.red).content("email not verified, token not associated with user").build());
            return "error_page"; // Redirect to error page if user is not found

        }
       if(user.getEmailToken().equals(token))
       {
           user.setEnabled(true);
           user.setEmailVerified(true);
           userRepo.save(user);
           session.setAttribute("message",Message.builder().type(MessageType.green).content("email verified, Login with email and password").build());
           return "success_page";
       }
        return "error_page";
    }
}

package com.scm.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class SessionHelper {
    public  static void removeMessage(){
        try
        {
            System.out.println("removeMessage from session");
            HttpSession session= ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
            session.removeAttribute("message");
        }
        catch(Exception e)
        {
            System.out.println("error in session helper :"+ e.getMessage());
            e.printStackTrace();
        }
    }
}

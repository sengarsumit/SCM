package com.scm.controller;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model)
    {
        System.out.println("home page handler");
        model.addAttribute("name","substring tech");
        model.addAttribute("youtubeChannel","learn cdode");
        model.addAttribute("githubRepo","sumitsengar");
        return "home";
    }
    @RequestMapping("/about")
    public String aboutPage()
    {
        System.out.println("about page loading");
        return "about";
    }
    @RequestMapping("/services")
    public String servicesPage()
    {
        System.out.println("services page loading");
        return "services";
    }
    @GetMapping("/contact")
    public String contact()
    {
        return new String("contact");
    }
    @GetMapping("/login")
    public String login()
    {
        return new String("login");
    }
    @GetMapping("/register")
    public String register(Model model)
    {

        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    //register
    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult ,HttpSession session)

    {
        if(rBindingResult.hasErrors())
        {
            return "register";
        }
//        User user=User.builder().name(userForm.getName()).email(userForm.getEmail()).password(userForm.getPassword()).about(userForm.getAbout()).phoneNumber(userForm.getPhoneNumber()).profilePic("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png").build();
        User user=new User();
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png");

        userService.saveUser(user);
        System.out.println("user saved");
        Message message=Message.builder().content("Registration sucessful").type(MessageType.green).build();
        session.setAttribute("message",message);


        return "redirect:register";
    }

}

package com.scm.controller;


import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/user/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    private Logger logger=org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String addContactView(Model model)
    {
        ContactForm contactForm =new ContactForm();
        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }
    @PostMapping("/add")
    public String saveContact(@Valid  @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession session)
    {
        if(bindingResult.hasErrors())
        {
            session.setAttribute("message",Message.builder().content("please correct the following errors").type(MessageType.red).build());
            return "user/add_contact";
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(username);
        String filename= UUID.randomUUID().toString();

        String fileURL=imageService.uploadImage(contactForm.getContactImage(),filename);



        Contact contact =new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setUser(user);
        contact.setDescription(contactForm.getDescription());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        contactService.saveContact(contact);
        System.out.println(contactForm);
        session.setAttribute("message",Message.builder()
                .content("Your contact has been saved")
                .type(MessageType.green).build());

        return "redirect:/user/contact/add";
    }
}

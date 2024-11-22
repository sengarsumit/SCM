package com.scm.controller;


import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @RequestMapping
    public String viewContact(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"") int size,@RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
                              @RequestParam (value = "direction",defaultValue = "asc") String direction,Authentication authentication, Model model)
    {
        String username=Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(username);
        Page<Contact> PageContact=contactService.getByUser(user,page,size,sortBy,direction);
        model.addAttribute("PageContact",PageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        PageContact.getNumber();
        model.addAttribute("ContactSearchForm",new ContactSearchForm());

        return "user/contact";
    }

    //search handler
    @RequestMapping("/search")
    public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm, @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"")int size,
                                @RequestParam(value = "page",defaultValue = "0")int page, @RequestParam(value = "sortBy",defaultValue = "name") String sortBy, @RequestParam(value = "direction",defaultValue = "asc") String direction, Model model, Authentication authentication)
    {

        var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name"))
        {
        pageContact= contactService.searchByName(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email"))
        {
            pageContact=contactService.searchByEmail(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("phone"))
        {
            pageContact=contactService.searchByPhoneNumber(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }
        model.addAttribute("PageContact",pageContact);
        model.addAttribute("ContactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/search";
    }
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId,HttpSession session)
    {
        contactService.deleteContact(contactId);
        logger.info("contact deleted " + contactId);
        session.setAttribute("message",Message.builder().content("contact deleted").type(MessageType.green).build());

        return "redirect:/user/contact";

    }


    //view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId, Model model, Authentication authentication){

        var contact=contactService.getContactById(contactId);
        ContactForm contactForm=new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("ContactForm",contactForm);
        model.addAttribute("contactId",contactId);

        return "user/update_contact_view";

    }

    @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
    public String updateContact(@PathVariable String contactId,@Valid @ModelAttribute  ContactForm contactForm, Model model, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "user/update_contact_view";
        }

        var con= contactService.getContactById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setLinkedinLink(contactForm.getLinkedinLink());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setFavorite(contactForm.isFavorite());
        con.setPicture(contactForm.getPicture());
        //image
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty())
        {
            String fileName=UUID.randomUUID().toString();
            String imageUrl=imageService.uploadImage(contactForm.getContactImage(),fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }

        var updateCon=contactService.updateContact(con);
        logger.info("contact updated " + updateCon);
        model.addAttribute("message",Message.builder().content("contact updated").type(MessageType.green).build());
        return "redirect:/user/contact/view/" + contactId;
    }

}

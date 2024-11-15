package com.scm.services.impl;

import com.scm.entities.Contact;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;
    @Override
    public Contact saveContact(Contact contact) {
        String contactID= UUID.randomUUID().toString();
        contact.setId(contactID);
        return contactRepo.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("contact Not Found with given id"+id));
    }

    @Override
    public void deleteContact(String id) {
        var contact=getContactById(id);
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> searchContact(String name, String email, String phone) {
        return List.of();
    }

    @Override
    public List<Contact> getByUserid(String userid) {

       return contactRepo.findByUserId(userid);
    }
}

package com.scm.services;

import com.scm.entities.Contact;

import java.util.List;

public interface ContactService {
    Contact saveContact(Contact contact);
    Contact updateContact(Contact contact);
    List<Contact> getAllContacts();
    Contact getContactById(String id);
    void deleteContact(String id);
    List<Contact> searchContact(String name,String email,String phone);
    List<Contact> getByUserid(String userid);

}

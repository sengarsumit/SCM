package com.scm.services.impl;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
       var contactOld=contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("contact not found"));

       contactOld.setName(contact.getName());
       contactOld.setEmail(contact.getEmail());
       contactOld.setAddress(contact.getAddress());
       contactOld.setPhoneNumber(contact.getPhoneNumber());
       contactOld.setWebsiteLink(contact.getWebsiteLink());
       contactOld.setLinkedinLink(contact.getLinkedinLink());
       contactOld.setDescription(contact.getDescription());
       contactOld.setFavorite(contact.isFavorite());


       return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("contact Not Found with given id" +id));
    }

    @Override
    public void deleteContact(String id) {
        var contact=getContactById(id);
        contactRepo.delete(contact);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort=order.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndNameContaining(user,nameKeyword,pageable);

    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort=order.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword,pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort=order.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndEmailContaining(user,emailKeyword,pageable);
    }


    @Override
    public List<Contact> getByUserid(String userid) {

       return contactRepo.findByUserId(userid);
    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
        Sort sort=direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable= PageRequest.of(page,size,sort);
         return contactRepo.findByUser(user,pageable);
    }
}

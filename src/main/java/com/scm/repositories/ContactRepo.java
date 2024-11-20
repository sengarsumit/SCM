package com.scm.repositories;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
    Page<Contact> findByUser(User user, Pageable pageable);
    @Query("select c from Contact c WHERE c.user.userId = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
    Page<Contact> findByUserAndNameContaining( User user,String nameKeyword, PageRequest pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user,String phoneKeyword,  PageRequest pageable);
    Page<Contact> findByUserAndEmailContaining( User user,String  emailKeyword, PageRequest pageable);

}

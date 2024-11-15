package com.scm.repositories;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
    List<Contact> findByUser(User user);
    @Query("select c from Contact c WHERE c.user.id= :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
}

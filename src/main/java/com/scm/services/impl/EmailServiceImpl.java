package com.scm.services.impl;

import com.scm.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String DoaminName;

    @Override
    public void sendEmail(String to, String subject, String body) {


      SimpleMailMessage message=  new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);
      message.setFrom(DoaminName);
      emailSender.send(message);
    }

    @Override
    public void sendEmailWithHtml() {

    }

    @Override
    public void sendEmailWithAttachment() {

    }
}

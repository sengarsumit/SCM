package com.scm.config;

import com.scm.entities.User;
import com.scm.entities.providers;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.bootstrap.ProviderSpecificBootstrap;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfShort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger= LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    @Autowired
    private UserRepo userRepo;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Authentication success");


         var oAuth2AuthenticationToken=(OAuth2AuthenticationToken) authentication;
         String authorizedClientRegistrationId=oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

         logger.info(authorizedClientRegistrationId);

         var oathUser=(DefaultOAuth2User) authentication.getPrincipal();
         oathUser.getAttributes().forEach((key,value)->{
             logger.info(key+":"+value);
         });
         User user=new User();
         user.setUserId(UUID.randomUUID().toString());
         user.setRoleList(List.of(AppConstants.ROLE_USER));
         user.setEmailVerified(true);
         user.setEnabled(true);
         user.setPassword("password");

         if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
             user.setEmail(oathUser.getAttribute("email").toString());
             user.setProfilePic(oathUser.getAttribute("picture").toString());
             user.setName(oathUser.getAttribute("name").toString());
             user.setProviderUserId(oathUser.getName());
             user.setProvider(providers.GOOGLE);
             user.setAbout("created using google");
         }
         else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
             String email=oathUser.getAttribute("email")!=null?oathUser.getAttribute("email").toString():oathUser.getAttribute("login").toString()+"@scm.com";
             String picture=oathUser.getAttribute("avatar_url").toString();
             String name=oathUser.getAttribute("login").toString();
             String providerUserId=oathUser.getName();
             user.setEmail(email);
             user.setProfilePic(picture);
             user.setName(name);
             user.setProviderUserId(providerUserId);
             user.setProvider(providers.GITHUB);
             user.setAbout("created using github");
         }



        /*
       DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
//       logger.info(user.getName());
//
//       user.getAttributes().forEach((key,value)->{logger.info("{}=>{}",key,value);});
//       logger.info(user.getAuthorities().toString());
        // save to database
        String email=user.getAttribute("email");
        String name=user.getAttribute("name");
        String picture=user.getAttribute("picture");
        //create and save user
        User user1=new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProvider(providers.GOOGLE);
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("account created using google account");


*/
        User user2= userRepo.findByEmail(user.getEmail()).orElse(null);
        if(user2==null){
            userRepo.save(user);
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}

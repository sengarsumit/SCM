package com.scm.helper;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;


@Component
public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication) {


        if(authentication instanceof OAuth2AuthenticationToken)
        {
            var aOAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            var clientId=aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2User=(OAuth2User)authentication.getPrincipal();
            String username="";
            if(clientId.equalsIgnoreCase("google")){
                System.out.println("getting email from google");
                username=oauth2User.getAttribute("email").toString();
            }
            else if(clientId.equalsIgnoreCase("github")){
                System.out.println("getting email from github");
                username=oauth2User.getAttribute("email")!=null?oauth2User.getAttribute("email").toString():oauth2User.getAttribute("login").toString()+"@scm.com";
            }
            return username;
        }
        else {

            return authentication.getName();
        }

    }
}

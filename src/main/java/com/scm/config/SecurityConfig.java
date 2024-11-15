package com.scm.config;


import com.scm.services.impl.SecurityCustomUserDeatilService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDeatilService userDeatilService;
    @Autowired
    private OAuthAuthenticationSuccessHandler handler;
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        //passing object of service and password
        daoAuthenticationProvider.setUserDetailsService(userDeatilService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/login", "/authenticate").permitAll(); // Allow access to login page and authentication URL
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();


        });


        // form default login
        // agar hame kuch bhi change karna hua to hama yaha ayenge: form login se
        // related
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login"); // Where users go to see the login form
            formLogin.loginProcessingUrl("/authenticate"); // Where login submissions are processed
            formLogin.successForwardUrl("/user/profile");
            formLogin.defaultSuccessUrl("/user/profile",true);// Redirect on success
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");


//            formLogin.defaultSuccessUrl("/user/dashboard");
//            formLogin.failureHandler(new AuthenticationFailureHandler() {
//                @Override
//                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                    throw new UnsupportedOperationException("unimplemented method");
//                }
//            });
//            formLogin.successHandler(new AuthenticationSuccessHandler() {
//                @Override
//                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                    throw new UnsupportedOperationException("unimplemented method");
//                }
////            });

        });
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutform->{logoutform.logoutUrl("/logout");
        logoutform.logoutSuccessUrl("/login?logout=true");});

        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

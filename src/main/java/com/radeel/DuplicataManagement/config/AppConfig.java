package com.radeel.DuplicataManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.radeel.DuplicataManagement.service.UserManager;

@Configuration
public class AppConfig {
  
  @Bean
  UserDetailsService userDetailsService(){
    return new UserManager();
  }

  @Bean 
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
    dao.setUserDetailsService(userDetailsService());
    dao.setPasswordEncoder(passwordEncoder());
    return dao;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration conf) throws Exception{
    return conf.getAuthenticationManager();
  }
}

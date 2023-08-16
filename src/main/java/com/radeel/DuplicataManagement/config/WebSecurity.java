package com.radeel.DuplicataManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.radeel.DuplicataManagement.model.Role;

@Configuration
@EnableWebSecurity
public class WebSecurity {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http
      .authorizeHttpRequests(auth ->
      auth
        .requestMatchers(
          "/create_admin",
           "/create_client",
           "/list_users",
           "/modify_client"
        ).hasAuthority(Role.ADMIN.name())
        .requestMatchers(
          "/verify_client"
        ).hasAuthority(Role.CLIENT.name())
        .requestMatchers("/**").permitAll()
      )
      .formLogin(log -> 
      log
        .loginPage("/login")
        .permitAll()
        .defaultSuccessUrl("/create_client")
        .failureUrl("/login?error=1")
        )
      .logout(log -> 
      log
        .logoutUrl("/logout")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .logoutSuccessUrl("/login")
      );
    return http.build();
  }
}

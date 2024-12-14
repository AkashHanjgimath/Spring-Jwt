package com.springJwt.practice;

import com.springJwt.practice.model.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService myUserDetailService;

    public DefaultSecurityFilterChain securityConfiguration(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry->{
                            registry.requestMatchers("/home", "/register/**", "/authenticate").permitAll();
                            registry.requestMatchers("/admin/**").hasRole("ADMIN");
                            registry.requestMatchers("/user/**").hasRole("USER");
                            registry.anyRequest().authenticated();
                        })
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .build();
    }

    // interface  used to retrieve user details  from database,for authentication
    @Bean
    public UserDetailsService userDetailsService()
    {
        return myUserDetailService;
    }

   //DaoAuthenticationProvider implementation of AuthenticationProvider
   // that retrieves user details from a database (via a UserDetailsService),
   // typically used for authenticating users against a database. It compares the username and password and performs authentication based on that.
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provoider=new DaoAuthenticationProvider();
        provoider.setUserDetailsService(userDetailsService());
        provoider.setPasswordEncoder(passwordEncoder());
        return provoider;
    }

    @Bean
    public AuthenticationManager authenticationManager()
    {
        return new ProviderManager(authenticationProvider());
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

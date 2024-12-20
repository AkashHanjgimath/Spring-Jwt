package com.springJwt.practice;

import com.springJwt.practice.model.MyUserDetailService;
import com.springJwt.practice.webtoken.JwtAuthenticationFilter;
import com.springJwt.practice.webtoken.JwtService;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry->{
                            registry.requestMatchers("/home", "/register/**", "/authenticate").permitAll();
                            registry.requestMatchers("/admin/**").hasRole("ADMIN");
                            registry.requestMatchers("/user/**").hasRole("USER");
                            registry.anyRequest().authenticated();
                        })
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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
        provoider.setUserDetailsService(myUserDetailService);
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

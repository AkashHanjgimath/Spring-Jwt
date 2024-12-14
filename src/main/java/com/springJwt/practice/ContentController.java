package com.springJwt.practice;

import com.springJwt.practice.model.MyUserDetailService;
import com.springJwt.practice.webtoken.JwtService;
import com.springJwt.practice.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    MyUserDetailService myUserDetailService;


    @GetMapping("/home")
    public String handleWelcome() {
        return "Welcome to home!";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "Welcome to ADMIN home!";
    }

    @GetMapping("/user/home")
    public String handleUserHome() {
        return "Welcome to USER home!";
    }

    @GetMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm)
    {
       Authentication authentication= authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(),loginForm.password()));

       if(authentication.isAuthenticated())
       {
           return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
       }
       else {
           throw new UsernameNotFoundException("Invalid credentials");
       }

    }

}

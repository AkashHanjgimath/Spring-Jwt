package com.springJwt.practice.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      Optional<MyUser>user= myUserRepository.findByUsername(username);
      if(user.isPresent())
      {
          var userobj=user.get();
         return User.builder()
                  .username(userobj.getUsername())
                  .password(userobj.getPassword())
                  .roles(getRoles(userobj))
                  .build();
      }
      else{
          throw new UsernameNotFoundException(username);

      }

    }

    private String[] getRoles(MyUser user) {
        if(user.getRole()==null)
        {
            return new String[]{"USER"};

        }else {
            return user.getRole().split(",");
        }

    }
}

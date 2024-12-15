package com.springJwt.practice.webtoken;

import com.springJwt.practice.model.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

@Autowired
private JwtService jwtService;

@Autowired
private MyUserDetailService myUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //This fetches the Authorization header from the incoming HTTP request.
        String authHeader=request.getHeader("Authorization");

        //If the header is missing (null) or does not start with "Bearer ", the filter skips
        // further processing and passes the request to the next filter in the chain.
        if(authHeader==null||!authHeader.startsWith("Bearer "))
        {
            //The filterChain.doFilter(request, response) ensures that the request
            // is forwarded to the next filter in the security chain, maintaining the request flow.
            filterChain.doFilter(request,response);
            return;
        }

        String jwt=authHeader.substring(7);
        String username=jwtService.extractName(jwt);
        if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null)
        {
         UserDetails userDetails= myUserDetailService.loadUserByUsername(username);
         if(userDetails!=null||jwtService.isTokenValid(jwt))
         {
             UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                     username,
                     userDetails.getPassword(),
                     userDetails.getAuthorities()
             );
             authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(authenticationToken);
         }
        }
        filterChain.doFilter(request,response);


    }
}

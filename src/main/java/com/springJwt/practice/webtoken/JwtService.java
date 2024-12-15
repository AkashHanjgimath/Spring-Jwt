package com.springJwt.practice.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class JwtService {

    private static final String SECRET="93B054390BBC38E35504523929E5D71B0BC7E56604071730FEAF74503CD1EAFC05BED680C8E8FA1AEEA6648953043509CD13C5A2005173571FE5DDE8D52135E6";
    private static final long VALIDITY= TimeUnit.MINUTES.toMillis(30);


    public String generateToken(UserDetails userDetails)
    {
        Map<String,String>claims=new HashMap<>();
        claims.put("iss","https://security.dummy.com");


      return   Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();

    }

    private SecretKey generateKey() {
      byte[]decodedKey= Base64.getDecoder().decode(SECRET);
      return Keys.hmacShaKeyFor(decodedKey);


    }

    public String extractName(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        Claims claims= Jwts.parser()
                   .verifyWith(generateKey())
                   .build()
                   .parseSignedClaims(jwt)
                   .getPayload();
        return claims;
    }

    public boolean isTokenValid(String jwt) {
    Claims claims=getClaims(jwt);
    return claims.getExpiration().after(Date.from(Instant.now()));
    }
}

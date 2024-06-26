package com.example.desafiofinal.Auth.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final String SECRET_KEY="586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
    public String getToken(UserDetails user){
        return getToken(new HashMap<>(),user);
    }
    private  String getToken(Map<String,Object>extraClaims,UserDetails user)
    {
        Set<String> roles= user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        extraClaims.put("rol", roles);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                //.setSubject(String.valueOf(user.getAuthorities()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    Key getKey()
    {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token)
    {
        return getClaim(token, Claims::getSubject);
    }

    private Claims getAllClaims(String token)
    {
        return  Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    boolean isTokenExpired(String token)
    {
        return  getExpiration(token).before(new Date());
    }
}

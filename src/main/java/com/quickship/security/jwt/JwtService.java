package com.quickship.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quickship.security.model.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {
	
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );

    }
    
    
    public String generateToken(UserDetails userDetails)
    {
    	Map<String, Object> claims = new HashMap<>();
    	CustomUserDetails customUser = (CustomUserDetails)userDetails;
    	claims.put("role", customUser.getUser().getRole().name());
    	
    	return Jwts.builder()
    			.subject(userDetails.getUsername())
    			.claims(claims)
    			.issuedAt(new Date())
    			.expiration(new Date(System.currentTimeMillis()+jwtExpiration))
    			.signWith(getSigningKey())
                .compact();
    }
    
    private Claims extractAllClaims(String token) {

    	return Jwts
    			.parser()
    			.verifyWith(getSigningKey())
    			.build()
    			.parseSignedClaims(token)
    			.getPayload();
    }
    
    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) 
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public String extractUserName(String token)
    {
    	return extractClaim(token,Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    private boolean isTokenExpired(String token)
    {
    	return extractExpiration(token).before(new Date());
    }
    
    
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
    	final String userName=extractUserName(token);
    	return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}

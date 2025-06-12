package com.connect.eduHub.secutiry;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
//private String secretKey = "";
	
	@Value("${jwt.secret}")
    private String secretKey;
	
	
//	public JWTService() {
//		try {
//			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//			SecretKey sk = keyGen.generateKey();
//			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//		}catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	    public String generateToken(UserDetails userDetails) {
	        return Jwts.builder()
	                .setSubject(userDetails.getUsername())
	                .setIssuedAt(new Date())
	                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
	                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(secretKey.getBytes())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

	    private boolean isTokenExpired(String token) {
	        Date expiration = Jwts.parserBuilder()
	                .setSigningKey(secretKey.getBytes())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getExpiration();
	        return expiration.before(new Date());
	    }

}

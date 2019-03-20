package com.jz.nebula.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	private Key secretKey;
	
	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h
	
  @Autowired
  private RedisTemplate<String, String> template;
  
	@Autowired
	private UserDetailsService userDetailsService;
	
	@PostConstruct
	protected void init() {
		secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }
    
	public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        String token = Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
//            .setExpiration(validity)//
            .signWith(secretKey)//
            .compact();
        template.opsForValue().set(username, token);
        
        return token;
    }
		
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            
            String username = claims.getBody().getSubject();
            String redisToken = template.opsForValue().get(username);
            
            if(redisToken != null && !token.equals(redisToken)) {
            	return false;
            }
            
            System.out.println("Token equal");
            
            template.opsForValue().set(username, token, 15, TimeUnit.MINUTES);;
            
//            if (claims.getBody().getExpiration().before(new Date())) {
//                return false;
//            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
        		e.printStackTrace();
//            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        		return false;
        }
    }
}

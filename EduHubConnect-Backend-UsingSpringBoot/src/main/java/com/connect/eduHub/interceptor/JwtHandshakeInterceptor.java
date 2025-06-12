package com.connect.eduHub.interceptor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.connect.eduHub.secutiry.CustomUserDetailsService;
import com.connect.eduHub.secutiry.JWTService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.socket.WebSocketHandler;


@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	  private final JWTService jwtService;
	    private final CustomUserDetailsService userDetailsService;

	    public JwtHandshakeInterceptor(JWTService jwtService, CustomUserDetailsService userDetailsService) {
	        this.jwtService = jwtService;
	        this.userDetailsService = userDetailsService;
	    }

	    @Override
	    public boolean beforeHandshake(ServerHttpRequest request,
	                                   ServerHttpResponse response,
	                                   WebSocketHandler wsHandler,
	                                   Map<String, Object> attributes) {

	        if (request instanceof ServletServerHttpRequest servletRequest) {
	            String token = servletRequest.getServletRequest().getParameter("token");
	            if (token != null && !token.isEmpty()) {
	                try {
	                    String username = jwtService.extractUsername(token);
	                    System.out.println("JWT Authenticated User: " + username); 
	                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	                    if (jwtService.validateToken(token, userDetails)) {
	                        attributes.put("username", username);
	                        return true;
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace(); // or log
	                }
	            }else {
	            	 System.out.println("Missing or empty token from WebSocket request");
	            }
	        }
	        System.out.println("JWT authentication failed for WebSocket request");
	        return false; // token invalid or missing
	    }

	    @Override
	    public void afterHandshake(ServerHttpRequest request,
	                               ServerHttpResponse response,
	                               WebSocketHandler wsHandler,
	                               Exception exception) {
	        // nothing needed here
	    }
	   
}

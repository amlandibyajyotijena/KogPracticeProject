package com.example.demo.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//3
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter{
	
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//fetch token from request
		
		var jwtTokenOptional=getTokenFromRequest(request);
		
		jwtTokenOptional.ifPresent(jwtToken->{
			if(JwtUtils.validateToken(jwtToken)) {
				//get username from jwttoken
			Optional<String> usernameoptional=JwtUtils.getUsernameFromToken(jwtToken);
			
			//fetch userdetails with the help of username
			var userDetails=userDetailsService.loadUserByUsername(usernameoptional.get());
			
			//Create Auth Token
var authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			//set auth token to security context
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
				
				);
		//pass request and response to next filter
		filterChain.doFilter(request, response);
		
	}

	private Optional<String> getTokenFromRequest(HttpServletRequest request) {
		//extrct auth header
		var authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		//BEARER <JWT token>
	  	if(StringUtils.hasText(authHeader)&&authHeader.startsWith("Bearer ")) {
	  		return Optional.of(authHeader.substring(7));
	  	}
		return Optional.empty();
	}

}

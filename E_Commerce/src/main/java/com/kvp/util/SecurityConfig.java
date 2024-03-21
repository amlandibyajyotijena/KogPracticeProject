package com.kvp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kvp.securities.JWTFilter;
import com.kvp.service.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JWTFilter jwtFilter;
	@Autowired
    private  AuthenticationEntryPoint authenticationEntryPoint;

	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
         // Disable CORS and CSRF
         .csrf(csrf -> csrf.disable())
         // Configure exception handling
         .exceptionHandling(handling -> {
				try {
					handling.authenticationEntryPoint(authenticationEntryPoint).and()
					// Set session creation policy to STATELESS
					.sessionManagement(management -> {
						try {
							management.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
							// Configure HTTP security
							.authorizeHttpRequests(requests -> requests
							        // Permit all requests
							        .requestMatchers("/api/register","/api/login/**").permitAll()
							        // Endpoints that require authentication can be configured here
							        // .antMatchers("/api/**").authenticated()
							        .anyRequest().authenticated());
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

 // Add JWT authentication filter
 http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

 return http.build();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsServiceImpl);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
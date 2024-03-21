package com.example.demo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.AppUser;
import com.example.demo.repo.AppUserRepo;
import com.example.demo.util.JwtUtils;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AppUserRepo arepo;

	@Override
	public String login(String username, String password) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		Authentication authenticate = authenticationManager.authenticate(authToken);

		String uname = ((UserDetails) (authenticate.getPrincipal())).getUsername();

		return JwtUtils.generateToken(uname);

	}

	@Override
	@Transactional
	public String signup(String name, String username, String password) {
		// Check if user already exists
		if (arepo.existsByUsername(username)) {
			throw new RuntimeException("User already exists with username: " + username);
		}

		// Encode password
		String encodedPassword = passwordEncoder.encode(password);

		// Create authorities
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")
				,new SimpleGrantedAuthority("ROLE_USER"));

		// Create app user
		AppUser appUser = AppUser.builder().name(name).username(username).password(encodedPassword)
				.authorities(authorities).build();

		// Save user to the database
		arepo.save(appUser);

		// Generate JWT token
		return JwtUtils.generateToken(username);
	}

}

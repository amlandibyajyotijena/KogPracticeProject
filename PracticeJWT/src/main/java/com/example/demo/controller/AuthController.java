package com.example.demo.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.AuthStatus;
import com.example.demo.model.AppUser;
import com.example.demo.repo.AppUserRepo;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;
//7
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	private final AppUserRepo appRepo;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
		var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
		var authResp = new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESSFULLY);

		return ResponseEntity.status(HttpStatus.OK).body(authResp);
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto authRequestDto) {
		try {
			String jwtToken = authService.signup(authRequestDto.name(), authRequestDto.username(),
					authRequestDto.password());

			AuthResponseDto authResp = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED);

			return ResponseEntity.status(HttpStatus.OK).body(authResp);
		} catch (Exception e) {
			var authResp = new AuthResponseDto(null, AuthStatus.USER_ALREADY_EXIST);

			return ResponseEntity.status(HttpStatus.CONFLICT).body(authResp);
		}
	}

	@GetMapping("/user")
	public ResponseEntity<AppUser> getUserDetails() {
		// Retrieve the authenticated user's details
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); // Get the username

		// Retrieve the user from the repository
		Optional<AppUser> userOptional = appRepo.findByUsername(username);
		if (userOptional.isPresent()) {
			// User found, return ResponseEntity with the user details
			return ResponseEntity.ok(userOptional.get());
		} else {
			// User not found, return ResponseEntity with  error status
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}

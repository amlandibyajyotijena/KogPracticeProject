package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @GetMapping("/welcome")
	public String welcome() {
		return "welcome but without authorization";
	}

	@GetMapping("/user/u")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String user() {
		return "this is user";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String admin() {
		return "this is admin";
	}
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
}

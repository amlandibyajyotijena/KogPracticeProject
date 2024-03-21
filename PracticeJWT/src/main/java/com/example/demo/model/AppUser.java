package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("app-users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
	
	@Id
	private String name;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;
}

package com.kvp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kvp.dto.UserDTO;
import com.kvp.dto.UserResponse;
import com.kvp.service.UserService;
import com.kvp.util.UtilConstant;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/admin/users")
	public ResponseEntity<UserResponse> getUsers(
			@RequestParam(name = "pageNumber", defaultValue = UtilConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = UtilConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = UtilConstant.SORT_USERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = UtilConstant.SORT_DIR, required = false) String sortOrder) throws Exception {
		
		UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/public/users/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) throws Exception {
		UserDTO user = userService.getUserById(userId);
		
		return new ResponseEntity<UserDTO>(user, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/users/{userId}")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) throws Exception {
		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		
		return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws Exception {
		String status = userService.deleteUser(userId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
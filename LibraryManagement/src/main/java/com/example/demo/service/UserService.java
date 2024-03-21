package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repo.UserRepo;

@Service
public class UserService {
	@Autowired
	private UserRepo repository;

	public User saveUser(User product) {
		return repository.save(product);
	}

	public List<User> getUsers() {
		return repository.getAllUsers();
	}

	public User getUserById(int id) {
		return repository.findById(id);
	}

	public String deleteUser(int id) {
		repository.delete(id);
		return "product removed !! " + id;
	}

	public User updateUser(User product) {
		return repository.update(product);
	}
}

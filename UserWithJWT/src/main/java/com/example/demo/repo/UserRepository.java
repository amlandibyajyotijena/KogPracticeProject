package com.example.demo.repo;

import com.example.demo.helper.RefreshableCRUDRepository;
import com.example.demo.model.UserInfo;

public interface UserRepository extends RefreshableCRUDRepository<UserInfo, Long> 
{
	public UserInfo findByUsername(String username);
	   UserInfo findFirstById(Long id);
}



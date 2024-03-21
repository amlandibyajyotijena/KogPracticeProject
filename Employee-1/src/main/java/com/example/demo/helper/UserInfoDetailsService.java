package com.example.demo.helper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entities.Employee;
import com.example.demo.repo.EmployeeRepository;

@Configuration
public class UserInfoDetailsService implements UserDetailsService{
	
	@Autowired
	private EmployeeRepository emprepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> emp=emprepo.findByName(username);
		
		return emp.map(EmployeeUserDetails::new).orElseThrow(()->new UsernameNotFoundException(username+" is not found"));
	}

}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Employee;
import com.example.demo.repo.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String getEmployee() {
		return "hiiii";
	}

	
	@GetMapping("/employee/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping("id/single")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Employee> getEmployeeById() {
		Optional<Employee> employeeOptional = employeeRepository.findByName(getLoggedInUserDetails().getUsername());
		return ResponseEntity.ok(employeeOptional.get());
		
	}

	@PostMapping("/add")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		Employee savedEmployee = employeeRepository.save(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}
         
	public UserDetails getLoggedInUserDetails() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null&& auth.getPrincipal() instanceof UserDetails) {
			return (UserDetails) auth.getPrincipal();
		}
		return null;
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (employeeOptional.isPresent()) {
			updatedEmployee.setId(id);
			Employee savedEmployee = employeeRepository.save(updatedEmployee);
			return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (employeeOptional.isPresent()) {
			employeeRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

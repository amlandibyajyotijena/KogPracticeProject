package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {
	Optional<Employee> findByName(String name);
}

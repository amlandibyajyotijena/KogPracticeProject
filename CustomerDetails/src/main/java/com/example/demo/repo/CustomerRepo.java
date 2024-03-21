package com.example.demo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Customer;
@Repository
public interface CustomerRepo extends MongoRepository<Customer, Integer> {
	@Query("{'cust_address':?0}")
	List<Customer> findByAddress(String cust_address);

}

package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Customer;
import com.example.demo.repo.CustomerRepo;
@Service
public class CustomerService {
	@Autowired
	CustomerRepo cr;

	public Customer saveCustomer(Customer c) {
		return cr.save(c);
	}

	public List<Customer> fetchAll() {

		return cr.findAll();
	}

	public Customer fetchById(int cust_id) {
		return cr.findById(cust_id).get();
	}

	public String delete(int cust_id) {
		cr.deleteById(cust_id);
		return "Customer Deleted";
	}

	public List<Customer> findByAddress(String cust_address) {
		return cr.findByAddress(cust_address);
	}

}

package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Customer;
import com.example.demo.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	CustomerService cs;

	@PostMapping
	public Customer createCustomer(@RequestBody Customer c) {
		return cs.saveCustomer(c);
	}

	@GetMapping("/allrecords")
	public List<Customer> fetchAll() {
		return cs.fetchAll();
	}
     @GetMapping("/{cust_id}")
	public Customer fetchById(@PathVariable int cust_id) {
		return cs.fetchById(cust_id);
	}
     @DeleteMapping("/{cust_id}")
     public String deleteById(@PathVariable int cust_id) {
    	 cs.delete(cust_id);
    	 return "Customer Delete";
     }
     @GetMapping("/address/{cust_address}")
     public List<Customer> fetchByAddress(@PathVariable String cust_address){
    	 return cs.findByAddress(cust_address);
     }

}

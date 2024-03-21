package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@Document(collection = "customer")
public class Customer {
	@Id
	private int cust_id;
	private String cust_name;
	private String cust_address;
	private String email;
	private String gender;

}

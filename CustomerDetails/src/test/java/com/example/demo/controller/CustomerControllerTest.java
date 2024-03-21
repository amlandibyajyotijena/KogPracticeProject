package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.entities.Customer;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
	@MockBean
	private CustomerService customerService;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateCustomer() throws JsonProcessingException, Exception {
		Customer customer = new Customer(1, "amlan", "a", "b", "c");

		when(customerService.saveCustomer((Customer) any())).thenReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders.post("/customer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.cust_id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cust_name").value("amlan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cust_address").value("a"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("b"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("c"));

	}

	@Test
	void testFetchAll() throws Exception {
		Customer customer = new Customer(1, "amlan", "a", "b", "c");
		Customer customer2 = new Customer(2, "amla", "a", "b", "c");

		List<Customer> customers = Arrays.asList(customer, customer2);

		when(customerService.fetchAll()).thenReturn(customers);

		// Perform the GET request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/allrecords")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].cust_id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].cust_name").value("amlan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].cust_address").value("a"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("b"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("c"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].cust_id").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].cust_name").value("amla"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].cust_address").value("a"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("b"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("c"));

	}

	@Disabled
	@Test
	void testFetchById() {
		fail("Not yet implemented");
	}

	@Disabled
	@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

	@Disabled
	@Test
	void testFetchByAddress() {
		fail("Not yet implemented");
	}

}

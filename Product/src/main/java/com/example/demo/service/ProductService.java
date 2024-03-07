package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Product;
import com.example.demo.repo.ProductRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class ProductService {
	@Autowired
	private ProductRepository repository;

	public Product saveProduct(Product product) {
		log.debug("Saving product: {}", product);
		return repository.save(product);
	}

	public List<Product> getProducts() {
		log.info("Getting all products");
		return repository.findAll();
	}

	public Product getProductById(int id) {
		log.debug("Getting product by ID: {}", id);
		return repository.findById(id).orElse(null);
	}

	public String deleteProduct(int id) {
		log.debug("Deleting product by ID: {}", id);
		repository.deleteById(id);
		return "Product removed: " + id;
	}

	public String updateProduct(int id, Product product) {
		log.debug("Updating product with ID: {}", id);
		Product existingProduct = repository.findById(id).orElse(null);
		if (existingProduct != null) {
			repository.save(product);
			return "Product updated successfully";
		} else {
			return "Product not found";
		}
	}

	public Product getProductByName(String name) {
		log.debug("Getting product by name: {}", name);
		return repository.findByName(name);
	}
}

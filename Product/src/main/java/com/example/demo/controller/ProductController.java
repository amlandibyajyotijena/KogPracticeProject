package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.Product;
import com.example.demo.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        log.info("Adding a product: {}", product);
        return service.saveProduct(product);
    }

    @GetMapping
    public List<Product> findAllProducts() {
        log.info("Getting all products");
        return service.getProducts();
    }

    @GetMapping("{id}")
    public Product findProductById(@PathVariable int id) {
        log.info("Getting product by ID: {}", id);
        return service.getProductById(id);
    }
    
    @GetMapping("/name/{name}")
    public Product findProductByName(@PathVariable String name) {
        log.info("Getting product by name: {}", name);
        return service.getProductByName(name);
    }

    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable int id, @RequestBody Product product) {
        log.info("Updating product with ID: {}", id);
        return service.updateProduct(id, product);
    }

    @DeleteMapping("{id}")
    public String deleteProduct(@PathVariable int id) {
        log.info("Deleting product by ID: {}", id);
        return service.deleteProduct(id);
    }
}

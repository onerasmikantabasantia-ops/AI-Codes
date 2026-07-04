package com.example.crud.controller;

import com.example.crud.entity.Product;
import com.example.crud.agent.ProductAgent;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductAgent agent;

	public ProductController(ProductAgent agent) {
		this.agent = agent;
	}

	@GetMapping
	public List<Product> all() {
		return agent.searchProducts();
	}

	@PostMapping
	public Product create(@RequestBody Product p) {
		return agent.createProduct(p);
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable Long id, @RequestBody Product p) {
		return agent.updateProduct(id, p);
	}
	
	@GetMapping("/{id}")
	public Product getById(@PathVariable Long id) {
		return agent.getProductById(id);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {
		agent.deleteProduct(id);
		return "Deleted";
	}

	@GetMapping("/count/total")
	public long countProducts() {
		return agent.countProducts();
	}

	@GetMapping("/search/by-name")
	public List<Product> findByName(@RequestParam String name) {
		return agent.findByName(name);
	}

	@GetMapping("/search/by-price")
	public List<Product> findByPrice(@RequestParam double price) {
		return agent.findByPrice(price);
	}
}
package com.example.crud.agent;

import com.example.crud.entity.Product;
import com.example.crud.repository.ProductRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProductAgent {
	private final ProductRepository productRepository;

	public ProductAgent(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Create a new product
	 */
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Update an existing product
	 */
	public Product updateProduct(Long id, Product product) {
		Product existingProduct = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
		existingProduct.setName(product.getName());
		existingProduct.setPrice(product.getPrice());
		return productRepository.save(existingProduct);
	}

	/**
	 * Delete a product by ID
	 */
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new IllegalArgumentException("Product not found with id: " + id);
		}
		productRepository.deleteById(id);
	}

	/**
	 * Search all products (get all products)
	 */
	public List<Product> searchProducts() {
		return productRepository.findAll();
	}

	/**
	 * Count total products
	 */
	public long countProducts() {
		return productRepository.count();
	}

	/**
	 * Find products by name
	 */
	public List<Product> findByName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Product name cannot be null or empty");
		}
		return productRepository.findByName(name);
	}

	/**
	 * Find products by price
	 */
	public List<Product> findByPrice(double price) {
		if (price < 0) {
			throw new IllegalArgumentException("Product price cannot be negative");
		}
		return productRepository.findByPrice(price);
	}

	/**
	 * Get product by ID
	 */
	public Product getProductById(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
	}
}

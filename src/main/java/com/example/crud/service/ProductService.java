package com.example.crud.service;

import com.example.crud.entity.Product;
import com.example.crud.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProductService {
	private final ProductRepository r;

	public ProductService(ProductRepository r) {
		this.r = r;
	}

	public List<Product> getAll() {
		return r.findAll();
	}

	public Product save(Product p) {
		return r.save(p);
	}

	public Product update(Long id, Product p) {
		Product e = r.findById(id).orElseThrow();
		e.setName(p.getName());
		e.setPrice(p.getPrice());
		return r.save(e);
	}

	public void delete(Long id) {
		r.deleteById(id);
	}

	public Product getById(Long id) {
		return r.findById(id).orElseThrow();
	}
}
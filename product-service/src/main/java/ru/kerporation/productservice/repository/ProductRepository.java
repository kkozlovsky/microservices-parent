package ru.kerporation.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kerporation.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

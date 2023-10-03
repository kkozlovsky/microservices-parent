package ru.kerporation.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.kerporation.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}

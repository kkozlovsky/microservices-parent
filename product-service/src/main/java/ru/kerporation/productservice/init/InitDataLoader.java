package ru.kerporation.productservice.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kerporation.productservice.model.Product;
import ru.kerporation.productservice.repository.ProductRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() < 1) {
            Product product = new Product();
            product.setName("iPhone 13");
            product.setDescription("iPhone 13");
            product.setPrice(BigDecimal.valueOf(1000));

            productRepository.save(product);
        }
    }
}

package ru.kerporation.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.kerporation.productservice.dto.ProductRequest;
import ru.kerporation.productservice.dto.ProductResponse;
import ru.kerporation.productservice.model.Product;
import ru.kerporation.productservice.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    public void createProduct(final ProductRequest productRequest) {
        final Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Created product: {}", product);
    }

    public List<ProductResponse> getAllProducts() {
        final List<Product> all = productRepository.findAll();

        return all.stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }
}

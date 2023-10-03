package ru.kerporation.productservice.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kerporation.productservice.dto.ProductResponse;
import ru.kerporation.productservice.model.Product;

@Component
public class Product_To_ProductResponseConverter implements Converter<Product, ProductResponse> {

    @Override
    public ProductResponse convert(final Product source) {
        return ProductResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .build();
    }
}

//package ru.kerporation.productservice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.kerporation.productservice.dto.ProductRequest;
//import ru.kerporation.productservice.service.ProductService;
//
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Testcontainers
//@AutoConfigureMockMvc
//class ProductServiceApplicationTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private ProductService productService;
//
//    @Container
//    final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");
//
//    @DynamicPropertySource
//    static void setProperties(final DynamicPropertyRegistry dynamicPropertyRegistry) {
//        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
//        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
//        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @Test
//    void shouldCreateProduct() throws Exception {
//
//        final ProductRequest productRequest = ProductRequest.builder()
//                .name("IPhone13")
//                .description("IPhone13")
//                .price(BigDecimal.valueOf(1200))
//                .build();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isCreated());
//
//        Assertions.assertEquals(1, productService.getAllProducts().size());
//    }
//
//}

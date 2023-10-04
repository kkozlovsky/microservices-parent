package ru.kerporation.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kerporation.orderservice.dto.InventoryResponse;
import ru.kerporation.orderservice.dto.OrderRequest;
import ru.kerporation.orderservice.model.Order;
import ru.kerporation.orderservice.model.OrderLineItems;
import ru.kerporation.orderservice.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(final OrderRequest orderRequest) {
        final Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        final List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> conversionService.convert(orderLineItemsDto, OrderLineItems.class))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        final List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        final InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        final boolean allProductsInStock = nonNull(inventoryResponseArray) && Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Product is out of stock");
        }

    }
}

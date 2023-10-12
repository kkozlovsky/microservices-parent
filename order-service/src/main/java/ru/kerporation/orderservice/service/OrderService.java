package ru.kerporation.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kerporation.orderservice.dto.InventoryResponse;
import ru.kerporation.orderservice.dto.OrderRequest;
import ru.kerporation.orderservice.event.OrderPlacedEvent;
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
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(final OrderRequest orderRequest) {
        final Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        final List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> conversionService.convert(orderLineItemsDto, OrderLineItems.class))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        final List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        final Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try (final Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            final InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            final boolean allProductsInStock = nonNull(inventoryResponseArray) && Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order placed successfully";
            } else {
                throw new IllegalStateException("Product is out of stock");
            }
        } finally {
            inventoryServiceLookup.end();
        }

    }
}

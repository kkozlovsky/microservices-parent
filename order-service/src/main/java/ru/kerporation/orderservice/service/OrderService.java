package ru.kerporation.orderservice.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
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
@Slf4j
public class OrderService {

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;


    public String placeOrder(final OrderRequest orderRequest) {
        final Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        final List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> conversionService.convert(orderLineItemsDto, OrderLineItems.class))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        final List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Call Inventory Service, and place order if a product is in
        // stock
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            final InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            final boolean allProductsInStock = nonNull(inventoryResponseArray) && Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                // publish Order Placed Event
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order placed successfully";
            } else {
                throw new IllegalStateException("Product is out of stock");
            }
        });
    }

}

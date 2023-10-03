package ru.kerporation.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kerporation.orderservice.dto.OrderRequest;
import ru.kerporation.orderservice.model.Order;
import ru.kerporation.orderservice.model.OrderLineItems;
import ru.kerporation.orderservice.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;

    public void placeOrder(final OrderRequest orderRequest) {
        final Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        final List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> conversionService.convert(orderLineItemsDto, OrderLineItems.class))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);
        orderRepository.save(order);
    }
}

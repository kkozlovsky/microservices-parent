package ru.kerporation.orderservice.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kerporation.orderservice.dto.OrderLineItemsDto;
import ru.kerporation.orderservice.model.OrderLineItems;

@Component
public class OrderLineItemsDto_To_OrderLineItems implements Converter<OrderLineItemsDto, OrderLineItems> {

    @Override
    public OrderLineItems convert(final OrderLineItemsDto source) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(source.getPrice());
        orderLineItems.setQuantity(source.getQuantity());
        orderLineItems.setSkuCode(source.getSkuCode());
        return orderLineItems;
    }
}

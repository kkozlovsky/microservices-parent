package ru.kerporation.orderservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderPlacedEvent extends ApplicationEvent {
    private final String orderNumber;

    public OrderPlacedEvent(final Object source, final String orderNumber) {
        super(source);
        this.orderNumber = orderNumber;
    }

    public OrderPlacedEvent(final String orderNumber) {
        super(orderNumber);
        this.orderNumber = orderNumber;
    }
}

package ru.kerporation.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kerporation.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

package ru.kerporation.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kerporation.inventoryservice.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(final String skuCode);
}

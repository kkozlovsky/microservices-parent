package ru.kerporation.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kerporation.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(final List<String> skuCode);
}

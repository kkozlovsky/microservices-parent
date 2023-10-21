package ru.kerporation.inventoryservice.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kerporation.inventoryservice.model.Inventory;
import ru.kerporation.inventoryservice.repository.InventoryRepository;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iphone_13");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_13_red");
        inventory1.setQuantity(0);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
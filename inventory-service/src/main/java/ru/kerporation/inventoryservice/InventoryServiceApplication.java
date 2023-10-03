package ru.kerporation.inventoryservice;

import lombok.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.kerporation.inventoryservice.model.Inventory;
import ru.kerporation.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(final InventoryRepository inventoryRepository) {
		return args -> {
			final Inventory inventory0 = new Inventory();
			inventory0.setSkuCode("iphone_13");
			inventory0.setQuantity(100);

			final Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_13_red");
			inventory1.setQuantity(0);

            inventoryRepository.save(inventory0);
            inventoryRepository.save(inventory1);
        };
	}

}

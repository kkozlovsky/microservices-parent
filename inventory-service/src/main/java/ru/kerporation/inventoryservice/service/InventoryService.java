package ru.kerporation.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kerporation.inventoryservice.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    
    @Transactional(readOnly = true)
    public boolean isInStock(final String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}

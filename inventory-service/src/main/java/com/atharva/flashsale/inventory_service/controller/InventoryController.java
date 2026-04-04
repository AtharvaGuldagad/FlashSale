package com.atharva.flashsale.inventory_service.controller;

import com.atharva.flashsale.inventory_service.model.Inventory;
import com.atharva.flashsale.inventory_service.repository.InventoryRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping
    public Inventory addStock(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @GetMapping("/{productId}")
    public Inventory checkStock(@PathVariable String productId) {
        // Return the inventory, or null if it doesn't exist yet
        return inventoryRepository.findByProductId(productId).orElse(null);
    }
}
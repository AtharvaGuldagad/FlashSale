package com.atharva.flashsale.inventory_service.kafka;

import com.atharva.flashsale.inventory_service.event.InventoryReservedEvent;
import com.atharva.flashsale.inventory_service.event.OrderCreatedEvent;
import com.atharva.flashsale.inventory_service.model.Inventory;
import com.atharva.flashsale.inventory_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryEventHandler {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventHandler(InventoryRepository inventoryRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "docker-exclusive-group")
public void handleOrderCreated(String rawMessage) {
    // If it catches ANYTHING, it will scream it in red text!
    System.err.println("🚨 RED HANDED! INVENTORY CAUGHT THE RAW MESSAGE:");
    System.err.println(rawMessage);
}
    
    @KafkaListener(topics = "order-cancelled-events", groupId = "inventory-group")
    public void handleOrderCancelled(OrderCreatedEvent event) {
        System.out.println("Inventory Service received Rollback for Order ID: " + event.getOrderId());

        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(event.getProductId());

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            
            // Add the stock BACK to the database
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + event.getQuantity());
            inventoryRepository.save(inventory);

            System.out.println("Stock Restored! Current Quantity: " + inventory.getAvailableQuantity());
        }
    }
}
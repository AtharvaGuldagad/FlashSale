package com.atharva.flashsale.inventory_service.kafka;

import com.atharva.flashsale.inventory_service.event.InventoryReservedEvent;
import com.atharva.flashsale.inventory_service.event.OrderCreatedEvent;
import com.atharva.flashsale.inventory_service.model.Inventory;
import com.atharva.flashsale.inventory_service.repository.InventoryRepository;
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

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Inventory Service received Order Event for Order ID: " + event.getOrderId());

        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(event.getProductId());

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            
            // Check if we have enough stock
            if (inventory.getAvailableQuantity() >= event.getQuantity()) {
                // Deduct stock and save
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - event.getQuantity());
                inventoryRepository.save(inventory);

                System.out.println("Stock deducted. Remaining: " + inventory.getAvailableQuantity());

                // Publish the next event to the "inventory-events" topic
                InventoryReservedEvent reservedEvent = new InventoryReservedEvent(
                        event.getOrderId(), 
                        event.getProductId(), 
                        "RESERVED"
                );
                kafkaTemplate.send("inventory-events", reservedEvent);
                System.out.println("Published InventoryReservedEvent to Kafka!");
            } else {
                System.out.println("Out of stock for product: " + event.getProductId());
                // REMINDER to handle this later
            }
        }
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
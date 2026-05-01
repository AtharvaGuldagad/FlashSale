package com.atharva.flashsale.order_service.kafka;

import com.atharva.flashsale.order_service.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        // "order-events" = Kafka topic we are sending this to
        try {
            kafkaTemplate.send("order-events", event).get();
            System.out.println("Order Event Published to Kafka: " + event.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to publish order event to Kafka", e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Failed to publish order event to Kafka", e);
        }
    }
}
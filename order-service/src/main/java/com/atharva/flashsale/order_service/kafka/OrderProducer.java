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
            // The .get() forces the thread to wait for a success or failure
            kafkaTemplate.send("order-events", event).get();
            System.out.println("✅ SUCCESSFULLY SENT EVENT TO KAFKA!");
        } catch (Exception e) {
            // If serialization fails, it will be caught here and printed to Docker logs
            System.err.println("🚨 KAFKA SEND FAILED! HERE IS THE REASON:");
            e.printStackTrace();
        }
        System.out.println("Order Event Published to Kafka: " + event.getOrderId());
    }
}
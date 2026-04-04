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
        kafkaTemplate.send("order-events", event);
        System.out.println("Order Event Published to Kafka: " + event.getOrderId());
    }
}
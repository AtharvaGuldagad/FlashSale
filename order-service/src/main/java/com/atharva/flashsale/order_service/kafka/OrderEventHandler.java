package com.atharva.flashsale.order_service.kafka;

import com.atharva.flashsale.order_service.event.OrderCreatedEvent;
import com.atharva.flashsale.order_service.event.PaymentEvent;
import com.atharva.flashsale.order_service.model.Order;
import com.atharva.flashsale.order_service.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderEventHandler {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventHandler(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void handlePaymentResult(PaymentEvent event) {
        Optional<Order> orderOpt = orderRepository.findById(event.getOrderId());

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();

            if ("PAYMENT_SUCCESS".equals(event.getStatus())) {
                order.setStatus("COMPLETED");
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " marked as COMPLETED.");
            } else {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " marked as CANCELLED. Triggering Rollback...");

                // Reuse the OrderCreatedEvent to pass the exact details back to Inventory
                OrderCreatedEvent rollbackEvent = new OrderCreatedEvent(
                        order.getId(),
                        order.getProductId(),
                        order.getQuantity()
                );
                
                // Publish to a new cancellation topic
                kafkaTemplate.send("order-cancelled-events", rollbackEvent);
            }
        }
    }
}
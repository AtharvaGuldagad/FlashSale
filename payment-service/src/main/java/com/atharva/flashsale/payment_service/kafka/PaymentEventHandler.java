package com.atharva.flashsale.payment_service.kafka;

import com.atharva.flashsale.payment_service.event.InventoryReservedEvent;
import com.atharva.flashsale.payment_service.event.PaymentEvent;
import com.atharva.flashsale.payment_service.model.Payment;
import com.atharva.flashsale.payment_service.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventHandler {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentEventHandler(PaymentRepository paymentRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "inventory-events", groupId = "payment-group")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        System.out.println("Payment Service received Reservation for Order ID: " + event.getOrderId());

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        
        // We don't have the price in this event, so we'll just mock an amount for the database
        payment.setAmount(99.99); 

        // Simulate a credit card decline logic (Odd Order IDs fail, Even succeed)
        if (event.getOrderId() % 2 != 0) {
            System.out.println("CREDIT CARD DECLINED for Order ID: " + event.getOrderId());
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            PaymentEvent failedEvent = new PaymentEvent(event.getOrderId(), "PAYMENT_FAILED");
            kafkaTemplate.send("payment-events", failedEvent);
            System.out.println("Published PAYMENT_FAILED to Kafka!");
            
        } else {
            System.out.println("Payment successful for Order ID: " + event.getOrderId());
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            PaymentEvent successEvent = new PaymentEvent(event.getOrderId(), "PAYMENT_SUCCESS");
            kafkaTemplate.send("payment-events", successEvent);
            System.out.println("Published PAYMENT_SUCCESS to Kafka!");
        }
    }
}
package com.atharva.flashsale.order_service.controller;

import com.atharva.flashsale.order_service.event.OrderCreatedEvent;
import com.atharva.flashsale.order_service.kafka.OrderProducer;
import com.atharva.flashsale.order_service.model.Order;
import com.atharva.flashsale.order_service.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer; // Inject the producer

    public OrderController(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);

        // Create the event object
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(), 
                savedOrder.getProductId(), 
                savedOrder.getQuantity()
        );
        
        // Publish to Kafka!
        orderProducer.sendOrderCreatedEvent(event);

        return savedOrder;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
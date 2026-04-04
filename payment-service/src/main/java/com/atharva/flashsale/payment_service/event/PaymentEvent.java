package com.atharva.flashsale.payment_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private Long orderId;
    private String status; // Will be "PAYMENT_SUCCESS" or "PAYMENT_FAILED"
}
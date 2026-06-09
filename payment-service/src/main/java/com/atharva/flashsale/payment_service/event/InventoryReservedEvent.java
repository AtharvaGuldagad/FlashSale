package com.atharva.flashsale.payment_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservedEvent {
    private Long orderId;
    private String productId;
    private String status;
}

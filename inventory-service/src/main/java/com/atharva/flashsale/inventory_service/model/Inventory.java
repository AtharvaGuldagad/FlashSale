package com.atharva.flashsale.inventory_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String productId;
    private Integer availableQuantity;
}
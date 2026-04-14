package com.atharva.flashsale.inventory_service.repository;

import com.atharva.flashsale.inventory_service.model.Inventory;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByProductId(String productId);
}
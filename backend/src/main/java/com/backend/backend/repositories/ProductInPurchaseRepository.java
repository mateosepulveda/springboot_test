package com.backend.backend.repositories;

import com.backend.backend.models.ProductInPurchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductInPurchase p WHERE p.purchase.id = :purchaseId AND p.product.id = :productId")
    void deleteByPurchaseIdAndProductId(Long purchaseId, Long productId);

    @Transactional
    @Query("SELECT p FROM ProductInPurchase p WHERE p.purchase.id = :purchaseId AND p.product.id = :productId")
    Optional<ProductInPurchase> findByPurchaseIdAndProductId(Long purchaseId, Long productId);
}
package com.backend.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.backend.backend.models.Purchase;
import com.backend.backend.models.ProductInPurchase;
import com.backend.backend.repositories.PurchaseRepository;
import com.backend.backend.repositories.ProductInPurchaseRepository;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;

    // Get all purchases
    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    // Get purchase by ID
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        return purchase.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new purchase
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        try {
            Purchase savedPurchase = purchaseRepository.save(purchase);
            return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add product to a purchase
    @PostMapping("/{purchaseId}/products")
    public ResponseEntity<ProductInPurchase> addProductToPurchase(
            @PathVariable Long purchaseId, @RequestBody ProductInPurchase productInPurchase) {
        try {
            Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
            if (purchase.isPresent()) {
                productInPurchase.setPurchase(purchase.get());
                ProductInPurchase savedProductInOrder = productInPurchaseRepository.save(productInPurchase);
                return new ResponseEntity<>(savedProductInOrder, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update product quantity in a purchase
    @PutMapping("/{purchaseId}/products/{productId}")
    public ResponseEntity<ProductInPurchase> updateProductQuantityInPurchase(
            @PathVariable Long purchaseId, @PathVariable Long productId,
            @RequestBody ProductInPurchase productInPurchase) {
        try {
            Optional<ProductInPurchase> existingProductInPurchase = productInPurchaseRepository
                    .findByPurchaseIdAndProductId(purchaseId, productId);
            if (existingProductInPurchase.isPresent()) {
                ProductInPurchase updatedProductInPurchase = existingProductInPurchase.get();
                updatedProductInPurchase.setQuantity(productInPurchase.getQuantity());
                ProductInPurchase savedProductInPurchase = productInPurchaseRepository.save(updatedProductInPurchase);
                return new ResponseEntity<>(savedProductInPurchase, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a purchase by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePurchase(@PathVariable Long id) {
        try {
            purchaseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete product from a purchase
    @DeleteMapping("/{purchaseId}/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProductFromPurchase(
            @PathVariable Long purchaseId, @PathVariable Long productId) {
        try {
            productInPurchaseRepository.deleteByPurchaseIdAndProductId(purchaseId, productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
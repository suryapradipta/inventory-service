package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(
            """
                    SELECT COUNT(i) > 0 FROM Inventory i WHERE i.skuCode = :skuCode
                                        AND i.quantity >= :quantity
                    """
    )
    boolean existsBySkuCodeAndQuantity(String skuCode, Integer quantity);
}

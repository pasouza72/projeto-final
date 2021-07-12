package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.dtos.response.ProductCountResponseDTO;
import com.mercadolibre.projetofinal.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

    Warehouse findByCountry(String country);
    @Query(value = "SELECT" +
            "    sum(bp.current_quantity) as count," +
            "    whs.warehouse_id as warehouseId " +
            "FROM batch_product as bp" +
            "    INNER JOIN warehouses_sections as whs ON bp.warehouses_sections_id = whs.id " +
            "WHERE" +
            "    bp.product_id = :productId AND bp.current_quantity > 0 " +
            "GROUP BY" +
            "    warehouse_id ORDER BY count DESC", nativeQuery = true)
    List<ProductCountResponseDTO> findProductCountByWarehouse(String productId);
}

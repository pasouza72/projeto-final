package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.dtos.response.ProductStockCountResponseDTO;
import com.mercadolibre.projetofinal.model.BatchProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BatchProductRepository extends JpaRepository<BatchProduct, String> {
    @Query(value = "SELECT * FROM batch_product as p INNER JOIN warehouses_sections as whs " +
            "ON whs.id = p.warehouses_sections_id " +
            "WHERE whs.warehouse_id = :warehouseId " +
            "AND p.product_id = :productId " +
            "AND p.due_date > DATE_SUB(curdate(),INTERVAL -3 WEEK)",nativeQuery = true)
    Page<BatchProduct> findBatchesInWarehouseByProductId(String warehouseId, String productId, Pageable pageRequest);

    @Query(value = "select bp.* from batch_product bp " +
            "inner join product p on p.id = bp.product_id " +
            "inner join warehouses w on w.country = :countryNumber " +
            "inner join warehouses_sections ws on ws.warehouse_id = w.id " +
            "where p.id in :productsIds " +
            "group by bp.id ", nativeQuery = true)
    Optional<List<BatchProduct>> findAllBatchByProductsAndCountry(
            @Param(value = "productsIds") List<String> productsId, @Param(value = "countryNumber") Integer country);

    @Query(value = "select bp.* from batch_product bp " +
            "inner join product p on p.id = bp.product_id " +
            "inner join warehouses w on w.country = :countryNumber " +
            "inner join warehouses_sections ws on ws.warehouse_id = w.id " +
            "where p.id = :productId " +
            "group by bp.id ", nativeQuery = true)
    Optional<List<BatchProduct>> findAllBatchByProductAndCountry(
            @Param(value = "productId") String productId, @Param(value = "countryNumber") Integer country);

    @Query(value = "SELECT SUM(current_quantity) AS currentQuantity, product_id AS productId " +
            "FROM warehouses w " +
            "inner join accounts a ON a.country = w.country " +
            "inner join warehouses_sections ws ON ws.warehouse_id = w.id " +
            "inner join batch_product bp ON bp.warehouses_sections_id = ws.id " +
            "where username = :username AND bp.product_id IN :productsId AND bp.due_date > DATE_SUB(curdate(),INTERVAL -3 WEEK) " +
            "group by product_id", nativeQuery = true)
    List<ProductStockCountResponseDTO> getStockProductsByUsername(String username, List<String> productsId);

    @Query(value = "select * " +
            "from batch_product bp " +
            "inner join warehouses_sections ws on ws.id = bp.warehouses_sections_id " +
            "where " +
            "due_date <= date_add(curdate(), interval :days day) " +
            "and ws.warehouse_id = :warehouseId ", nativeQuery = true)
    Page<BatchProduct> findByWarehouseIdAndDueDate(String warehouseId, Integer days, Pageable pageRequest);

    @Query(value = "select * " +
            "from batch_product bp " +
            "inner join warehouses_sections ws on ws.id = bp.warehouses_sections_id " +
            "inner join sections s on s.id = ws.section_id " +
            "where due_date <= date_add(curdate(), interval :days day) " +
            "and ws.warehouse_id = :warehouseId " +
            "and s.category = :category ", nativeQuery = true)
    Page<BatchProduct> findByWarehouseIdAndDueDateAndCategory(String warehouseId, Integer days, String category, Pageable pageRequest);
}

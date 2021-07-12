package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    Integer countByIdIn(List<String> ids);

    @Query(value = "SELECT p.* FROM product p " +
            "inner join batch_product bp ON bp.product_id = p.id " +
            "inner join warehouses_sections ws ON ws.id = bp.warehouses_sections_id " +
            "inner join warehouses w ON w.id = ws.warehouse_id " +
            "where country = :country AND p.id IN :productsId", nativeQuery = true)
    List<Product> findAllByIdAndCountry(List<String> productsId, Integer country);
    List<Product> findByBatchProductsWarehousesSectionsWarehouseCountry(CountryEnum countryEnum);

    List<Product> findByBatchProductsWarehousesSectionsWarehouseCountryAndBatchProductsWarehousesSectionsSectionId(CountryEnum countryEnum, String id);
}

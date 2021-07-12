package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrdersProductsRepository extends JpaRepository<PurchaseOrdersProducts, String> {
}

package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
}

package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.InsertOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsertOrderRepository extends JpaRepository<InsertOrder, Integer> {
}

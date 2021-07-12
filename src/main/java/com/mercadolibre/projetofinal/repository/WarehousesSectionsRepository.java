package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.WarehousesSections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehousesSectionsRepository extends JpaRepository<WarehousesSections, String> {

    Optional<WarehousesSections> findByWarehouseIdAndSectionId(String warehouseId, String sectionId);
}

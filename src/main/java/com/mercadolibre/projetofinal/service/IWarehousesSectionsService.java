package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.model.WarehousesSections;

public interface IWarehousesSectionsService {

    WarehousesSections findByWarehouseIdAndSectionId(String warehouseId, String sectionId);
}

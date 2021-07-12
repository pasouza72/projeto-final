package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.WarehousesSections;
import com.mercadolibre.projetofinal.repository.WarehousesSectionsRepository;
import com.mercadolibre.projetofinal.service.IWarehousesSectionsService;
import org.springframework.stereotype.Service;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML103;

@Service
public class WarehousesSectionsService implements IWarehousesSectionsService {

    private final WarehousesSectionsRepository warehousesSectionsRepository;

    public WarehousesSectionsService(WarehousesSectionsRepository warehousesSectionsRepository) {
        this.warehousesSectionsRepository = warehousesSectionsRepository;
    }

    @Override
    public WarehousesSections findByWarehouseIdAndSectionId(String warehouseId, String sectionId) {
        return warehousesSectionsRepository.findByWarehouseIdAndSectionId(warehouseId, sectionId).orElseThrow(
                () -> new NotFoundException(ML103));
    }
}

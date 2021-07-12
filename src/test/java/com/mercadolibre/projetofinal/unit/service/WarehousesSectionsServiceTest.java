package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.repository.WarehousesSectionsRepository;
import com.mercadolibre.projetofinal.service.impl.WarehousesSectionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML103;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.*;

@ExtendWith(MockitoExtension.class)
public class WarehousesSectionsServiceTest {

    @Mock
    private WarehousesSectionsRepository warehousesSectionsRepository;

    @InjectMocks
    private WarehousesSectionsService warehousesSectionsService;

    @Test
    public void findByWarehouseIdAndSectionId() {
        when(warehousesSectionsRepository.findByWarehouseIdAndSectionId(any(), any()))
                .thenReturn(Optional.of(warehousesSections));
        assertEquals(warehousesSections, warehousesSectionsService.findByWarehouseIdAndSectionId("", ""));
    }

    @Test
    public void findByWarehouseIdAndSectionIdException() {
        when(warehousesSectionsRepository.findByWarehouseIdAndSectionId(any(), any()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            warehousesSectionsService.findByWarehouseIdAndSectionId("", "");
        });

        assertEquals(ML103.getDescription(), exception.getMessage());
    }
}

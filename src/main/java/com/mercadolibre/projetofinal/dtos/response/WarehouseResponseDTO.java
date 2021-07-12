package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class WarehouseResponseDTO {
    private String message;
    private WarehouseDTO warehouseDTO;
}

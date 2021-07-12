package com.mercadolibre.projetofinal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionOrderRequestDTO {
    private String sectionCode;
    private String warehouseCode;


}

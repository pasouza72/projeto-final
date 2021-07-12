package com.mercadolibre.projetofinal.dtos.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderRequestDTO {

    private String id;
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    @Valid
    private OrderStatusRequestDTO orderStatus;
    @Valid
    private List<ProductsRequestDTO> products;
}

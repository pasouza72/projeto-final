package com.mercadolibre.projetofinal.dtos.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderRequestDTO {

    @NotNull
    private Integer orderNumber;
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate orderDate;
    @Valid
    @NotNull
    private SectionOrderRequestDTO section;

    @NotEmpty
    @NotNull
    @Valid
    private List<BatchProductRequestDTO> batchStock;

}

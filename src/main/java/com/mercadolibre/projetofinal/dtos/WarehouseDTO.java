package com.mercadolibre.projetofinal.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.projetofinal.dtos.response.AccountResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {

    @JsonIgnore
    private Integer id;

    @NotNull(message = "Name es requerido")
    @Size(min = 3, message = "Name debe tener un minimo de 3 caracteres")
    private String name;

    @NotNull(message = "Country es requerido")
    @Size(min = 3, message = "Country debe tener un minimo de 3 caracteres")
    private String country;

    @JsonIgnore
    private List<AccountResponseDTO> accounts;
}

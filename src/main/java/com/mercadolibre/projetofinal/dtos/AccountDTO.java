package com.mercadolibre.projetofinal.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDTO {

    private String id;
    private String username;
    private String warehouse;
    private Integer country;

}

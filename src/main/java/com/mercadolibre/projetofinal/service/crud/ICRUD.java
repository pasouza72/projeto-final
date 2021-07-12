package com.mercadolibre.projetofinal.service.crud;

import java.util.List;

public interface ICRUD <DTO>{
    DTO create(DTO dto);

    DTO update(DTO dto);

    void delete(String id);

    DTO findById(String id);

    List<DTO> findAll();
}

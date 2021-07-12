package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountListDTO;
import com.mercadolibre.projetofinal.model.Warehouse;
import com.mercadolibre.projetofinal.service.crud.ICRUD;

public interface IWarehouseService extends ICRUD<WarehouseDTO> {
    WarehouseDTO findByCountry(String country);
    Warehouse findWarehouseById(String id);

    ProductCountListDTO getProductCountByWarehouse(String productId);
}

package com.mercadolibre.projetofinal.controller;


import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountListDTO;
import com.mercadolibre.projetofinal.dtos.response.WarehouseResponseDTO;
import com.mercadolibre.projetofinal.service.IWarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping(path = "/api/v1/fresh-products/warehouse")
@RestController
@AllArgsConstructor
public class WarehouseController {

    private IWarehouseService warehouseService;

    /**================================
     * Creates a warehouse in a country
     */
    @RequestMapping(method = RequestMethod.POST, consumes="application/json")
    @ResponseBody
    public ResponseEntity<WarehouseResponseDTO> create(@RequestHeader(value="Authorization") String token, @Validated @RequestBody WarehouseDTO newCountryHouse) {
        WarehouseDTO warehouseDTO = warehouseService.create(newCountryHouse);

        WarehouseResponseDTO response = new WarehouseResponseDTO();

        if (warehouseDTO !=null){
            response.setWarehouseDTO(warehouseDTO);
            response.setMessage("CREADO");
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(warehouseDTO.getId()).toUri();
            return new ResponseEntity<WarehouseResponseDTO>(response, HttpStatus.CREATED).created(location).body(response);
        }else{
            response.setWarehouseDTO(newCountryHouse);
            response.setMessage("YA EXISTE");
            return new ResponseEntity<WarehouseResponseDTO>(response, HttpStatus.BAD_REQUEST);
        }

    }

    /**================================
     * Counts the quantity of products in Warehouse
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ProductCountListDTO> getProductCountByWarehouse(@RequestParam String productId) {
        ProductCountListDTO response = warehouseService.getProductCountByWarehouse(productId);
        return ResponseEntity.ok(response);

    }


}

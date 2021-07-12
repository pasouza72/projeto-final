package com.mercadolibre.projetofinal.controller;

import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseValueResponseDTO;
import com.mercadolibre.projetofinal.service.IPurchaseOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/orders")
@AllArgsConstructor
public class PurchaseOrderController {

    private final IPurchaseOrderService purchaseOrderService;

    @PostMapping(consumes="application/json")
    @ResponseBody
    public ResponseEntity<PurchaseValueResponseDTO> create(@RequestHeader(value="Authorization") String token,
                                                           @RequestBody @Valid PurchaseRequestDTO purchaseRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.create(purchaseRequestDTO, token));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderProductResponseDTO>> getProductsOfPurchase(
            @RequestParam(name = "orderId") String orderId, @RequestHeader(value="Authorization") String token){
            return ResponseEntity.status(HttpStatus.OK).body(purchaseOrderService.getProductsOfPurchase(orderId));
    }

    @PutMapping(consumes="application/json")
    @ResponseBody
    public ResponseEntity<PurchaseOrderResponseDTO> update(@RequestParam(name = "orderId") String orderId,
                                                           @RequestBody @Valid PurchaseRequestDTO purchaseRequestDTO,
                                                           @RequestHeader(value="Authorization") String token
                                                           ) {
        purchaseRequestDTO.getPurchaseOrder().setId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(purchaseOrderService.update(purchaseRequestDTO, token));
    }
}

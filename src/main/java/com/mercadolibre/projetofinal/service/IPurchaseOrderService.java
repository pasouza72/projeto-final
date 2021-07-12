package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseValueResponseDTO;

import java.util.List;

public interface IPurchaseOrderService {
    PurchaseValueResponseDTO create(PurchaseRequestDTO purchaseRequestDTO, String token);

    List<PurchaseOrderProductResponseDTO> getProductsOfPurchase(String orderId);

    PurchaseOrderResponseDTO update(PurchaseRequestDTO purchaseRequestDTO, String token);
//    PurchaseOrderResponseDTO updateTest(PurchaseRequestDTO purchaseRequestDTO, String token);
}

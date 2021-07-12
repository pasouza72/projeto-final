package com.mercadolibre.projetofinal.factory;

import com.mercadolibre.projetofinal.dtos.request.PurchaseOrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.model.PurchaseOrder;
import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderFactory {
    private PurchaseOrderFactory() { throw new IllegalStateException("Utility class"); }


    public static PurchaseOrder toPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO, String accountId) {
        return PurchaseOrder.builder()
                .buyerId(accountId)
                .date(purchaseOrderRequestDTO.getDate())
                .statusCode(purchaseOrderRequestDTO.getOrderStatus().getStatusCode())
                .build();
    }

    public static PurchaseOrderProductResponseDTO toPurchaseOrderProductResponseDTO(PurchaseOrdersProducts purchaseOrdersProducts){
        var product = purchaseOrdersProducts.getProduct();
        return PurchaseOrderProductResponseDTO.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .quantity(purchaseOrdersProducts.getQuantity())
                                                .build();

    }

    public static PurchaseOrderResponseDTO toPurchaseOrderResponseDTO(PurchaseOrderRequestDTO purchaseOrderRequestDTO,
                                                                     List<PurchaseOrdersProducts> purchaseOrdersProductsList) {

        List<PurchaseOrderProductResponseDTO> products = purchaseOrdersProductsList.stream()
                                                                    .map(p -> new PurchaseOrderProductResponseDTO(
                                                                            p.getProduct().getId(),
                                                                            p.getProduct().getName(),
                                                                            p.getQuantity()))
                                                                    .collect(Collectors.toList());

        return new PurchaseOrderResponseDTO(purchaseOrderRequestDTO.getId(),
                                                purchaseOrderRequestDTO.getDate(),
                                                purchaseOrderRequestDTO.getOrderStatus(),
                                                products);
    }
}

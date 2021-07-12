package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.PurchaseOrder;
import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;
import com.mercadolibre.projetofinal.repository.PurchaseOrdersProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PurchaseOrdersProductsService {

    private final PurchaseOrdersProductsRepository purchaseOrdersProductsRepository;

    public List<PurchaseOrdersProducts> save(List<Product> products, PurchaseOrder purchaseOrder,
                                              List<ProductsRequestDTO> purchaseOrderRequestDTOProducts){

        Map<String, Integer> map = new HashMap<>();

        purchaseOrderRequestDTOProducts.forEach(p -> map.put(p.getProductId(), p.getQuantity()));

        List<PurchaseOrdersProducts> purchaseOrdersProductsList = products.stream()
                                                    .map(p -> mountPurchaseOrdersProducts(purchaseOrder, map, p))
                                                    .collect(Collectors.toList());

        return purchaseOrdersProductsRepository.saveAll(purchaseOrdersProductsList);
    }

    private PurchaseOrdersProducts mountPurchaseOrdersProducts(PurchaseOrder purchaseOrder, Map<String, Integer> map,
                                                               Product p) {
        return PurchaseOrdersProducts.builder()
                                    .purchaseOrder(purchaseOrder)
                                    .product(p)
                                    .quantity(map.get(p.getId()))
                                    .build();
    }
}

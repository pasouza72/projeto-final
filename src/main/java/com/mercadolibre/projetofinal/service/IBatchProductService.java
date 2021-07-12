package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.dtos.request.BatchProductRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.ListBatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.Warehouse;
import com.mercadolibre.projetofinal.model.WarehousesSections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IBatchProductService {

    List<BatchProduct> convertToModel(List<BatchProductRequestDTO> products, WarehousesSections warehousesSections);
    Page<BatchProduct> findBatchProductsByProductId(String productId, Warehouse warehouse, PageRequest pageRequest);
    ListBatchStockSimplifiedResponseDTO findBatchesForRepresentative(String productId, String token, PageRequest pageRequest);
    void validStockOfProductsByCountry(List<Product> products, List<ProductsRequestDTO> productsRequest, String username);
    void updateStock(List<ProductsRequestDTO> products, Integer country);
    Page<BatchProduct> findBatchesByRepresentativeAndDueDate(String token, Integer days, PageRequest pageRequest);
    Page<BatchProduct> findBatchesByRepresentativeAndDueDateAndCategory(String token, Integer days, String category, PageRequest pageRequest);
}

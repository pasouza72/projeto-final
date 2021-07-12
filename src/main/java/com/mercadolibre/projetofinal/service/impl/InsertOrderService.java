package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.BatchProductRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.OrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.BatchProductResponseDTO;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.exceptions.BadRequestException;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.*;
import com.mercadolibre.projetofinal.repository.InsertOrderRepository;
import com.mercadolibre.projetofinal.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.*;

@Service
@AllArgsConstructor
public class InsertOrderService implements IInsertOrderService {

    private final IProductService productService;
    private final IWarehouseService warehouseService;
    private final IBatchProductService batchProductService;
    private final IWarehousesSectionsService warehousesSectionsService;
    private final ISessionService sessionService;
    private final InsertOrderRepository insertOrderRepository;

    @Override
    public List<BatchProductResponseDTO> createOrder(OrderRequestDTO orderRequestDTO, AccountDTO accountDTO) {
        verifyOrderExistence(orderRequestDTO.getInboundOrder().getOrderNumber());
        InsertOrder insertOrder = prepareInsertOrder(orderRequestDTO, accountDTO, true);
        return getBatchProductResponse(insertOrderRepository.save(insertOrder));
    }

    @Override
    public List<BatchProductResponseDTO> updateOrder(OrderRequestDTO orderRequestDTO, AccountDTO accountDTO) {
        InsertOrder databaseInsertOrder = findById(orderRequestDTO.getInboundOrder().getOrderNumber());
        InsertOrder insertOrder = prepareInsertOrder(orderRequestDTO, accountDTO, false);
        insertOrder = handleDuplicateBatchProducts(insertOrder, databaseInsertOrder);

        return getBatchProductResponse(insertOrderRepository.save(insertOrder));
    }

    private List<BatchProductResponseDTO> getBatchProductResponse(InsertOrder insertOrder){
        return insertOrder.getBatchProducts().stream().map(element -> new BatchProductResponseDTO(element)).collect(Collectors.toList());
    }


    private InsertOrder prepareInsertOrder(OrderRequestDTO orderRequestDTO, AccountDTO accountDTO, Boolean create) {
        List<BatchProductRequestDTO> productsToInsert = orderRequestDTO.getInboundOrder().getBatchStock();
        String sectionIdToInsert = orderRequestDTO.getInboundOrder().getSection().getSectionCode();
        String warehouseIdToInsert = orderRequestDTO.getInboundOrder().getSection().getWarehouseCode();
        Warehouse warehouse = warehouseService.findWarehouseById(warehouseIdToInsert);
        WarehousesSections warehousesSections = warehousesSectionsService
                .findByWarehouseIdAndSectionId(warehouseIdToInsert,sectionIdToInsert);
        if (create){
            verifyWarehouseSectionStillHasRoomInsert(warehousesSections, productsToInsert, sectionIdToInsert);
        }
        verifyInsertIntegrity(warehouse, sectionIdToInsert, accountDTO, productsToInsert);
        List<BatchProduct> batchProducts = batchProductService.convertToModel(productsToInsert,warehousesSections);
        Account account = sessionService.getAccountById(accountDTO.getId());
        InsertOrder insertOrder = new InsertOrder(orderRequestDTO.getInboundOrder().getOrderNumber(),
                warehousesSections,account,batchProducts, LocalDateTime.now());
        batchProducts.forEach(el -> el.setInsertOrder(insertOrder));
        return insertOrder;
    }

    private InsertOrder handleDuplicateBatchProducts(InsertOrder insertOrder, InsertOrder databaseInsertOrder){
        List<BatchProduct> databaseProducts = databaseInsertOrder.getBatchProducts();
        List<BatchProduct> newBatchProducts = insertOrder.getBatchProducts().stream().map(
                el -> {
                    Integer index = databaseProducts.indexOf(el);
                    return index == -1 ? el : databaseProducts.get(index);
                }
                ).collect(Collectors.toList());
        insertOrder.getBatchProducts().clear();
        insertOrder.getBatchProducts().addAll(newBatchProducts);
        Integer spaceLeft = spaceLeftInSection(insertOrder.getWarehousesSections(),
                insertOrder.getWarehousesSections().getSection().getId());
        if (spaceLeft < getBalanceFromUpdate(databaseProducts, newBatchProducts)){
            throw new ApiException(ML102);
        }
        return insertOrder;
        }

    private Integer getBalanceFromUpdate(List<BatchProduct> databaseProducts, List<BatchProduct> newPartialProducts){
            return getNewProductsAfterUpdate(databaseProducts, newPartialProducts)
                    - getProductsToDeleteAfterUpdate(databaseProducts, newPartialProducts);
        }

        private Integer getNewProductsAfterUpdate(List<BatchProduct> databaseProducts, List<BatchProduct> newPartialProducts){
            return newPartialProducts
                    .stream()
                    .filter(el -> databaseProducts.indexOf(el) == -1)
                    .mapToInt(BatchProduct::getCurrentQuantity)
                    .sum();
        }

    private Integer getProductsToDeleteAfterUpdate(List<BatchProduct> databaseProducts, List<BatchProduct> newPartialProducts){
        return databaseProducts
                .stream()
                .filter(el -> newPartialProducts.indexOf(el) == -1)
                .mapToInt(BatchProduct::getCurrentQuantity)
                .sum();
    }

    public InsertOrder findById(Integer orderId) {
        return insertOrderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(ML701)
        );
    }

    private void verifyInsertIntegrity(Warehouse warehouse, String sectionIdToInsert, AccountDTO accountDTO,
                                       List<BatchProductRequestDTO> productToInsert){
        verifyNoDuplicateProductBatches(productToInsert);
        verifyRepresentativeBelongsToWarehouse(warehouse.getId(), accountDTO);
        verifySectionExistsInWarehouse(warehouse, sectionIdToInsert);
        verifyAllProductsExist(productToInsert);
        verifyProductsTemperatureMatchesSection(warehouse, sectionIdToInsert, productToInsert);
    }

    private void verifyRepresentativeBelongsToWarehouse(String warehouseIdToInsert, AccountDTO accountDTO){
        if (!accountDTO.getWarehouse().equals(warehouseIdToInsert)) {
            throw new ApiException(ML202);
        }
    }

    private void verifySectionExistsInWarehouse(Warehouse warehouse, String sectionIdToInsert){
        warehouse.getWarehouseSections().stream().map(el -> el.getSection().getId())
                .filter(el -> el.equals(sectionIdToInsert)).findFirst().orElseThrow(() -> new NotFoundException(ML103));
    }

    public void verifyAllProductsExist(List<BatchProductRequestDTO> products){
        List<String> productIds = products
                .stream()
                .map(BatchProductRequestDTO::getProductId)
                .collect(Collectors.toList());
        if (productService.countExistentByIds(productIds) < productIds.size()){
            throw new NotFoundException(ML001);
        }
    }

    private void verifyProductsTemperatureMatchesSection(Warehouse warehouse, String sectionIdToInsert,
                                                         List<BatchProductRequestDTO> productToInsert){
        Section section = warehouse.getWarehouseSections()
                .stream().map(el-> el.getSection())
                .filter(el -> el.getId().equals(sectionIdToInsert))
                .findFirst()
                .get();
        Byte maxTemperature = section.getMaxTemperature();
        Byte minTemperature = section.getMinTemperature();
        productToInsert.forEach(el -> {
            if (el.getMinimumTemperature() < minTemperature || el.getMinimumTemperature() >= maxTemperature){
                throw new BadRequestException(ML104);}
        });
    }

    public void verifyOrderExistence(Integer orderId) {
        if (insertOrderRepository.existsById(orderId)){
            throw new ApiException(ML702);
        }
    }

    private Integer spaceLeftInSection(WarehousesSections warehousesSections, String sectionIdToInsert){
        Integer stockLimit = warehousesSections.getStockLimit();
        Integer currentStock = warehousesSections
                .getBatchProducts()
                .stream()
                .mapToInt(BatchProduct::getCurrentQuantity)
                .reduce(0, (acc, current) -> acc+current);
        return stockLimit-currentStock;
    }

    private void verifyWarehouseSectionStillHasRoomInsert(WarehousesSections warehousesSections, List<BatchProductRequestDTO> productToInsert,
                                                          String sectionIdToInsert) {
        Integer totalProductsToAdd = productToInsert
                .stream()
                .mapToInt(el -> el.getCurrentQuantity())
                .reduce(0,(acc, current) -> acc + current);

        Integer spaceLeft = spaceLeftInSection(warehousesSections, sectionIdToInsert);

        if (totalProductsToAdd > spaceLeft){
            throw new ApiException(ML102);
        }
    }

    private void verifyNoDuplicateProductBatches(List<BatchProductRequestDTO> products){
        if (products.size() > new HashSet<>(products).size()){
            throw new BadRequestException(ML602);
        }
    }

}

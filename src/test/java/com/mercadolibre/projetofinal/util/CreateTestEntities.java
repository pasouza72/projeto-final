package com.mercadolibre.projetofinal.util;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.request.*;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.enums.AccountRolesEnum;
import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.enums.OrderStatusEnum;
import com.mercadolibre.projetofinal.enums.SectionCategoryEnum;
import com.mercadolibre.projetofinal.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CreateTestEntities {

    public static final PurchaseOrderResponseDTO purchaseOrderResponseDTO = createPurchaseOrderResponseDTO();
    public static final PurchaseRequestDTO purchaseRequestDTO = createPurchaseRequestDTO();
    public static final List<Product> products = createProductList();
    public static final List<ProductsRequestDTO> productsRequestDTO = createProductsRequestDTOList();
    public static final AccountDTO accountDTO = createAccountDto();
    public static final Warehouse warehouse = createWarehouse();
    public static final Section section = createSection();
    public static final Account account = createAccount();
    public static final WarehousesSections warehousesSections = createWarehouseSections();
    public static final List<BatchProduct> newBatchProducts = createNewBatchProducts();
    public static final List<BatchProduct> databaseProducts = createDatabaseBatchProducts();
    public static final InsertOrder insertOrder = createInsertOrder();
    public static final OrderRequestDTO orderRequest = createOrderRequest();
    public static final String representativeToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJjb3VudHJ5IjoyLCJzdWIiOiJkY2E0N2JiOC01NzRhLTQwMTktYmMzMS0xNDgxMTFjOTAzYjAiLCJ3YXJlaG91c2UiOiJiZTg1MzBkMS04YmQxLTRkODAtYTQxMS04YTM3OTQwOGI2YjUiLCJleHAiOjE2MjU3NjcxNDEwMDAwMCwiaWF0IjoxNjI1NzY2NTQxLCJhdXRob3JpdGllcyI6WyJST0xFX1JFUFJFU0VOVEFUSVZFIl0sInVzZXJuYW1lIjoidXNlcl9vbmUifQ.9hv1W-mjMToUmCYxmJpT8f7a7bfd3P_sWVVU-gRua-aIoUq-hB4antSbTrcOHqfYycmXqWkYSOx2v7sM1bvxng";
    public static final String buyerToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJjb3VudHJ5IjoyLCJzdWIiOiJkY2E0N2JiOC01NzRhLTQwMTktYmMzMS0xNDgxMTFjOTAzYjAiLCJ3YXJlaG91c2UiOiJiZTg1MzBkMS04YmQxLTRkODAtYTQxMS04YTM3OTQwOGI2YjUiLCJleHAiOjE2MjU3NjcxNDEwMDAwMCwiaWF0IjoxNjI1NzY2NTQxLCJhdXRob3JpdGllcyI6WyJST0xFX1JFUFJFU0VOVEFUSVZFIl0sInVzZXJuYW1lIjoidXNlcl9vbmUifQ.9hv1W-mjMToUmCYxmJpT8f7a7bfd3P_sWVVU-gRua-aIoUq-hB4antSbTrcOHqfYycmXqWkYSOx2v7sM1bvxng";

    public static InsertOrder createInsertOrder() {
        return new InsertOrder(0, warehousesSections, null, newBatchProducts, LocalDateTime.now());
    }

    public static Account createAccount() {
        return new Account("user1", "userteste", CountryEnum.BRAZIL, "password", AccountRolesEnum.REPRESENTATIVE, warehouse, new HashSet<>());
    }

    public static Section createSection() {
        return new Section("section1", "Section Teste 1", Byte.valueOf("4"), Byte.valueOf("15"), new HashSet<>(), SectionCategoryEnum.FF);
    }

    public static Warehouse createWarehouse() {
        return new Warehouse("warehouse1", "Warehouse Teste 1", CountryEnum.BRAZIL, new HashSet(List.of(accountDTO)), new HashSet<>());
    }

    public static WarehouseDTO createWarehouseDTO() {
        Warehouse warehouse = createWarehouse();
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setName(warehouse.getName());
        return warehouseDTO;
    }

    public static AccountDTO createAccountDto(){
        return new AccountDTO("user1", "userteste", "warehouse1", CountryEnum.BRAZIL.getId());
    }

    public static WarehousesSections createWarehouseSections(){
        WarehousesSections warehousesSections = new WarehousesSections("warehousessection1", warehouse, section, 500, new HashSet<>());
        warehouse.getWarehouseSections().add(warehousesSections);
        section.getWarehouseSections().add(warehousesSections);
        return warehousesSections;
    }

    public static List<Product> createProductList(){
        return new ArrayList(List.of(
                new Product("abc1", "Teste produto 1", 10.99, "Meu produto 1", new ArrayList<>()),
                new Product("abc2", "Teste produto 2", 11.99, "Meu produto 2", new ArrayList<>()),
                new Product("abc3", "Teste produto 3", 12.99, "Meu produto 3", new ArrayList<>()),
                new Product("abc4", "Teste produto 4", 13.99, "Meu produto 4", new ArrayList<>())));
    }

    public static List<BatchProduct> createNewBatchProducts(){
        return new ArrayList<>(List.of(
                new BatchProduct(null, 1231, 10.1, 5.0, 10, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(0), warehousesSections, null),
                new BatchProduct(null, 121, 10.3, 5.0, 5, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(1), warehousesSections, null),
                new BatchProduct(null, 122, 10.0, 5.0, 7, 15, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(1), warehousesSections, null)
        ));
    }

    public static List<ProductsRequestDTO> createProductsRequestDTOList(){
        return new ArrayList(List.of(
                new ProductsRequestDTO("abc1", 20),
                new ProductsRequestDTO("abc2", 40),
                new ProductsRequestDTO("abc3", 60),
                new ProductsRequestDTO("abc4", 80)));
    }

    public static List<BatchProduct> createDatabaseBatchProducts(){
        List<BatchProduct> databaseProductsInner = new ArrayList<>(List.of(
                new BatchProduct(null, 1231, 10.1, 5.0, 10, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(0), warehousesSections, null),
                new BatchProduct(null, 121, 10.3, 5.0, 5, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(1), warehousesSections, null),
                new BatchProduct(null, 14, 10.0, 5.0, 7, 15, LocalDate.now(), LocalDateTime.now(), LocalDate.now(),
                        products.get(1), warehousesSections, null)
        ));
        warehousesSections.getBatchProducts().addAll(databaseProductsInner);
        return databaseProductsInner;
    }

    public static OrderRequestDTO createOrderRequest(){
        SectionOrderRequestDTO section = new SectionOrderRequestDTO("section1", "section2");

        List<BatchProductRequestDTO> productsDTO = new ArrayList<>(List.of(
                new BatchProductRequestDTO("abc1", 1231, 10.1, 5.0, 10, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now()),
                new BatchProductRequestDTO("abc2", 121, 10.3, 5.0, 5, 20, LocalDate.now(), LocalDateTime.now(), LocalDate.now()),
                new BatchProductRequestDTO("abc3", 122, 10.0, 5.0, 7, 15, LocalDate.now(), LocalDateTime.now(), LocalDate.now()),
                new BatchProductRequestDTO("abc4", 124, 10.2, 5.0, 7, 15, LocalDate.now(), LocalDateTime.now(), LocalDate.now())
        ));
        InboundOrderRequestDTO inboundOrderRequestDTO = new InboundOrderRequestDTO(12, LocalDate.now(), section, productsDTO);
        return new OrderRequestDTO(inboundOrderRequestDTO);
    }

    public static PurchaseRequestDTO createPurchaseRequestDTO(){
        return new PurchaseRequestDTO(new PurchaseOrderRequestDTO("id1", LocalDate.now(),
                new OrderStatusRequestDTO(OrderStatusEnum.NEW), productsRequestDTO));
    }

    public static PurchaseOrderResponseDTO createPurchaseOrderResponseDTO(){
        return new PurchaseOrderResponseDTO("id1", LocalDate.now(), new OrderStatusRequestDTO(OrderStatusEnum.NEW),
                List.of(new PurchaseOrderProductResponseDTO("id1", "product1", 100)));
    }
}

package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.OrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.BatchProductResponseDTO;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.exceptions.BadRequestException;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.model.WarehousesSections;
import com.mercadolibre.projetofinal.repository.InsertOrderRepository;
import com.mercadolibre.projetofinal.service.*;
import com.mercadolibre.projetofinal.service.impl.InsertOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.*;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.*;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.newBatchProducts;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InsertOrderServiceTest {

    @Mock
    private IProductService productService;
    @Mock
    private IWarehouseService warehouseService;
    @Mock
    private IBatchProductService batchProductService;
    @Mock
    private IWarehousesSectionsService warehousesSectionsService;
    @Mock
    private ISessionService sessionService;
    @Mock
    private InsertOrderRepository insertOrderRepository;
    @InjectMocks
    private InsertOrderService insertOrderService;


    @Test
    void testCreateOrderWorks(){
        // given -> orderRequest, account, newBatchProducts, warehousesSections, account, insertOrder (Utils)

        //when
        setupCreateOrderMocks();
        setupInsertOrderValidationMocks();

        List<BatchProductResponseDTO> finalBatchProducts = insertOrderService.createOrder(orderRequest, accountDTO);

        //assert
        assertEquals(finalBatchProducts.size(), newBatchProducts.size());
        assertEquals(1231, finalBatchProducts.get(0).getBatchNumber());

    }

    @Test
    void testUpdateOrderWorks(){
        // given -> orderRequest, account, newBatchProducts, warehousesSections, account, insertOrder (Utils)

        //when
        setupUpdateOrderMocks();
        setupInsertOrderValidationMocks();

        List<BatchProductResponseDTO> finalBatchProducts = insertOrderService.updateOrder(orderRequest, accountDTO);

        //assert
        assertEquals(finalBatchProducts.size(), newBatchProducts.size());
        assertEquals(1231, finalBatchProducts.get(0).getBatchNumber());

    }

    @Test
    void testInsertOrderFailsWhenWarehouseSectionDoesntHaveRoom(){
        // given -> orderRequest, accountDTO, warehouse, section, databaseProducts (Utils)

        WarehousesSections updatedWhs = new WarehousesSections("whs2", warehouse, section, 70, new HashSet<>(databaseProducts));

        //when
        setupCreateOrderMocks(updatedWhs);

        //assert
        ApiException apiException = assertThrows(ApiException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("There is not enough room in this Section to store all products"));
        assertEquals(apiException.getCode(), ML102.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.CONFLICT.value());

    }


    @Test
    void testInsertOrderFailsWhenDuplicateBatch(){
        // given -> orderRequest, accountDTO, warehouse, section, databaseProducts (Utils)

        OrderRequestDTO orderRequest = createOrderRequest();
        orderRequest.getInboundOrder().getBatchStock().add(orderRequest.getInboundOrder().getBatchStock().get(0));

        //when
        setupCreateOrderMocks();

        //assert
        ApiException apiException = assertThrows(BadRequestException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("There is at least one duplicate Product Batch"));
        assertEquals(apiException.getCode(), ML602.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.BAD_REQUEST.value());

    }


    @Test
    void testInsertOrderFailsWhenOrderAlreadyExists(){
        // given -> orderRequest, accountDTO (Utils)

        //when
        when(insertOrderRepository.existsById(any())).thenReturn(true);

        //assert
        assertThrows(ApiException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));

        verify(insertOrderRepository, times(1)).existsById(any());

    }

    @Test
    void testInsertOrderFailsWhenRepresentativeDoesntBelongToWarehouse(){
        // given -> orderRequest, warehouse, section, databaseProducts (Utils)
        AccountDTO accountDTO = createAccountDto();
        accountDTO.setWarehouse("different warehouse");

        //when
        setupCreateOrderMocks();

        //assert
        ApiException apiException = assertThrows(ApiException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("Representative does not belong to given Warehouse"));
        assertEquals(apiException.getCode(), ML202.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.FORBIDDEN.value());

    }

    @Test
    void testInsertOrderFailsWhenSectionDoesNotBelongToWarehouse(){
        // given -> accountDTO, warehouse, section, databaseProducts (Utils)
        OrderRequestDTO orderRequest = createOrderRequest();
        orderRequest.getInboundOrder().getSection().setSectionCode("different section");

        //when
        setupCreateOrderMocks();

        //assert
        ApiException apiException = assertThrows(ApiException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("The given section does not exist in this warehouse"));
        assertEquals(apiException.getCode(), ML103.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.NOT_FOUND.value());

    }

    @Test
    void testInsertOrderFailsWhenProductDoesNotExist(){
        // given -> orderRequest, accountDTO, warehouse, section, databaseProducts (Utils)

        //when
        setupCreateOrderMocks();
        when(productService.countExistentByIds(any())).thenReturn(0);

        //assert
        NotFoundException apiException = assertThrows(NotFoundException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("At least one of the given products was not found"));
        assertEquals(apiException.getCode(), ML001.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testInsertOrderFailsWhenProductTemperatureDoesntMatchSection(){
        // given ->  accountDTO, warehouse, section, databaseProducts (Utils)
        OrderRequestDTO orderRequest = createOrderRequest();
        orderRequest.getInboundOrder().getBatchStock().get(0).setMinimumTemperature(20.0);

        //when
        setupCreateOrderMocks();
        when(productService.countExistentByIds(any())).thenReturn(orderRequest.getInboundOrder().getBatchStock().size());

        //assert
        BadRequestException apiException = assertThrows(BadRequestException.class, () -> insertOrderService.createOrder(orderRequest, accountDTO));
        assertTrue(apiException.getDescription().contains("At least one product cannot be stored in this Section"));
        assertEquals(apiException.getCode(), ML104.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testUpdateOrderFailsWhenNotEnoughSpace(){
        // given -> account, newBatchProducts, warehousesSections, account, insertOrder (Utils)

        List<BatchProduct> newBatchProducts = createNewBatchProducts();
        newBatchProducts.get(0).setCurrentQuantity(470);
        //when
        setupUpdateOrderMocks();
        when(productService.countExistentByIds(any())).thenReturn(orderRequest.getInboundOrder().getBatchStock().size());
        when(batchProductService.convertToModel(any(), any())).thenReturn(newBatchProducts);

        ApiException apiException = assertThrows(ApiException.class, () -> insertOrderService.updateOrder(orderRequest, accountDTO));


        //assert
        assertTrue(apiException.getDescription().contains("There is not enough room in this Section to store all products"));
        assertEquals(apiException.getCode(), ML102.getCode());
        assertEquals(apiException.getStatusCode(), HttpStatus.CONFLICT.value());

    }

    @Test
    void findByIdException() {
        when(insertOrderRepository.findById(any()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            insertOrderService.findById(1);
        });

        assertEquals(ML701.getDescription(), exception.getMessage());
    }


    private void setupCreateOrderMocks(){
        when(insertOrderRepository.existsById(any())).thenReturn(false);
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(warehousesSectionsService.findByWarehouseIdAndSectionId(any(), any())).thenReturn(warehousesSections);
    }

    private void setupCreateOrderMocks(WarehousesSections warehousesSections){
        when(insertOrderRepository.existsById(any())).thenReturn(false);
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(warehousesSectionsService.findByWarehouseIdAndSectionId(any(), any())).thenReturn(warehousesSections);
    }

    private void setupUpdateOrderMocks(){
        when(insertOrderRepository.findById(any())).thenReturn(Optional.of(insertOrder));
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(warehousesSectionsService.findByWarehouseIdAndSectionId(any(), any())).thenReturn(warehousesSections);
    }

    private void setupInsertOrderValidationMocks(){
        when(productService.countExistentByIds(any())).thenReturn(orderRequest.getInboundOrder().getBatchStock().size());
        when(batchProductService.convertToModel(any(), any())).thenReturn(newBatchProducts);
        when(sessionService.getAccountById(any())).thenReturn(account);
        when(insertOrderRepository.save(any())).thenReturn(insertOrder);
    }





}

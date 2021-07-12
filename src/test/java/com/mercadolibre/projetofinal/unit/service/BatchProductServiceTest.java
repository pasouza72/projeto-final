package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.dtos.request.BatchProductRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.ListBatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductStockCountResponseDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.exceptions.ProductsOutOfStockException;
import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.repository.BatchProductRepository;
import com.mercadolibre.projetofinal.service.impl.BatchProductService;
import com.mercadolibre.projetofinal.service.impl.ProductService;
import com.mercadolibre.projetofinal.service.impl.WarehouseService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML101;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchProductServiceTest {

    @Mock
    WarehouseService warehouseService;

    @Mock
    BatchProductRepository batchProductRepository;

    @Mock
    ProductService productService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    BatchProductService batchProductService;

    @Test
    void shouldReturnProductsOutOfStockException(){
        List<ProductStockCountResponseDTO> currentProductStock =  List.of(
                new ProductStockCountResponseDTOImpl("abc1", 0),
                new ProductStockCountResponseDTOImpl("abc2", 0));

        when(batchProductRepository.getStockProductsByUsername(any(), any())).thenReturn(currentProductStock);

        assertThrows(ProductsOutOfStockException.class,
                () -> batchProductService.validStockOfProductsByCountry(products, productsRequestDTO, "user_five"),
                "Products out of stock");
    }

    @Test
    void updateQuantityToSaveNewBatchProducts(){
        List<BatchProduct> batchProducts = newBatchProducts;

        when(batchProductRepository.findAllBatchByProductsAndCountry(any(), any()))
                .thenReturn(Optional.of(batchProducts));

        List<ProductsRequestDTO> productToUpdate =  List.of(
                new ProductsRequestDTO("abc1", 1),
                new ProductsRequestDTO("abc2", 21));

        batchProductService.updateStock(productToUpdate,0);

        assertEquals(19,batchProducts.get(0).getCurrentQuantity());
        assertEquals(0,batchProducts.get(1).getCurrentQuantity());
    }

    @Test
    void findBatchesByRepresentativeAndDueDate(){
        List<BatchProduct> batchProducts = newBatchProducts;
        Page<BatchProduct> pages = new PageImpl<>(batchProducts);

        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(batchProductRepository.findByWarehouseIdAndDueDate(any(), any(), any(Pageable.class)))
                .thenReturn(pages);

        Page<BatchProduct> actual = batchProductService.findBatchesByRepresentativeAndDueDate("", 0, PageRequest.of(0, 1));

        assertEquals(pages, actual);
    }

    @Test
    void findBatchesByRepresentativeAndDueDateAndCategory(){
        List<BatchProduct> batchProducts = newBatchProducts;
        Page<BatchProduct> pages = new PageImpl<>(batchProducts);

        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(batchProductRepository.findByWarehouseIdAndDueDateAndCategory(any(), any(), any(), any(Pageable.class)))
                .thenReturn(pages);

        Page<BatchProduct> actual = batchProductService.findBatchesByRepresentativeAndDueDateAndCategory("", 0, "FF", PageRequest.of(0, 1));

        assertEquals(pages, actual);
    }

    @Test
    void findBatchesByRepresentativeAndDueDateAndCategoryException(){
        List<BatchProduct> batchProducts = newBatchProducts;
        Page<BatchProduct> pages = new PageImpl<>(batchProducts);

        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            batchProductService.findBatchesByRepresentativeAndDueDateAndCategory("", 0, "ABC", PageRequest.of(0, 1));
        });

        assertEquals(ML101.getDescription(), exception.getMessage());
    }

    @Test
    void testConvertToModelWorks(){
        //given
        List<BatchProductRequestDTO> batchStockDTO = orderRequest.getInboundOrder().getBatchStock();

        //when
        when(productService.findAllById(any())).thenReturn(products);

        List<BatchProduct> batchProducts = batchProductService.convertToModel(batchStockDTO, warehousesSections);

        //assert
        assertEquals(batchProducts.get(0).getBatchNumber(), batchStockDTO.get(0).getBatchNumber());
        assertEquals(batchStockDTO.size(), batchProducts.size());

    }

    @Test
    void testConvertToModelFailsWhenProductDoesNotExist(){
        //given
        List<BatchProductRequestDTO> batchStockDTO = orderRequest.getInboundOrder().getBatchStock();
        List<Product> databaseProducts = createProductList();
        databaseProducts.remove(0);

        //when
        when(productService.findAllById(any())).thenReturn(databaseProducts);

        //assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> batchProductService.convertToModel(batchStockDTO, warehousesSections));
        assertTrue(exception.getMessage().contains("At least one of the given products was not found"));
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND.value());

    }

    @Test
    void testFindBatchProductsByProductIdWorks(){

        Page p = new PageImpl(databaseProducts);
        PageRequest pageRequest = PageRequest.of(0,20);

        //when
        when(batchProductRepository.findBatchesInWarehouseByProductId(any(), any(), any())).thenReturn(p);
        Page<BatchProduct> result = batchProductService.findBatchProductsByProductId(products.get(0).getId(), warehouse, pageRequest);
        //assert
        assertEquals(result.getContent().get(0).getBatchNumber(), databaseProducts.get(0).getBatchNumber());
        assertEquals(result.getContent().size(), databaseProducts.size());

    }

    @Test
    void testFindBatchesForRepresentativeWorks(){
        //given
        PageRequest pageRequest = PageRequest.of(0,20);
        Page p = new PageImpl(databaseProducts,pageRequest,50);


        //when
        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(batchProductRepository.findBatchesInWarehouseByProductId(any(),any(),any())).thenReturn(p);
        ListBatchStockSimplifiedResponseDTO response = batchProductService.findBatchesForRepresentative(products.get(0).getId(), "ab", pageRequest);

        //assert
        assertEquals(response.getBatchStock().size(), databaseProducts.size());
        assertEquals(response.getBatchStock().get(0).getBatchId(), databaseProducts.get(0).getId());
    }

    @Test
    void testFindBatchesForRepresentativeFailsWhenNoBatchProductsFound(){
        //given
        PageRequest pageRequest = PageRequest.of(0,20);
        Page p = new PageImpl(List.of());

        //when
        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(batchProductRepository.findBatchesInWarehouseByProductId(any(),any(),any())).thenReturn(p);

        //assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> batchProductService.findBatchesForRepresentative(products.get(0).getId(),"a",pageRequest));
        assertTrue(exception.getMessage().contains("Batch Stock for given Product not found for the Representative Warehouse"));

    }

    @Test
    void testFindBatchesForRepresentativeFailsWhenSectionIsNotFound(){
        //given
        PageRequest pageRequest = PageRequest.of(0,20);
        Page p = new PageImpl(List.of(),pageRequest,50);

        //when
        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(warehouseService.findWarehouseById(any())).thenReturn(warehouse);
        when(batchProductRepository.findBatchesInWarehouseByProductId(any(),any(),any())).thenReturn(p);

        //assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> batchProductService.findBatchesForRepresentative(products.get(0).getId(),"a",pageRequest));
        assertTrue(exception.getMessage().contains("Page out of bounds"));

    }



    @Test
    void shouldReturnQuantityWhenHasBatchAvailable(){
        //Mock
        Integer userCountry = 0;
        String productId = "productId";
        BatchProduct batchProduct = BatchProduct.builder().currentQuantity(3).build();

        Mockito.when(batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry))
                .thenReturn(Optional.of(Collections.singletonList(batchProduct)));

        //Method call
        int quantity = batchProductService.getQuantityOfBatchAvailableByCountryAndProductId(userCountry, productId);

        assertEquals(3, quantity);
    }

    @Test
    void shouldReturnExceptionWhenHasNotBatchProductsAvailable(){
        //Mock
        Integer userCountry = 0;
        String productId = "productId";

        Mockito.when(batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry))
                .thenReturn(Optional.empty());

        //Method call
        assertThrows( NotFoundException.class, () ->
                        batchProductService.getQuantityOfBatchAvailableByCountryAndProductId(userCountry, productId)
                );
    }

    @Test
    void shouldRemoveProductFromBatchWhenQuantityProductToRemoveIsEqualCurrencyQuantityOnDatabase(){
        //Mock
        Integer userCountry = 0;
        String productId = "productId";
        BatchProduct batchProduct = BatchProduct.builder().currentQuantity(3).build();
        Optional<List<BatchProduct>> batchProductsOptional = Optional.of(Collections.singletonList(batchProduct));

        Mockito.when(batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry))
                .thenReturn(batchProductsOptional);

        Mockito.when(batchProductRepository.saveAll(Mockito.anyList()))
                .thenAnswer(i -> i.getArguments()[0]);

        //Method call
        batchProductService.removeProductFromBatch(productId, 3, userCountry);

        //Assertion
        List<BatchProduct> batchProducts = batchProductsOptional.get();
        assertEquals(0, batchProducts.get(0).getCurrentQuantity());
    }

    @Test
    void shouldRemoveProductWhenHasMoreThanOneBatchProduct(){
        //Mock
        Integer userCountry = 0;
        String productId = "productId";
        BatchProduct batchProduct = BatchProduct.builder().currentQuantity(3).build();
        BatchProduct batchProduct2 = BatchProduct.builder().currentQuantity(3).build();
        Optional<List<BatchProduct>> batchProductsOptional = Optional.of(Arrays.asList(batchProduct, batchProduct2));

        Mockito.when(batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry))
                .thenReturn(batchProductsOptional);

        Mockito.when(batchProductRepository.saveAll(Mockito.anyList()))
                .thenAnswer(i -> i.getArguments()[0]);

        //Method call
        batchProductService.removeProductFromBatch(productId, 5, userCountry);

        //Assertion
        List<BatchProduct> batchProducts = batchProductsOptional.get();
        assertEquals(1, batchProducts.get(1).getCurrentQuantity());
    }

    @Test
    void shouldIncrementProductOnBatch(){
        //Mock
        Integer userCountry = 0;
        String productId = "productId";
        BatchProduct batchProduct = BatchProduct.builder().currentQuantity(3).build();
        Optional<List<BatchProduct>> batchProductsOptional = Optional.of(Collections.singletonList(batchProduct));

        Mockito.when(batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry))
                .thenReturn(batchProductsOptional);

        Mockito.when(batchProductRepository.save(Mockito.any()))
                .thenAnswer(i -> i.getArguments()[0]);

        //Method call
        batchProductService.incrementProductOnBatch(productId, 3, userCountry);

        //Assert
        List<BatchProduct> batchProducts = batchProductsOptional.get();
        assertEquals(6, batchProducts.get(0).getCurrentQuantity());
    }

    @AllArgsConstructor
    @Data
    public static class ProductStockCountResponseDTOImpl implements ProductStockCountResponseDTO{
        private final String productId;
        private final Integer currentQuantity;
    }
}

package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.OrderStatusRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseOrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseValueResponseDTO;
import com.mercadolibre.projetofinal.enums.OrderStatusEnum;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.PurchaseOrder;
import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;
import com.mercadolibre.projetofinal.repository.PurchaseOrderRepository;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PurchaseOrderServiceTest {

    @InjectMocks private PurchaseOrderService purchaseOrderService;
    @Mock private JwtUtil jwtUtil;
    @Mock private ProductsFromPurchaseService productsFromPurchaseService;
    @Mock private PurchaseOrderRepository purchaseOrderRepository;
    @Mock private PurchaseOrdersProductsService purchaseOrdersProductsService;
    @Mock private BatchProductService batchProductService;
    @Mock private ValidStockProductService validProductsStock;


    private final static Integer QUANTITY = 5;
    private final static Integer COUNTRY_ID = 8;
    private final static String PRODUCT_ID = "2";
    private final static Double PRODUCT_PRICE = 50.0;
    private final static String ORDER_ID = "2";

    @Test
    void shouldReturnTotalPriceEquals250WhenHasFiveProductAndYourPriceIsEqualsFifty(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).price(PRODUCT_PRICE).build();
        List<Product> products = Collections.singletonList(product);

        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().quantity(QUANTITY).productId(PRODUCT_ID).build();
        List<ProductsRequestDTO> productsRequestDTOS = Collections.singletonList(productsRequestDTO);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().products(productsRequestDTOS)
                .orderStatus(new OrderStatusRequestDTO(OrderStatusEnum.NEW)).build();
        PurchaseRequestDTO purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        AccountDTO accountDTO = AccountDTO.builder().country(COUNTRY_ID).build();

        Mockito.when(jwtUtil.getAccountDTO(""))
                .thenReturn(accountDTO);

        Mockito.when(productsFromPurchaseService.getProductsFromRequestByAccount(purchaseRequestDTO, accountDTO ))
                .thenReturn(products);

        Mockito.when(purchaseOrderRepository.save(Mockito.any(PurchaseOrder.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Mockito.when(purchaseOrdersProductsService.save(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn( Collections.singletonList(new PurchaseOrdersProducts(product, QUANTITY))) ;


        //Method call
        PurchaseValueResponseDTO response = purchaseOrderService.create(purchaseRequestDTO, "");

        //Assert
        assertEquals(250.0, response.getTotalPrice() );
    }

    @Test
    void shouldReturnListOfProductWhenPurchaseOrderExist(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).build();
        PurchaseOrdersProducts purchaseOrdersProducts = PurchaseOrdersProducts.builder().product(product).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().purchaseOrdersProducts(Collections.singletonList(purchaseOrdersProducts)).build();

        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(purchaseOrder));

        //Method call
        var responseDTOList = purchaseOrderService.getProductsOfPurchase(ORDER_ID);

        //Assert
        assertEquals(PRODUCT_ID, responseDTOList.get(0).getId() );

    }

    @Test
    void shouldReturnExceptionWhenPurchaseOrderNotExist(){
        //Mock
        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.empty());

        //Method call
        assertThrows(NotFoundException.class, () ->
            purchaseOrderService.getProductsOfPurchase(ORDER_ID)
        );
    }

    @Test
    void shouldUpdatePurchaseOrderWhenItExistAndThereIsOnlyOneLessQuantityOfProductInRequest(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).price(PRODUCT_PRICE).build();

        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().quantity(QUANTITY).productId(PRODUCT_ID).build();
        List<ProductsRequestDTO> productsRequestDTOS = Collections.singletonList(productsRequestDTO);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().id(ORDER_ID).products(productsRequestDTOS)
                .orderStatus(new OrderStatusRequestDTO(OrderStatusEnum.NEW)).build();
        PurchaseRequestDTO purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        PurchaseOrdersProducts purchaseOrdersProducts = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = new ArrayList<>();
        purchaseOrdersProductsList.add(purchaseOrdersProducts);

        PurchaseOrdersProducts purchaseOrdersProductsToMock = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY - 1).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsToMockList = new ArrayList<>();
        purchaseOrdersProductsToMockList.add(purchaseOrdersProductsToMock);

        PurchaseOrder purchaseOrder = PurchaseOrder.builder().purchaseOrdersProducts( purchaseOrdersProductsList ).build();

        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(purchaseOrder));

        Mockito.when(purchaseOrdersProductsService.save(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(purchaseOrdersProductsToMockList);

        //Method call
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.update(purchaseRequestDTO, "");

        //Assert
        assertEquals(QUANTITY - 1, responseDTO.getProducts().get(0).getQuantity());

        //Verify
        Mockito.verify(batchProductService).incrementProductOnBatch(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldUpdatePurchaseOrderWhenItExistAndThereIsOnlyOneMoreQuantityOfProductInRequest(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).price(PRODUCT_PRICE).build();

        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().quantity(QUANTITY + 1).productId(PRODUCT_ID).build();
        List<ProductsRequestDTO> productsRequestDTOS = Collections.singletonList(productsRequestDTO);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().id(ORDER_ID).products(productsRequestDTOS)
                .orderStatus(new OrderStatusRequestDTO(OrderStatusEnum.NEW)).build();
        PurchaseRequestDTO purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        PurchaseOrdersProducts purchaseOrdersProducts = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = new ArrayList<>();
        purchaseOrdersProductsList.add(purchaseOrdersProducts);

        PurchaseOrdersProducts purchaseOrdersProductsToMock = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY + 1).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsToMockList = new ArrayList<>();
        purchaseOrdersProductsToMockList.add(purchaseOrdersProductsToMock);

        PurchaseOrder purchaseOrder = PurchaseOrder.builder().purchaseOrdersProducts( purchaseOrdersProductsList ).build();

        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(purchaseOrder));

        Mockito.when(purchaseOrdersProductsService.save(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(purchaseOrdersProductsToMockList);

        //Method call
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.update(purchaseRequestDTO, "");

        //Assert
        assertEquals(QUANTITY + 1, responseDTO.getProducts().get(0).getQuantity());

        //Verify
        Mockito.verify(batchProductService).removeProductFromBatch(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldUpdatePurchaseOrderWhenThereAreTwoProductsOnDatabaseAndOnlyOneProductOnRequest(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).price(PRODUCT_PRICE).build();
        Product product2 = Product.builder().id("3").price(PRODUCT_PRICE).build();

        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().quantity(QUANTITY + 1).productId(PRODUCT_ID).build();
//        ProductsRequestDTO productsRequestDTO2 = ProductsRequestDTO.builder().quantity(1).productId("3").build();
        List<ProductsRequestDTO> productsRequestDTOS = Arrays.asList(productsRequestDTO);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().id(ORDER_ID).products(productsRequestDTOS)
                .orderStatus(new OrderStatusRequestDTO(OrderStatusEnum.NEW)).build();
        PurchaseRequestDTO purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        PurchaseOrdersProducts purchaseOrdersProducts = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY).build();
        PurchaseOrdersProducts purchaseOrdersProducts2 = PurchaseOrdersProducts.builder().product(product2).quantity(1).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = new ArrayList<>();
        purchaseOrdersProductsList.add(purchaseOrdersProducts);
        purchaseOrdersProductsList.add(purchaseOrdersProducts2);

        PurchaseOrdersProducts purchaseOrdersProductsToMock = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY + 1).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsToMockList = new ArrayList<>();
        purchaseOrdersProductsToMockList.add(purchaseOrdersProductsToMock);

        PurchaseOrder purchaseOrder = PurchaseOrder.builder().purchaseOrdersProducts( purchaseOrdersProductsList ).build();

        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(purchaseOrder));

        Mockito.when(purchaseOrdersProductsService.save(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(purchaseOrdersProductsToMockList);

        //Method call
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.update(purchaseRequestDTO, "");

        //Assert
        assertEquals(1, responseDTO.getProducts().size());
        assertEquals(PRODUCT_ID, responseDTO.getProducts().get(0).getId());

    }

    @Test
    void shouldUpdatePurchaseOrderWhenThereIsAProductOnRequestThatIsNotOnDatabase(){
        //Mock
        Product product = Product.builder().id(PRODUCT_ID).price(PRODUCT_PRICE).build();

        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().quantity(QUANTITY + 1).productId(PRODUCT_ID).build();
        ProductsRequestDTO productsRequestDTO2 = ProductsRequestDTO.builder().quantity(1).productId("3").build();
        List<ProductsRequestDTO> productsRequestDTOS = Arrays.asList(productsRequestDTO, productsRequestDTO2);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().id(ORDER_ID).products(productsRequestDTOS)
                .orderStatus(new OrderStatusRequestDTO(OrderStatusEnum.NEW)).build();
        PurchaseRequestDTO purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        PurchaseOrdersProducts purchaseOrdersProducts = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = new ArrayList<>();
        purchaseOrdersProductsList.add(purchaseOrdersProducts);

        PurchaseOrdersProducts purchaseOrdersProductsToMock = PurchaseOrdersProducts.builder().product(product).quantity(QUANTITY + 1).build();
        List<PurchaseOrdersProducts> purchaseOrdersProductsToMockList = new ArrayList<>();
        purchaseOrdersProductsToMockList.add(purchaseOrdersProductsToMock);

        PurchaseOrder purchaseOrder = PurchaseOrder.builder().purchaseOrdersProducts( purchaseOrdersProductsList ).build();

        Mockito.when(purchaseOrderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(purchaseOrder));

        Mockito.when(purchaseOrdersProductsService.save(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(purchaseOrdersProductsToMockList);

        //Method call
        PurchaseOrderResponseDTO responseDTO = purchaseOrderService.update(purchaseRequestDTO, "");

        //Assert
        assertEquals(1, responseDTO.getProducts().size());
        assertEquals(PRODUCT_ID, responseDTO.getProducts().get(0).getId());

    }
}
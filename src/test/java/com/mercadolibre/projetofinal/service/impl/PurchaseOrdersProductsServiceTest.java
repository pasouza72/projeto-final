package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.PurchaseOrder;
import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;
import com.mercadolibre.projetofinal.repository.PurchaseOrdersProductsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PurchaseOrdersProductsServiceTest {

    @InjectMocks private PurchaseOrdersProductsService service;
    @Mock private PurchaseOrdersProductsRepository repository;

    private final static String PRODUCT_ID = "1";
    private final static String PURCHASE_ORDER_ID = "2";
    private final static Integer QUANTITY = 5;



    @Test
    void shouldValidPurchaseOrderWhenSavePurchaseOrdersProducts(){
        //Method call
        List<PurchaseOrdersProducts> purchaseOrdersProductsSaved = save();

        //Assertion
        PurchaseOrdersProducts purchaseOrdersProduct = purchaseOrdersProductsSaved.get(0);
        assertEquals(PURCHASE_ORDER_ID, purchaseOrdersProduct.getPurchaseOrder().getId());
    }

    @Test
    void shouldValidProductWhenSavePurchaseOrdersProducts(){
        //Method call
        List<PurchaseOrdersProducts> purchaseOrdersProductsSaved = save();

        //Assertion
        PurchaseOrdersProducts purchaseOrdersProduct = purchaseOrdersProductsSaved.get(0);
        assertEquals(PRODUCT_ID, purchaseOrdersProduct.getProduct().getId());
    }

    @Test
    void shouldValidQuantityWhenSavePurchaseOrdersProducts(){
        //Method call
        List<PurchaseOrdersProducts> purchaseOrdersProductsSaved = save();

        //Assertion
        PurchaseOrdersProducts purchaseOrdersProduct = purchaseOrdersProductsSaved.get(0);
        assertEquals(QUANTITY, purchaseOrdersProduct.getQuantity());
    }

    private List<PurchaseOrdersProducts> save() {
        //Mock
        List<Product> products = getProducts();
        PurchaseOrder purchaseOrder = getPurchaseOrder();
        List<ProductsRequestDTO> purchaseOrderRequestDTOProducts = getPurchaseOrderRequestDTOProducts();

        Mockito.when(repository.saveAll(Mockito.anyListOf(PurchaseOrdersProducts.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        //Method call
        return service.save(products, purchaseOrder, purchaseOrderRequestDTOProducts);
    }

    private List<ProductsRequestDTO> getPurchaseOrderRequestDTOProducts() {
        ProductsRequestDTO productsRequestDTO = ProductsRequestDTO.builder().productId(PRODUCT_ID).quantity(QUANTITY).build();
        return Collections.singletonList(productsRequestDTO);
    }

    private PurchaseOrder getPurchaseOrder() {
        return PurchaseOrder.builder().id(PURCHASE_ORDER_ID).build();
    }

    private List<Product> getProducts() {
        Product product = Product.builder().id(PRODUCT_ID).build();
        return Collections.singletonList(product);
    }
}
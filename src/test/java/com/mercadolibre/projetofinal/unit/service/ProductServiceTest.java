package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.Section;
import com.mercadolibre.projetofinal.repository.ProductRepository;
import com.mercadolibre.projetofinal.service.ISectionService;
import com.mercadolibre.projetofinal.service.impl.ProductService;
import com.mercadolibre.projetofinal.util.CreateTestEntities;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @Mock
    ISectionService sectionService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    ProductService service;

    List<Product> productList;
    Section section;


    @BeforeEach
    void setUp(){

        productList = List.of(
                new Product("0", "Produto 0 - Colombia", 50.0, "produto 2", null),
                new Product("1", "Produto 1 - Colombia", 100.0, "produto 1", null)
        );
        this.section = new Section("1", "Section", Byte.valueOf("1"), Byte.valueOf("2"), null, null);

    }

    @Test
    void getAllByCountryTest(){
        when(jwtUtil.getUserCountry(any())).thenReturn(4);

        when(repository.findByBatchProductsWarehousesSectionsWarehouseCountry(CountryEnum.COLOMBIA))
                .thenReturn(productList);

        List<Product> list = service.getAllProductsByCountry("my_token");

        assertEquals("Produto 0 - Colombia", list.get(0).getName());
    }

    @Test
    void getAllByCountryErrorTest(){
        when(jwtUtil.getUserCountry(any())).thenReturn(0);
        when(repository.findByBatchProductsWarehousesSectionsWarehouseCountry(CountryEnum.ARGENTINA)).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> service.getAllProductsByCountry("my_token"),
                "Products not found");
    }

    @Test
    void getAllProductsByCategorySectionNullTest(){
        when(jwtUtil.getUserCountry(any())).thenReturn(4);
        when(sectionService.findByCategory(any())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.getAllProductsByCategory("my_token", "FS"),
                "Warehouse doesn't exist in your Country");
    }

    @Test
    void getAllProductsByCategoryProductsNullTest(){
        when(jwtUtil.getUserCountry(any())).thenReturn(4);
        when(sectionService.findByCategory(any())).thenReturn(section);
        when(repository.findByBatchProductsWarehousesSectionsWarehouseCountryAndBatchProductsWarehousesSectionsSectionId(any(), any())).thenReturn(List.of());


        assertThrows(NotFoundException.class, () -> service.getAllProductsByCategory("my_token", "FS"),
                "Products not found");
    }

    @Test
    void getAllProductsByCategoryTest(){
        when(jwtUtil.getUserCountry(any())).thenReturn(4);
        when(sectionService.findByCategory(any())).thenReturn(section);
        when(repository.findByBatchProductsWarehousesSectionsWarehouseCountryAndBatchProductsWarehousesSectionsSectionId(any(), any())).thenReturn(productList);

        List<Product> list = service.getAllProductsByCategory("my_token", "FS");

        assertEquals("Produto 0 - Colombia", list.get(0).getName());
    }

    @Test
    void countExistentByIdsTest(){
        when(repository.countByIdIn(any())).thenReturn(5);
        List<String> stringList = List.of("524234", "3423424", "32432423");
        assertEquals(5, service.countExistentByIds(stringList));
    }

    @Test
    void countExistentByIdsEmptyTest(){
        List<String> stringList = List.of();
        assertEquals(0, service.countExistentByIds(stringList));
    }

    @Test
    void findAllByIdTest(){
        when(repository.findAllById(any())).thenReturn(CreateTestEntities.products);
        List<Product> productList = service.findAllById(List.of("123", "232"));

        assertEquals(CreateTestEntities.products.size(), productList.size());

    }

    @Test
    void findByIdTest(){
        when(repository.findById(any())).thenReturn(java.util.Optional.ofNullable(CreateTestEntities.products.get(0)));
        Product product = service.findById("123");

        assertEquals(CreateTestEntities.products.get(0).getName(), product.getName());
    }

    @Test
    void findByIdNullTest(){
        assertThrows(NotFoundException.class, () -> service.findById(null),
                "At least one of the given products was not found");
    }

    @Test
    void existsByIdTest(){
        when(repository.existsById(any())).thenReturn(true);
        assertEquals(true, service.existsById("132"));
    }
    @Test
    void existsByIdNullTest(){
        assertEquals(false, service.existsById(null));
    }

    @Test
    void findAllByIdAndCountryTest(){
        when(repository.findAllByIdAndCountry(any(), any())).thenReturn(CreateTestEntities.products);
        List<Product> list = service.findAllByIdAndCountry(List.of("12"), 0);
        assertEquals(CreateTestEntities.products.get(0).getName(), list.get(0).getName());
    }


}

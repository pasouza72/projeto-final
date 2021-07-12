package com.mercadolibre.projetofinal.util;

import com.mercadolibre.projetofinal.enums.AccountRolesEnum;
import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.enums.SectionCategoryEnum;
import com.mercadolibre.projetofinal.model.*;
import com.mercadolibre.projetofinal.repository.ProductRepository;
import com.mercadolibre.projetofinal.repository.WarehouseRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class PopulateDatabase {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public PopulateDatabase(WarehouseRepository warehouseRepository, ProductRepository productRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    LocalDate date = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.now();

    @Transactional
    public void populate(){

        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        List<Warehouse> warehouses = new ArrayList(List.of(
                new Warehouse(null, "Casa central de Argentina", CountryEnum.ARGENTINA,new HashSet<>(), new HashSet<>()),
                new Warehouse(null, "Casa central de Chile", CountryEnum.CHILE,new HashSet<>(), new HashSet<>()),
                new Warehouse(null, "Casa central de Uruguay", CountryEnum.URUGUAY,new HashSet<>(), new HashSet<>()),
                new Warehouse(null, "Casa central de Colombia", CountryEnum.COLOMBIA,new HashSet<>(), new HashSet<>()),
                new Warehouse(null, "Casa central de Brazil 1", CountryEnum.BRAZIL,new HashSet<>(), new HashSet<>()),
                new Warehouse(null, "Casa central de Brazil 2", CountryEnum.BRAZIL,new HashSet<>(), new HashSet<>())
        ));

        List<Account> accounts = new ArrayList(List.of(
                new Account(null,"user_one",CountryEnum.BRAZIL, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(0), new HashSet<>()),
                new Account(null,"user_two",CountryEnum.BOLIVIA, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(1), new HashSet<>()),
                new Account(null,"user_three",CountryEnum.ARGENTINA, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(2), new HashSet<>()),
                new Account(null,"user_four",CountryEnum.CHILE, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(3), new HashSet<>()),
                new Account(null,"user_five",CountryEnum.COLOMBIA, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(3), new HashSet<>()),

                // argentina
                new Account(null,"user_argentina_representative",CountryEnum.ARGENTINA, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(4), new HashSet<>()),
                new Account(null,"user_argentina_buyer",CountryEnum.ARGENTINA, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(4), new HashSet<>()),

                // chile
                new Account(null,"user_chile_representative",CountryEnum.CHILE, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(4), new HashSet<>()),
                new Account(null,"user_chile_buyer",CountryEnum.CHILE, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(4), new HashSet<>()),

                // uruguay
                new Account(null,"user_uruguay_representative",CountryEnum.URUGUAY, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(4), new HashSet<>()),
                new Account(null,"user_uruguay_buyer",CountryEnum.URUGUAY, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(4), new HashSet<>()),

                // colombia
                new Account(null,"user_colombia_representative",CountryEnum.COLOMBIA, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(4), new HashSet<>()),
                new Account(null,"user_colombia_buyer",CountryEnum.COLOMBIA, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(4), new HashSet<>()),

                // brazil 1
                new Account(null,"user_brazil1_representative",CountryEnum.BRAZIL, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(4), new HashSet<>()),
                new Account(null,"user_brazil1_buyer",CountryEnum.BRAZIL, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(4), new HashSet<>()),

                // brazil 2
                new Account(null,"user_brazil2_representative",CountryEnum.BRAZIL, "contra123",  AccountRolesEnum.REPRESENTATIVE,  warehouses.get(5), new HashSet<>()),
                new Account(null,"user_brazil2_buyer",CountryEnum.BRAZIL, "contra123",  AccountRolesEnum.BUYER,  warehouses.get(5), new HashSet<>())
        ));

        ArrayList<Section> sections = new ArrayList(List.of(
                new Section(null,"Congelados", Byte.valueOf("-25"), Byte.valueOf("-18"), new HashSet<>(), SectionCategoryEnum.FF),
                new Section(null,"Refrigerados", Byte.valueOf("-18"), Byte.valueOf("10"), new HashSet<>(), SectionCategoryEnum.RF),
                new Section(null,"Temperatura ambiente", Byte.valueOf("10"), Byte.valueOf("30"), new HashSet<>(), SectionCategoryEnum.FS)
        ));

        accounts.forEach(el -> el.getWarehouse().getAccounts().add(el));

        ArrayList<WarehousesSections> warehousesSections = new ArrayList<>(List.of(
                // argentina
                new WarehousesSections(null, warehouses.get(0),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(0),sections.get(1),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(0),sections.get(2),500,new HashSet<>()),

                // chile
                new WarehousesSections(null, warehouses.get(1),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(1),sections.get(1),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(1),sections.get(2),500,new HashSet<>()),

                // uruguay
                new WarehousesSections(null, warehouses.get(2),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(2),sections.get(1),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(2),sections.get(2),500,new HashSet<>()),

                // colombia
                new WarehousesSections(null, warehouses.get(3),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(3),sections.get(1),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(3),sections.get(2),500,new HashSet<>()),

                // brazil
                new WarehousesSections(null, warehouses.get(4),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(4),sections.get(1),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(4),sections.get(2),500,new HashSet<>()),

                // brazil 2
                new WarehousesSections(null, warehouses.get(5),sections.get(0),500,new HashSet<>()),
                new WarehousesSections(null, warehouses.get(5),sections.get(1),500,new HashSet<>())
        ));

        warehousesSections.forEach(el -> {
            el.getWarehouse().getWarehouseSections().add(el);
            el.getSection().getWarehouseSections().add(el);
        });

        ArrayList<Product> products = new ArrayList(List.of(
                // argentina
                new Product(null, "Congelado Argentina", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Argentina", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Temperatura Ambiente Argentina", 10.90, "Produto.1", new ArrayList<>()),

                // chile
                new Product(null, "Congelado Chile", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Chile", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Temperatura Ambiente Chile", 10.90, "Produto.1", new ArrayList<>()),

                // uruguay
                new Product(null, "Congelado Uruguai", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Uruguai", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Temperatura Ambiente Uruguai", 10.90, "Produto.1", new ArrayList<>()),

                // colombia
                new Product(null, "Congelado Colombia", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Colombia", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Temperatura Ambiente Colombia", 10.90, "Produto.1", new ArrayList<>()),

                // brazil
                new Product(null, "Congelado Brazil 1", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Brazil 1", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Temperatura Ambiente Brazil 1", 10.90, "Produto.1", new ArrayList<>()),

                // brazil 2
                new Product(null, "Congelado Brazil 2", 10.90, "Produto.1", new ArrayList<>()),
                new Product(null, "Refrigerados Brazil 2", 10.90, "Produto.1", new ArrayList<>())
        ));

        ArrayList<BatchProduct> batchProducts = new ArrayList(List.of(
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(0), warehousesSections.get(0), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(1), warehousesSections.get(1), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(2), warehousesSections.get(2), null),

                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(3), warehousesSections.get(3), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(4), warehousesSections.get(4), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(5), warehousesSections.get(5), null),

                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(6), warehousesSections.get(6), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(7), warehousesSections.get(7), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(8), warehousesSections.get(8), null),

                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(9), warehousesSections.get(9), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(10), warehousesSections.get(10), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(11), warehousesSections.get(11), null),

                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(12), warehousesSections.get(12), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(13), warehousesSections.get(13), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(14), warehousesSections.get(14), null),

                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(15), warehousesSections.get(15), null),
                new BatchProduct(null, 1, 5., 1., 1, 100, date, dateTime, date, products.get(16), warehousesSections.get(16), null)
        ));

        batchProducts.forEach(el -> el.getProduct().getBatchProducts().add(el));
        warehouseRepository.saveAll(warehouses);
        productRepository.saveAll(products);
    }


}

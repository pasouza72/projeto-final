package com.mercadolibre.projetofinal.service;
import com.mercadolibre.projetofinal.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> getAllProductsByCountry(String token);

    Integer countExistentByIds(List<String> ids);

    List<Product> findAllById(List<String> ids);

    Product findById(String id);

    List<Product> getAllProductsByCategory(String token, String category);

    Boolean existsById(String id);

    List<Product> findAllByIdAndCountry(List<String> productsId, Integer country);
}

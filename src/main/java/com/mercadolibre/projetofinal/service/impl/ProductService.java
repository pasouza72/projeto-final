package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.enums.SectionCategoryEnum;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.Section;
import com.mercadolibre.projetofinal.repository.ProductRepository;
import com.mercadolibre.projetofinal.service.IProductService;
import com.mercadolibre.projetofinal.service.ISectionService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML001;
import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML201;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ISectionService sectionService;
    private final JwtUtil jwtUtil;


    @Override
    public List<Product> getAllProductsByCountry(String token) {
        Integer userCountry = jwtUtil.getUserCountry(token);
        CountryEnum userCountryEnum = CountryEnum.of(userCountry);
        List<Product> products = productRepository
                .findByBatchProductsWarehousesSectionsWarehouseCountry(userCountryEnum);

        if (products.size() == 0) throw new NotFoundException(ML001);

        return products;
    }


    @Override
    public Integer countExistentByIds(List<String> ids) {
        return productRepository.countByIdIn(ids);
    }

    @Override
    public List<Product> findAllById(List<String> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(ML001));
    }

    @Override
    public List<Product> getAllProductsByCategory(String token, String category) {
       Integer userCountry = jwtUtil.getUserCountry(token);
        CountryEnum userCountryEnum = CountryEnum.values()[userCountry];

        SectionCategoryEnum sectionCategoryEnum;

        sectionCategoryEnum = SectionCategoryEnum.of(category);

        Section section = sectionService
                .findByCategory(sectionCategoryEnum.getName());

        if (section == null) throw new NotFoundException(ML201);

        List<Product> products = productRepository
                .findByBatchProductsWarehousesSectionsWarehouseCountryAndBatchProductsWarehousesSectionsSectionId(
                        userCountryEnum, section.getId());

        if (products.size() == 0) throw new NotFoundException(ML001);

        return products;
    }

    @Override
    public Boolean existsById(String id) {
        return productRepository.existsById(id);
    }

    @Override
    public List<Product> findAllByIdAndCountry(List<String> productsId, Integer country){
        return this.productRepository.findAllByIdAndCountry(productsId, country);
    }

}

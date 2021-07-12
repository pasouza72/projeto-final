package com.mercadolibre.projetofinal.controller;

import com.mercadolibre.projetofinal.dtos.response.ProductResponseResponseDTO;
import com.mercadolibre.projetofinal.service.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseResponseDTO>> getAllProducts(@RequestHeader(value="Authorization") String token) {
        List<ProductResponseResponseDTO> productResponses = productService
                .getAllProductsByCountry(token)
                .stream()
                .map(ProductResponseResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseResponseDTO>> getProductsByCategory(
            @RequestHeader(value="Authorization") String token,
            @RequestParam String category
    ) {
        List<ProductResponseResponseDTO> productResponses = productService
                .getAllProductsByCategory(token, category)
                .stream()
                .map(ProductResponseResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productResponses);
    }
}

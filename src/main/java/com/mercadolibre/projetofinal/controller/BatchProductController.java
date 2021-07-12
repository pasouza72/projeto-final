package com.mercadolibre.projetofinal.controller;

import com.mercadolibre.projetofinal.dtos.response.BatchStockArrayDueDateResponse;
import com.mercadolibre.projetofinal.dtos.response.ListBatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.service.impl.BatchProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/fresh-products")
@RestController
@AllArgsConstructor
public class BatchProductController {

    private BatchProductService batchProductService;

    /**================================
     * Obtém lista de BatchProduct para a Warehouse do Representative
     * ================================
     */
    @GetMapping("/representative/list")
    public ResponseEntity<ListBatchStockSimplifiedResponseDTO> findBatchProducts(
            @RequestHeader(value="Authorization") String token,
            @RequestParam String productId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
            @RequestParam(value = "orderBy", defaultValue = "nome", required = false) String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC", required = false) String direction){
        orderBy = orderBy.toUpperCase().equals("C") ? "current_quantity": "due_date";
        direction = direction.toUpperCase().equals("DESC") ? "DESC": "ASC";
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.valueOf(direction), orderBy);
        ListBatchStockSimplifiedResponseDTO batchesForRepresentative = batchProductService
                .findBatchesForRepresentative(productId, token, pageRequest);
        return ResponseEntity.ok(batchesForRepresentative);
    }

    /**================================
     * Obtém lista de BatchProduct vencidos, com base Warehouse do Representative
     * ================================
     */
    @GetMapping("/due-date")
    public ResponseEntity<BatchStockArrayDueDateResponse> findBatchesProductsByDueDate(
            @RequestHeader(value="Authorization") String token,
            @RequestParam Integer days,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
            @RequestParam(value = "direction", defaultValue = "ASC", required = false) String direction
    ) {
        direction = direction.toUpperCase().equals("DESC") ? "DESC": "ASC";
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.valueOf(direction), "due_date");
        Page<BatchProduct> batchProducts = batchProductService.findBatchesByRepresentativeAndDueDate(token, days, pageRequest);
        return ResponseEntity.ok(new BatchStockArrayDueDateResponse(batchProducts));
    }

    /**================================
     * Obtém lista de BatchProduct vencidos, com base Warehouse do Representative e Categoria da Section
     * ================================
     */
    @GetMapping("/due-date/list")
    public ResponseEntity<BatchStockArrayDueDateResponse> findBatchesProductsByDueDateAndCategory(
            @RequestHeader(value="Authorization") String token,
            @RequestParam Integer days,
            @RequestParam String category,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
            @RequestParam(value = "direction", defaultValue = "ASC", required = false) String direction
    ) {
        direction = direction.toUpperCase().equals("DESC") ? "DESC": "ASC";
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.valueOf(direction), "due_date");
        Page<BatchProduct> batchProducts = batchProductService
                .findBatchesByRepresentativeAndDueDateAndCategory(token, days, category, pageRequest);
        return ResponseEntity.ok(new BatchStockArrayDueDateResponse(batchProducts));
    }
}


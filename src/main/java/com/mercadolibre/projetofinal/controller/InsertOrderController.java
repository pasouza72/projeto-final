package com.mercadolibre.projetofinal.controller;


import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.OrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.BatchProductResponseDTO;
import com.mercadolibre.projetofinal.service.IInsertOrderService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "/api/v1/fresh-products/inboundorder")
@RestController
@AllArgsConstructor
public class InsertOrderController {
    private final IInsertOrderService insertOrderService;
    private final JwtUtil jwtUtil;

    /**================================
     * Cadastra um pedido de estoque
     * ================================
     */
    @RequestMapping(method = RequestMethod.POST, consumes="application/json")
    @ResponseBody
    public ResponseEntity<List<BatchProductResponseDTO>> create(@RequestHeader(value="Authorization") String token, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {

        AccountDTO accountDTO = jwtUtil.getAccountDTO(token);
        return ResponseEntity.status(201).body(insertOrderService.createOrder(orderRequestDTO, accountDTO));

    }

    /**================================
     * Atualiza um pedido de estoque
     * ================================
     */
    @RequestMapping(method = RequestMethod.PUT, consumes="application/json")
    @ResponseBody
    public ResponseEntity<List<BatchProductResponseDTO>> update(@RequestHeader(value="Authorization") String token, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {

        AccountDTO accountDTO = jwtUtil.getAccountDTO(token);
        return ResponseEntity.status(201).body(insertOrderService.updateOrder(orderRequestDTO, accountDTO));
    }


}
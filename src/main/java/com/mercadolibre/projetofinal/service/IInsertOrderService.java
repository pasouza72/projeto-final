package com.mercadolibre.projetofinal.service;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.OrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.BatchProductResponseDTO;

import java.util.List;

public interface IInsertOrderService {


    List<BatchProductResponseDTO> createOrder(OrderRequestDTO orderRequestDTO, AccountDTO accountDTO);
    List<BatchProductResponseDTO> updateOrder(OrderRequestDTO orderRequestDTO, AccountDTO accountDTO);

}

package com.mercadolibre.projetofinal.controller;

import com.mercadolibre.projetofinal.dtos.request.BatchProductRequestDTO;
import com.mercadolibre.projetofinal.repository.SectionRepository;
import com.mercadolibre.projetofinal.repository.WarehouseRepository;
import com.mercadolibre.projetofinal.repository.WarehousesSectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@Autowired
	private WarehouseRepository warehouseRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private WarehousesSectionsRepository warehousesSectionsRepository;

	@GetMapping("/ping")
	public String ping() {
//

		return "pong";
	}
}

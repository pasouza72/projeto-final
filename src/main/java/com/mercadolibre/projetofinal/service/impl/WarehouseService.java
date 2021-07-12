package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountListDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountResponseDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Warehouse;
import com.mercadolibre.projetofinal.repository.WarehouseRepository;
import com.mercadolibre.projetofinal.service.IProductService;
import com.mercadolibre.projetofinal.service.IWarehouseService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import static com.mercadolibre.projetofinal.enums.ErrorsEnum.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseService implements IWarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;
    private final IProductService productService;

    @Override
    @Transactional
    public WarehouseDTO create(WarehouseDTO warehouseDTO) {
        if (warehouseRepository.findByCountry(warehouseDTO.getCountry())==null){
            Warehouse newWarehouse = modelMapper.map(warehouseDTO, Warehouse.class);
            warehouseRepository.save(newWarehouse);
        }else{
            warehouseDTO =null;
        }

        return warehouseDTO;
    }

    @Override
    @Transactional
    public WarehouseDTO update(WarehouseDTO warehouseDTO) {
        Warehouse newWarehouse = modelMapper.map(warehouseDTO, Warehouse.class);
        warehouseRepository.save(newWarehouse);
        return warehouseDTO;
    }

    @Override
    @Transactional
    public void delete(String id) {
        Optional<Warehouse> opt = warehouseRepository.findById(id);
        if (!opt.isPresent()) {
            throw new NoSuchElementException("No existe empleado con el id: " + id);
        }
        warehouseRepository.deleteById(id);
    }

    @Override
    public WarehouseDTO findById(String id) {
        Optional<Warehouse> opt = warehouseRepository.findById(id);

        if (!opt.isPresent()) {
            throw new NoSuchElementException("No existe empleado con el id: " + id);
        }
        return modelMapper.map(opt.get(), WarehouseDTO.class);
    }

    @Override
    public List<WarehouseDTO> findAll() {
        List<WarehouseDTO> countryHousesDTO = warehouseRepository.findAll()
                .stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());
        return countryHousesDTO;
    }

    @Override
    public WarehouseDTO findByCountry(String country) {
        Warehouse warehouse = warehouseRepository.findByCountry(country);
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }

    @Override
    public Warehouse findWarehouseById(String id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ML200)
        );
    }

    @Override
    public ProductCountListDTO getProductCountByWarehouse(String productId) {
        if (!productService.existsById(productId)){
            throw new NotFoundException(ML000);
        }
        List<ProductCountResponseDTO> productCounts = warehouseRepository.findProductCountByWarehouse(productId);
        if (productCounts.size() == 0){
            throw new NotFoundException(ML603);
        }
        return new ProductCountListDTO(productId, productCounts);
    }
}

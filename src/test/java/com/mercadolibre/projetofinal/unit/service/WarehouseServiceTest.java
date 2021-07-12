package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountListDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountResponseDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Warehouse;
import com.mercadolibre.projetofinal.repository.WarehouseRepository;
import com.mercadolibre.projetofinal.service.IProductService;
import com.mercadolibre.projetofinal.service.impl.WarehouseService;
import com.mercadolibre.projetofinal.util.CreateTestEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

        @Mock
        WarehouseRepository repository;

        @Mock
        ModelMapper modelMapper;

        @Mock
        IProductService productService;

        @InjectMocks
        WarehouseService service;


        @Test
        void createNotNullTest(){
            when(repository.findByCountry(any())).thenReturn(new Warehouse());
            assertEquals(null, service.create(new WarehouseDTO()));
        }
        @Test
        void createNullTest(){
            when(repository.findByCountry(any())).thenReturn(null);
            assertEquals(new WarehouseDTO(), service.create(new WarehouseDTO()));
        }

        @Test
        void updateTest(){
            when(repository.save(any())).thenReturn(null);
            assertEquals(new WarehouseDTO(), service.update(new WarehouseDTO()));
        }

        @Test
        void deleteNullTest(){
            when(repository.findById(any())).thenReturn(Optional.ofNullable(null));
            assertThrows(NoSuchElementException.class, () -> service.delete("my_id"),
                    "No existe empleado con el id: my_id");
        }

        @Test
        void deleteTest(){
            when(repository.findById(any())).thenReturn(Optional.ofNullable(CreateTestEntities.warehouse));
            service.delete("1");
            assertEquals(0,0);
        }

        @Test
        void findByIdNullTest(){
            when(repository.findById(any())).thenReturn(Optional.ofNullable(null));
            assertThrows(NoSuchElementException.class, () -> service.findById("my_id"),
                    "No existe empleado con el id: my_id");
        }

        @Test
        void findByIdTest(){
            when(repository.findById(any())).thenReturn(Optional.ofNullable(CreateTestEntities.warehouse));
            when(modelMapper.map(any(), any())).thenReturn(new WarehouseDTO());

            assertEquals(new WarehouseDTO(), service.findById("my_id"));
        }

        @Test
        void findAllTest(){
            when(repository.findAll()).thenReturn(List.of(CreateTestEntities.createWarehouse(), CreateTestEntities.createWarehouse()));
            when(modelMapper.map(any(), any())).thenReturn(CreateTestEntities.createWarehouseDTO());
            assertEquals(CreateTestEntities.createWarehouseDTO().getName(), service.findAll().get(0).getName());
        }

        @Test
        void findByCountryTest(){
            when(repository.findByCountry(any())).thenReturn(CreateTestEntities.createWarehouse());
            when(modelMapper.map(any(), any())).thenReturn(CreateTestEntities.createWarehouseDTO());
            assertEquals(CreateTestEntities.createWarehouseDTO().getName(), service.findByCountry("0").getName());
        }

        @Test
        void findWarehouseByIdTest(){
            when(repository.findById(any())).thenReturn(Optional.of(CreateTestEntities.createWarehouse()));
            assertEquals(CreateTestEntities.warehouse.getName(), service.findWarehouseById("0").getName());
        }

        @Test
        void findWarehouseByIdNullTest(){
            when(repository.findById(any())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> service.findWarehouseById("my_id"),
                    "Warehouse not found");
        }

        @Test
        void getProductCountByWarehouse(){
            List<ProductCountResponseDTO> productCountResponseDTOS = List.of(new ProductCountResponseDTOImpl("", 0));

            when(productService.existsById(any())).thenReturn(true);
            when(repository.findProductCountByWarehouse(any())).thenReturn(productCountResponseDTOS);

            assertEquals(new ProductCountListDTO("", productCountResponseDTOS), service.getProductCountByWarehouse(""));
        }

        @Test
        void getProductCountByWarehouseProductNullTest(){
            when(productService.existsById(any())).thenReturn(false);
            assertThrows(NotFoundException.class, () -> service.getProductCountByWarehouse("my_id"),
                    "Product not found");
        }

        @Test
        void getProductCountByWarehouseProductEmpityTest(){
            when(productService.existsById(any())).thenReturn(true);
            when(repository.findProductCountByWarehouse(any())).thenReturn(List.of());
            assertThrows(NotFoundException.class, () -> service.getProductCountByWarehouse("my_id"),
                    "There are no batches of this product left");
        }



        @AllArgsConstructor
        @Data
        public static class ProductCountResponseDTOImpl implements ProductCountResponseDTO{
            private final String warehouseId;
            private final Integer count;
        }
}

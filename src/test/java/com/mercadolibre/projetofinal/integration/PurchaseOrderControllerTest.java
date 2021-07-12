package com.mercadolibre.projetofinal.integration;

import com.mercadolibre.projetofinal.dtos.response.ProductStockCountResponseDTO;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.repository.BatchProductRepository;
import com.mercadolibre.projetofinal.repository.ProductRepository;
import com.mercadolibre.projetofinal.service.impl.BatchProductService;
import com.mercadolibre.projetofinal.service.impl.ProductService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sound.sampled.Control;
import java.util.ArrayList;
import java.util.List;

import static com.mercadolibre.projetofinal.util.CreateTestEntities.accountDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class PurchaseOrderControllerTest extends ControllerTest {

    private static final String PATH = "/api/v1";

    @Autowired
    MockMvc mockMvc;

    @Test
    void isForbiddenProductOrders() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(PATH + "/fresh-products/orders"))
                .andExpect(status()
                        .isForbidden());
    }

}

package com.mercadolibre.projetofinal.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ProductControllerTest extends ControllerTest {

    private static final String PATH = "/api/v1";

    @Autowired
    MockMvc mockMvc;

    @Test
    void isForbiddenFreshProducts() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(PATH + "/fresh-products"))
                .andExpect(status()
                        .isForbidden());
    }

    @Test
    void isForbiddenFreshProductsList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(PATH + "/fresh-products/list"))
                .andExpect(status()
                        .isForbidden());
    }
}

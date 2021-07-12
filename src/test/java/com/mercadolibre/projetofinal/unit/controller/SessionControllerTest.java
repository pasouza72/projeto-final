package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.SessionController;
import com.mercadolibre.projetofinal.dtos.response.AccountResponseDTO;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.service.ISessionService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SessionControllerTest {

    SessionController controller;
    ISessionService service = Mockito.mock(ISessionService.class);

    @BeforeEach
    void setUp() throws NotFoundException {
        when(service.login("user_one", "contra12"))
                .thenThrow(new ApiException("404", "Usuario y/o contraseña incorrecto", 404));
        when(service.login("user_one", "contra123"))
                .thenReturn(new AccountResponseDTO("user_one", "TOKEN"));
        controller = new SessionController(service);
    }

    @Test
    void loginFail() throws Exception {
        assertThrows(ApiException.class, () -> controller.login("user_one","contra12"),
                "Usuario y/o contraseña incorrecto");
    }

    @Test
    void loginOk() throws Exception {
        AccountResponseDTO accountDTO = controller.login("user_one","contra123");
        assertEquals("user_one", accountDTO.getUsername());
        assertEquals("TOKEN", accountDTO.getToken());
    }
}

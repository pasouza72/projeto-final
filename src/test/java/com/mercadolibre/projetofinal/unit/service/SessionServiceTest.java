package com.mercadolibre.projetofinal.unit.service;

import com.mercadolibre.projetofinal.dtos.response.AccountResponseDTO;
import com.mercadolibre.projetofinal.enums.CountryEnum;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Account;
import com.mercadolibre.projetofinal.repository.AccountRepository;
import com.mercadolibre.projetofinal.service.impl.SessionService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    SessionService sessionService;


    @Test
    void loginFail() {
        when(accountRepository.findByUsernameAndPassword("user", "invalid")).thenReturn(null);
        assertThrows(ApiException.class, () -> sessionService.login("user", "invalid"),
                "Incorrect username or password");
    }

    @Test
    void loginOk(){
        Account account = new Account(null, "User", CountryEnum.ARGENTINA, "Pass", null, null, null);
        when(accountRepository.findByUsernameAndPassword("User", "Pass")).thenReturn(account);
        AccountResponseDTO accountDTO = sessionService.login("User","Pass");
        assertEquals("User", accountDTO.getUsername());
    }

    /* sessionService.getAccountById() just run accountRepository.findById() */
    /* accountRepository is mocked and this test is just to get coverage */
    @Test
    void getAccountByIdTest(){
        Account accountToFind = new Account(null, "User", CountryEnum.ARGENTINA, "Pass", null, null, null);
        when(accountRepository.findById(any())).thenReturn(java.util.Optional.of(accountToFind));
        Account accountFound = sessionService.getAccountById(accountToFind.getId());
        assertEquals(accountToFind.getId(), accountFound.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetAccountByIdTest(){
        Account accountToFind = new Account(null, "User", CountryEnum.ARGENTINA, "Pass", null, null, null);
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.getAccountById(accountToFind.getId()));
    }
}

package com.mercadolibre.projetofinal.service;


import com.mercadolibre.projetofinal.dtos.response.AccountResponseDTO;
import com.mercadolibre.projetofinal.model.Account;
import javassist.NotFoundException;

public interface ISessionService {

    /**
     * Realiza la validación del usuario y contraseña ingresado.
     * En caso de ser correcto, devuelve la cuenta con el token necesario para realizar las demás consultas.
     *
     * @param username
     * @param password
     * @return
     * @throws NotFoundException
     */
    AccountResponseDTO login(String username, String password) throws NotFoundException;
    Account getAccountById(String id);
}

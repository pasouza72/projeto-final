package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.response.AccountResponseDTO;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.model.Account;
import com.mercadolibre.projetofinal.repository.AccountRepository;
import com.mercadolibre.projetofinal.service.ISessionService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML800;
import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML801;

@Service
@AllArgsConstructor
public class SessionService implements ISessionService {
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;



    /**
     * Realiza la validación del usuario y contraseña ingresado.
     * En caso de ser correcto, devuelve la cuenta con el token necesario para realizar las demás consultas.
     *
     * @param username
     * @param password
     * @return
     * @throws NotFoundException
     */
    @Override
    public AccountResponseDTO login(String username, String password) throws ApiException {
        //Voy a la base de datos y reviso que el usuario y contraseña existan.
        Account account = accountRepository.findByUsernameAndPassword(username, password);

        if (account != null) {
            String token = jwtUtil.getJWTToken(account);
            AccountResponseDTO user = new AccountResponseDTO();
            user.setUsername(username);
            user.setToken(token);
            return user;
        } else {
            throw new com.mercadolibre.projetofinal.exceptions.NotFoundException(ML800);
        }

    }

    @Override
    public Account getAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new com.mercadolibre.projetofinal.exceptions.NotFoundException(
                        ML801.getCode(),
                        String.format(ML801.getDescription(), id),
                        ML801.getHttpStatus().value()
                ));
    }

}

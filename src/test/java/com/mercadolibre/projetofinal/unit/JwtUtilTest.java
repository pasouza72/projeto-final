package com.mercadolibre.projetofinal.unit;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil("mySecret");

    @Test
    void testDecodeJwtWorks(){
        //given token

        //when
        AccountDTO accountDTO = jwtUtil.getAccountDTO(representativeToken);

        //assert
        assertEquals(accountDTO.getUsername(), "user_one");

    }

    @Test
    void testGetJwtWorks(){
        //given account

        //when
        Integer country = jwtUtil.getUserCountry(representativeToken);

        //assert
        assertEquals(country, 2);

    }

    @Test
    void testGetUserCountryWorks(){
        //given token

        //when
        String token = jwtUtil.getJWTToken(account);

        //assert
        assertTrue(token.startsWith("Bearer "));

    }
}

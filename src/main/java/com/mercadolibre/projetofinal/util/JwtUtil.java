package com.mercadolibre.projetofinal.util;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private String secret;

    public JwtUtil(@Value("${jwt-secret}") String secret) {
        this.secret = secret;
    }

    public AccountDTO getAccountDTO(String token) {
        var claims = decodeJWT(token);
        String warehouseId =  claims.get("warehouse", String.class);
        String username =  claims.get("username", String.class);
        String id =  claims.get("sub", String.class);
        Integer country = claims.get("country", Integer.class);
        return new AccountDTO(id,username,warehouseId, country);
    }

    public Integer getUserCountry(String token) {
        var claims = decodeJWT(token);
        return claims.get("country", Integer.class);
    }

    public String getJWTToken(Account account) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(account.getRole().getAuthorization());

        Map<String,Object> customClaims = new HashMap();
        customClaims.put("username",account.getUsername());
        customClaims.put("warehouse",account.getWarehouse().getId());
        customClaims.put("country", account.getCountry().ordinal());

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setClaims(customClaims)
                .setSubject(account.getId())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secret.getBytes()).compact();

        return "Bearer " + token;
    }

    private Claims decodeJWT(String token) {

        Claims claims = Jwts.parser().setSigningKey(secret.getBytes())
                                    .parseClaimsJws(token.split("Bearer ")[1]).getBody();
        return claims;
    }
}

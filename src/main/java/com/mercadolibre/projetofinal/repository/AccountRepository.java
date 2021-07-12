package com.mercadolibre.projetofinal.repository;

import com.mercadolibre.projetofinal.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("FROM Account a where a.username=:username and a.password=:password ")
    Account findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    Account findByUsernameAndWarehouseId(String username, Long countryHouseId);

    Optional<Account> findByUsername(String username);
}
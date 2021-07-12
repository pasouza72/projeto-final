package com.mercadolibre.projetofinal.model;

import com.mercadolibre.projetofinal.enums.AccountRolesEnum;
import com.mercadolibre.projetofinal.enums.CountryEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(unique = true)
    private String username;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private CountryEnum country;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private AccountRolesEnum role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_warehouse_fk", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "representative")
    private Set<InsertOrder> insertOrders;
}
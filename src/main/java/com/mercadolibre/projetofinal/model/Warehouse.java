package com.mercadolibre.projetofinal.model;

import com.mercadolibre.projetofinal.enums.CountryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="warehouses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Warehouse {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private CountryEnum country;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.PERSIST)
    private Set<Account> accounts;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private Set<WarehousesSections> warehouseSections;
}

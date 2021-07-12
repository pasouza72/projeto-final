package com.mercadolibre.projetofinal.model;

import com.mercadolibre.projetofinal.enums.SectionCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "sections")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Section {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @NotNull
    private Byte minTemperature;

    @NotNull
    private Byte maxTemperature;

    @OneToMany(mappedBy = "section")
    private Set<WarehousesSections> warehouseSections;

    @Enumerated(value = EnumType.STRING)
    private SectionCategoryEnum category;
}

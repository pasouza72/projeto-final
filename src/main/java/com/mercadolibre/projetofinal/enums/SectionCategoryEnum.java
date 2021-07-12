package com.mercadolibre.projetofinal.enums;

import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML101;

@AllArgsConstructor
public enum SectionCategoryEnum {
    FS("FS", "Temperatura ambiente"),
    RF("RF", "Refrigerados"),
    FF("FF", "Congelados");

    private final String id;
    private final String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SectionCategoryEnum of(String id) {
        for (SectionCategoryEnum category : values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        throw new NotFoundException(ML101);
    }
}

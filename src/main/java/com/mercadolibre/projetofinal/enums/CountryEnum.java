package com.mercadolibre.projetofinal.enums;

import com.mercadolibre.projetofinal.exceptions.NotFoundException;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML301;

public enum CountryEnum {
    ARGENTINA(0),
    BOLIVIA(1),
    BRAZIL(2),
    CHILE(3),
    COLOMBIA(4),
    COSTA_RICA(5),
    ECUADOR(6),
    EL_SALVADOR(7),
    GUATEMALA(8),
    HONDURAS(9),
    MEXICO(10),
    NICARAGUA(11),
    PANAMA(12),
    PARAGUAY(13),
    PERU(14),
    DOMINICAN_REPUBLIC(15),
    URUGUAY(16),
    VENEZUELA(17);

    private final Integer id;

    CountryEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String getName(){return this.toString().replace("_"," ");}

    public static CountryEnum of(Integer id) {
        for (CountryEnum country : values()) {
            if (country.getId().equals(id)) {
                return country;
            }
        }
        throw new NotFoundException(ML301);
    }

}

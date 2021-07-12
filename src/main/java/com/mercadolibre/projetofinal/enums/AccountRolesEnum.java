package com.mercadolibre.projetofinal.enums;

public enum AccountRolesEnum {
    REPRESENTATIVE(0),
    BUYER(1);

    private final Integer id;

    AccountRolesEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getRole() {
        return this.toString();
    }

    public String getName(){return this.toString().replace("_"," ");}

    public String getAuthorization() {
        return "ROLE_" + this.toString();
    }
}

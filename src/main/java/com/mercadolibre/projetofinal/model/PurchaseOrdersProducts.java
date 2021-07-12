package com.mercadolibre.projetofinal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "purchaseOrders_products")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PurchaseOrdersProducts {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    @NotNull
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnore
    private Product product;


    private Integer quantity;

    public PurchaseOrdersProducts(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public double priceCalculated(){
        return this.getQuantity() * this.getProduct().getPrice();
    }
}

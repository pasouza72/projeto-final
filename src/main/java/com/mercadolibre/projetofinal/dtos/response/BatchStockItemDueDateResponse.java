package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.model.BatchProduct;
import lombok.Data;

@Data
public class BatchStockItemDueDateResponse {

    private Integer batchNumber;
    private String productId;
    private String productTypeId;
    private String dueDate;
    private Integer quantity;

    public BatchStockItemDueDateResponse(BatchProduct batchProduct) {
        this.batchNumber = batchProduct.getBatchNumber();
        this.productId = batchProduct.getProduct().getId();
        this.productTypeId = batchProduct.getWarehousesSections().getSection().getCategory().getId();
        this.dueDate = batchProduct.getDueDate().toString();
        this.quantity = batchProduct.getCurrentQuantity();
    }
}

package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.model.BatchProduct;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BatchStockArrayDueDateResponse {

    private List<BatchStockItemDueDateResponse> batchStock;
    private Integer nextPage;
    private Integer lastPage;
    private Integer offset;

    public BatchStockArrayDueDateResponse(Page<BatchProduct> batchProducts) {
        this.batchStock = batchProducts
                .stream()
                .map(BatchStockItemDueDateResponse::new)
                .collect(Collectors.toList());
        this.nextPage = !(Math.toIntExact(batchProducts.getTotalPages()) == batchProducts.getNumber()+1) ? batchProducts.getNumber() + 1 : null;
        this.lastPage = Math.toIntExact(batchProducts.getTotalPages())-1;
        this.offset = Math.toIntExact(batchProducts.getPageable().getOffset());
    }
}

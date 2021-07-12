package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.model.Section;
import com.mercadolibre.projetofinal.model.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ListBatchStockSimplifiedResponseDTO {
    private SectionResponseDTO section;
    private String productId;
    private List<BatchStockSimplifiedResponseDTO> batchStock;
    private Integer nextPage;
    private Integer lastPage;
    private Integer offset;

    public ListBatchStockSimplifiedResponseDTO(Page<BatchProduct> batchProducts, Warehouse warehouse, Section section){
        this.productId = batchProducts.getContent().get(0).getProduct().getId();
        this.section = new SectionResponseDTO(section.getId(), warehouse.getId());
        this.batchStock = batchProducts
                .stream()
                .map(
                    product -> new BatchStockSimplifiedResponseDTO(
                            product.getId(),
                            product.getCurrentQuantity(),
                            product.getDueDate()))
                .collect(Collectors.toList());
        this.nextPage = !(Math.toIntExact(batchProducts.getTotalPages()) == batchProducts.getNumber()+1) ? batchProducts.getNumber() + 1 : null;
        this.lastPage = Math.toIntExact(batchProducts.getTotalPages())-1;
        this.offset = Math.toIntExact(batchProducts.getPageable().getOffset());

    }
}

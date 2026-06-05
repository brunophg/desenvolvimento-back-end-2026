package org.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import org.model.Item;

import java.math.BigDecimal;

@Schema(description = "Dados retornados para um item")
public record ItemResponse(
        Long id,
        String nome,
        String tipo,
        BigDecimal valor
) {
    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getNome(),
                item.getTipo(),
                item.getValor()
        );
    }
}

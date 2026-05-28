package org.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import org.model.Item;
import java.math.BigDecimal;

public record ItemRequest(
        @Schema(description = "Identificador do item. Opcional na criacao.", example = "1")
        Long id,
        @Schema(description = "Nome do item", example = "Contrato")
        String nome,
        @Schema(description = "Tipo do item", example = "Consumivel")
        String tipo,
        @Schema(description = "Valor do item", example = "150")
        BigDecimal valor
) {
    public Item toEntity() {
        Item item = new Item();
        if (id != null) {
            item.setId(id);
        }
        item.setNome(nome);
        item.setTipo(tipo);
        item.setValor(valor);

        return item;
    }
}

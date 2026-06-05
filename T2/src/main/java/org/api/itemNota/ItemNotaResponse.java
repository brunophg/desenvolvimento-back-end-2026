package org.api.itemNota;

import org.model.ItemNota;

import java.math.BigDecimal;

public record ItemNotaResponse(
        Long id,
        BigDecimal vrUnitario,
        BigDecimal quantidade,
        Long idNota,
        Long idProduto
) {
    public static ItemNotaResponse fromEntity(ItemNota itemNota) {
        return new ItemNotaResponse(
                itemNota.getId(),
                itemNota.getVrUnitario(),
                itemNota.getQuantidade(),
                itemNota.getNota().getId(),
                itemNota.getProduto().getId()
        );
    }
}

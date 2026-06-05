package org.dto.inventario;

import org.model.Inventario;

public record InventarioResponse(
        Long id,
        Integer quantidade,
        String nomeJogador,
        String nomeItem
) {
    public static InventarioResponse fromEntity(Inventario i) {
        return new InventarioResponse(
                i.getId(),
                i.getQuantidade(),
                i.getJogador() != null ? i.getJogador().getNome() : null,
                i.getItem() != null ? i.getItem().getNome() : null
        );
    }
}

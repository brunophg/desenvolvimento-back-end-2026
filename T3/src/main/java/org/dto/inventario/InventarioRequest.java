package org.dto.inventario;

import io.swagger.v3.oas.annotations.media.Schema;
import org.model.Inventario;
import org.model.Item;
import org.model.Jogador;

public record InventarioRequest(
        @Schema(description = "Identificador do inventario. Opcional na criacao.", example = "1")
        Long id,
        @Schema(description = "Quantidade de itens guardados", example = "5")
        Integer quantidade,
        @Schema(description = "ID do Jogador dono do inventário", example = "4001")
        Long idJogador,
        @Schema(description = "ID do Item sendo guardado", example = "4001")
        Long idItem
) {
    public Inventario toEntity() {
        Inventario i = new Inventario();
        if (id != null) {
            i.setId(id);
        }
        i.setQuantidade(quantidade);
        if (this.idJogador != null) {
            Jogador jogador = new Jogador();
            jogador.setId(this.idJogador);
            i.setJogador(jogador);
        }
        if (this.idItem != null) {
            Item item = new Item();
            item.setId(this.idItem);
            i.setItem(item);
        }
        return i;
    }
}

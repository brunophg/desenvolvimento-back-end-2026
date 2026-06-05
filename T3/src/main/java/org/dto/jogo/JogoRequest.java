package org.dto.jogo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.model.Jogo;

@Schema(description = "Dados enviados para criar ou atualizar um jogo")
public record JogoRequest(
        Long id,
        String nome,
        String genero
) {
    public Jogo toEntity() {
        Jogo jogo = new Jogo();
        if (id != null) {
            jogo.setId(id);
        }
        jogo.setNome(nome);
        jogo.setGenero(genero);
        return jogo;
    }
}

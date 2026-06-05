package org.dto.jogo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.model.Jogo;

@Schema(description = "Dados retornados para um jogo")
public record JogoResponse(
        Long id,
        String nome,
        String genero
) {
    public static JogoResponse fromEntity(Jogo jogo) {
        return new JogoResponse(
                jogo.getId(),
                jogo.getNome(),
                jogo.getGenero()
        );
    }
}

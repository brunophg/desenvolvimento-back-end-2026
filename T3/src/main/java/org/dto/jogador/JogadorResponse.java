package org.dto.jogador;

import org.model.Jogador;

public record JogadorResponse(
        Long id,
        String nome,
        String nickname,
        String email
) {
    public static JogadorResponse fromEntity(Jogador jogador) {
        return new JogadorResponse(
                jogador.getId(),
                jogador.getNome(),
                jogador.getNickname(),
                jogador.getEmail()
        );
    }
}

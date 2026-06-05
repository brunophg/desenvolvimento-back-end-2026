package org.dto.jogador;

import org.model.Jogador;

public record JogadorRequest(
        Long id,
        String nome,
        String nickname,
        String email
) {
    public Jogador toEntity() {
        Jogador jogador = new Jogador();
        if (id != null) {
            jogador.setId(id);
        }
        jogador.setNome(nome);
        jogador.setNickname(nickname);
        jogador.setEmail(email);

        return jogador;
    }
}

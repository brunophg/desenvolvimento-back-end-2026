package org.dto.jogador;

public record JogadorRankingDTO(
        Long id,
        String nome,
        String nickname,
        Long pontuacaoTotal // Este campo recebe a soma das partidas!
) {
    // Os registros do Java criam o construtor, getters e os métodos automaticamente.
}
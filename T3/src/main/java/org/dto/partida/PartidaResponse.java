package org.dto.partida;

import org.model.Partida;

import java.time.LocalDateTime;

public record PartidaResponse(
        Long id,
        LocalDateTime data,
        Integer pontuacao,
        String nomeJogador,
        String nomeJogo
) {
    public static PartidaResponse fromEntity(Partida partida) {
        return new PartidaResponse(
                partida.getId(),
                partida.getData(),
                partida.getPontuacao(),
                partida.getJogador() != null ? partida.getJogador().getNome() : null,
                partida.getJogo() != null ? partida.getJogo().getNome() : null
        );
    }

}

package org.dto.partida;

import org.model.Jogador;
import org.model.Jogo;
import org.model.Partida;

import java.time.LocalDateTime;

public record PartidaRequest(
        Long id,
        LocalDateTime data,
        Integer pontuacao,
        Long idJogador,
        Long idJogo
) {
    public Partida toEntity() {
        Partida part = new Partida();
        if (id != null) {
            part.setId(id);
        }
        part.setData(data);
        part.setPontuacao(pontuacao);
        if (this.idJogador != null) {
            Jogador p1 = new Jogador();
            p1.setId(this.idJogador);
            part.setJogador(p1);
        }
        if (this.idJogo != null) {
            Jogo jogo = new Jogo();
            jogo.setId(this.idJogo);
            part.setJogo(jogo);
        }
        return part;
    }
}
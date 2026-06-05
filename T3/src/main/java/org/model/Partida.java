package org.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    private Long id;
    @Column(name = "data")
    private LocalDateTime data;
    @Column(name = "pontuacao")
    private Integer pontuacao;

    @ManyToOne
    @JoinColumn(name = "id_jogador", nullable = false)
    private Jogador jogador;
    @ManyToOne
    @JoinColumn(name = "id_jogo", nullable = false)
    private Jogo jogo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", data=" + data +
                ", pontuacao=" + pontuacao +
                ", jogador=" + jogador +
                ", jogo=" + jogo +
                '}';
    }
}

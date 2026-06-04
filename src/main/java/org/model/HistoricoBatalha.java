package org.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "historico_batalha")
public class HistoricoBatalha {

    @Id
    private Long id;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "local")
    private String local;

    @Column(name = "vencedor")
    private String vencedor;

    @Column(name = "pontuacao")
    private Integer pontuacao;

    @ManyToOne
    @JoinColumn(name = "id_pokedex")
    private Pokedex pokedex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVencedor() {
        return vencedor;
    }

    public void setVencedor(String vencedor) {
        this.vencedor = vencedor;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Pokedex getPokedex() {
        return pokedex;
    }

    public void setPokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoBatalha that = (HistoricoBatalha) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HistoricoBatalha{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", local='" + local + '\'' +
                ", vencedor='" + vencedor + '\'' +
                ", pontuacao=" + pontuacao +
                ", pokedex=" + pokedex +
                '}';
    }
}

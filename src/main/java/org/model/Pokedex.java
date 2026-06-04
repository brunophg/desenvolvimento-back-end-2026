package org.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pokedex")
public class Pokedex {

    @Id
    private Long id;

    @Column(name = "data_hora_captura", updatable = false, nullable = false)
    private LocalDateTime dataHoraCaptura;

    @ManyToOne
    @JoinColumn(name = "id_jogador")
    private Jogador jogador;

    @ManyToOne
    @JoinColumn(name = "id_pokemon")
    private Pokemon pokemon;

    @OneToMany(mappedBy = "pokedex", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<HistoricoBatalha> batalhas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(LocalDateTime dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pokedex pokedex = (Pokedex) o;
        return Objects.equals(id, pokedex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pokedex{" +
                "id=" + id +
                ", dataHoraCaptura=" + dataHoraCaptura +
                '}';
    }
}

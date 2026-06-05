package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jogo")
public class Jogo {

    @Id
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "genero")
    private String genero;

    @OneToMany(mappedBy = "jogo")
    private List<Partida> partidas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void addPartida(Partida partida) {
        this.partidas.add(partida);
        partida.setJogo(this); // Agora sim! Referenciando o Jogo.
    }

    public void removePartida(Partida partida) {
        this.partidas.remove(partida);
        partida.setJogador(null);
    }

    @Override
    public String toString() {
        return "Jogo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}

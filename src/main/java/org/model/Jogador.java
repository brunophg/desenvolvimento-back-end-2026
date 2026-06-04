package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "jogador")
public class Jogador {

    @Id
    private Long id;
    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "jogador", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pokedex> capturas = new ArrayList<>();

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

    public List<Pokedex> getCapturas() {
        return capturas;
    }

    public void setCapturas(List<Pokedex> capturas) {
        this.capturas = capturas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Jogador jogador = (Jogador) o;
        return Objects.equals(id, jogador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}

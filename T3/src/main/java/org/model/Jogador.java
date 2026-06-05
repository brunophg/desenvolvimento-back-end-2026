package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jogador")
public class Jogador {

    @Id
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email")
    private String email;


    @OneToMany(mappedBy = "jogador", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Partida> partidas = new ArrayList<>();
    @OneToMany(mappedBy = "jogador", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Inventario> itens = new ArrayList<>();

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Partida> getPartida() {
        return partidas;
    }

    public List<Inventario> getItens() {
        return itens;
    }

    public void addPartida(Partida partida) {
        this.partidas.add(partida);
        partida.setJogador(this);
    }

    public void addInventario(Inventario inventario) {
        this.itens.add((inventario));
        inventario.setJogador(this);
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

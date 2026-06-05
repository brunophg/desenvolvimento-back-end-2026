package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table (name = "produto")
public class Produto {

    @Id
    @Column(name = "id")
    private Long id;

    @Column (name = "codigo")
    private String codigo;

    @Column (name = "descricao")
    private String descricao;

    @Transient
    private ArrayList<ItemNota> itemNotas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<ItemNota> getItemNotas() {
        return itemNotas;
    }

    public void setItemNotas(ArrayList<ItemNota> itemNotas) {
        this.itemNotas = itemNotas;
    }
}

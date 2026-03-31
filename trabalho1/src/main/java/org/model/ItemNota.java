package org.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@Table (name = "itemnota")
public class ItemNota {

    @Id
    @Column (name = "id")
    private Long id;
    @Column (name = "vrunitario")
    private BigDecimal vrUnitario;
    @Column (name = "quantidade")
    private BigDecimal quantidade;

    @ManyToOne
    @JoinColumn (name = "id_nota", nullable = false)
    private Nota nota;

    @ManyToOne
    @JoinColumn (name = "id_produto", nullable = false)
    private Produto produto;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getVrUnitario() {
        return vrUnitario;
    }

    public void setVrUnitario(BigDecimal vrUnitario) {
        this.vrUnitario = vrUnitario;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }
}

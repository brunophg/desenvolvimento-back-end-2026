package org.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "nota")
public class Nota {
    @Id
    @Column (name = "id")
    private Long id;

    @Column (name = "data")
    private Date data;
    @Column (name = "numero")
    private Integer numero;

    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemNota> itensNota;

    @ManyToOne
    @JoinColumn (name = "id_participante", nullable = false)
    private Participante participante;
    @ManyToOne
    @JoinColumn (name = "id_empresa", nullable = false)
    private Empresa empresa;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<ItemNota> getItensNota() {
        if (itensNota == null) {
            itensNota = new ArrayList<ItemNota>();
        }
        return itensNota;
    }

    public void setItensNota(List<ItemNota> itensNota) {
        this.itensNota = itensNota;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BigDecimal getVrTotal() {
        BigDecimal vrTotal = BigDecimal.ZERO;
        for (ItemNota item : getItensNota()) {
            if (item.getVrUnitario() != null && item.getQuantidade() != null) {
                BigDecimal subtotal = item.getVrUnitario().multiply(item.getQuantidade());
                vrTotal = vrTotal.add(subtotal);
            }
        }
        return vrTotal;
    }
}

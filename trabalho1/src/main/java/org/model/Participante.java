package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
@Entity
@Table(name = "participante")
public class Participante {

    @Id
    @Column(name = "id")
    private Long id;

    @Column (name = "codigo")
    private Integer codigo;

    @Column (name = "razaosocial")
    private String razaoSocial;

    @Column (name = "cnpj")
    private String cnpj;

    @Transient
    private ArrayList<Nota> notas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }
}

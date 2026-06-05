package org.model;

import jakarta.persistence.*;
import org.model.Nota;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "empresa")
public class Empresa {

    @Id
    @Column (name = "id")
    private long id;
    @Column (name = "codigo")
    private Integer codigo;
    @Column (name = "razaosocial")
    private String razaoSocial;
    @Column (name = "endereco")
    private String endereco;
    @Column (name = "cnpj")
    private String cnpj;

    @Transient
    private List<Nota> notas;

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", endereco='" + endereco + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}

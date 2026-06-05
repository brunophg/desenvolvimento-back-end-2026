package model;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private long id;
    private String descricao;
    private Integer ano;
    private Integer semestre;

    private Instituicao instituicao;
    private Disciplina disciplina;

    private ArrayList<Diario> diarios;


    public ArrayList<Diario> getDiarios() {
        if (diarios == null) {
            diarios = new ArrayList<>();
        }
        return diarios;
    }

    public void setDiarios(ArrayList<Diario> diarios) {
        this.diarios = diarios;
    }


    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}

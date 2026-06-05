package model;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private long id;
    private String descricao;

    private List<Turma> turmas = new ArrayList<>();
    private Curso curso;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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

    public List<Turma> getTurmas() {
        if (turmas == null) {
            turmas = new ArrayList<>();
        }
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

}

package model;

import java.util.ArrayList;
import java.util.List;

public class Instituicao {
    private long id;
    private String descricao;

    private List<Turma> turmas = new ArrayList<>();

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
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }


    public void addTurma(Turma t) {
        if (this.turmas == null) {
            this.turmas = new ArrayList<>();
        }
        this.turmas.add(t);
    }

    public void removeTurma(Turma t) {
        if (turmas != null){
            this.turmas.remove(t);
        }
    }
}

package org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Entidade de curso.
@Entity
@Table(name = "curso")
public class Curso {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "curso", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Disciplina> disciplinas;

    @ManyToMany
    @JoinTable(
            name = "curso_professor",
            joinColumns = @JoinColumn(name = "id_curso"),
            inverseJoinColumns = @JoinColumn(name = "id_professor")
    )
    private List<Professor> professores;

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

    public List<Disciplina> getDisciplinas() {
        if (disciplinas == null) {
            disciplinas = new ArrayList<>();
        }
        return disciplinas;
    }

    public void addDisciplina(Disciplina disciplina){
        getDisciplinas().add(disciplina);
        disciplina.setCurso(this);
    }

    public void removeDisciplina(Disciplina disciplina){
        getDisciplinas().remove(disciplina);
        if (disciplina != null && disciplina.getCurso() == this) {
            disciplina.setCurso(null);
        }
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public List<Professor> getProfessores() {
        if (professores == null) {
            professores = new ArrayList<>();
        }
        return professores;
    }

    public void addProfessor(Professor professor){
        getProfessores().add(professor);
        professor.getCursos().add(this);
    }

    public void removeProfessor(Professor professor){
        getProfessores().remove(professor);
        if (professor != null) {
            professor.getCursos().remove(this);
        }
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}

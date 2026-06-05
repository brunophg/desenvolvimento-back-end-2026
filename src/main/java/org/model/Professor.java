package org.model;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Professor extends model.Pessoa {

    private String matricula;
    private int titulacaoMaxima;

    private List<model.Curso> cursosLecionados = new ArrayList<>();
    private ArrayList<model.Turma> turmas;

    public Professor() {
    }

    public Professor(String cpf, Date dataNascimento, String email, String endereco, long id, String identidade, String nome, String telefone) {
        super(cpf, dataNascimento, email, endereco, id, identidade, nome, telefone);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getTitulacaoMaxima() {
        return titulacaoMaxima;
    }

    public void setTitulacaoMaxima(int titulacaoMaxima) {
        this.titulacaoMaxima = titulacaoMaxima;
    }

    public List<Curso> getCursosLecionados() {
        return cursosLecionados;
    }

    public void setCursosLecionados(List<Curso> cursosLecionados) {
        this.cursosLecionados = cursosLecionados;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(ArrayList<Turma> turmas) {
        this.turmas = turmas;
    }

    public void addTurma(Turma turma) {
        getTurmas().add(turma);
    }

    public void removeTurma(Turma turma) {
        getTurmas().remove(turma);
    }

    public void addCurso(Curso curso) {
        getCursosLecionados().add(curso);
    }

    public void removeCurso(Curso curso) {
        getCursosLecionados().remove(curso);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "matricula='" + matricula + '\'' +
                ", titulacaoMaxima=" + titulacaoMaxima +
                ", cursosLecionados=" + cursosLecionados +
                ", turmas=" + turmas +
                '}';
    }
}

package model;

import java.math.BigDecimal;

public class Diario {

    private BigDecimal v1;
    private BigDecimal v2;
    private BigDecimal vS;
    private BigDecimal vT;
    private int faltas;

    private Turma turma;
    private Aluno aluno;

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public BigDecimal getV1() {
        return v1;
    }

    public void setV1(BigDecimal v1) {
        this.v1 = v1;
    }

    public BigDecimal getV2() {
        return v2;
    }

    public void setV2(BigDecimal v2) {
        this.v2 = v2;
    }

    public BigDecimal getvS() {
        return vS;
    }

    public void setvS(BigDecimal vS) {
        this.vS = vS;
    }

    public BigDecimal getvT() {
        return vT;
    }

    public void setvT(BigDecimal vT) {
        this.vT = vT;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
}

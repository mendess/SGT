package main.sgt;

import main.dao.AulaDAO;

import java.util.ArrayList;
import java.util.List;

public class Turno {
    private int id;
    private List<String> alunos;
    private String docente;
    private boolean ePratico;
    private int vagas;
    public List<TurnoInfo> tInfo = new ArrayList<>();
    public AulaDAO aulas;

    public boolean getEPratico() {
        return this.ePratico;
    }

    public void setEPratico(boolean ePratico) {
        this.ePratico = ePratico;
    }

    public List<String> getAlunos() {
        throw new UnsupportedOperationException();
    }

    public void addAluno(String alunos) {
        throw new UnsupportedOperationException();
    }

    public void removeAluno(String aluno) {
        throw new UnsupportedOperationException();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVagas() {
        return this.vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public void marcarPresenca(String aluno, int aula) {
        throw new UnsupportedOperationException();
    }
}

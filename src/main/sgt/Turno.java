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
    private List<TurnoInfo> tInfo = new ArrayList<>();
    private AulaDAO aulas;

    boolean getEPratico() {
        return this.ePratico;
    }

    void setEPratico(boolean ePratico) {
        this.ePratico = ePratico;
    }

    List<String> getAlunos() {
        throw new UnsupportedOperationException();
    }

    void addAluno(String alunos) {
        throw new UnsupportedOperationException();
    }

    void removeAluno(String aluno) {
        throw new UnsupportedOperationException();
    }

    int getId() {
        return this.id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getVagas() {
        return this.vagas;
    }

    void setVagas(int vagas) {
        this.vagas = vagas;
    }

    void marcarPresenca(String aluno, int aula) {
        throw new UnsupportedOperationException();
    }
}

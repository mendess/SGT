package main.sgt;

import main.dao.TurnoDAO;

import java.util.List;

public class UC {
    private String id;
    private String nome;
    private String responsavel;
    private List<String> docentes;
    private List<String> alunos;
    public TurnoDAO _turnos;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    List<Turno> getTurnos() {
        throw new UnsupportedOperationException();
    }

    public void marcarPresenca(String aluno, int turno, int aula) {
        throw new UnsupportedOperationException();
    }

    public void removerAlunoDeTurno(String aluno, int turno) {
        throw new UnsupportedOperationException();
    }

    public void adicionarAlunoTurno(String aluno, int turno) {
        throw new UnsupportedOperationException();
    }

    public void trocarAlunos(String aluno1, String aluno2) {
        throw new UnsupportedOperationException();
    }

    public void addTurno(int id, boolean ePratico) {
        throw new UnsupportedOperationException();
    }

    public void removeTurno(int id) {
        throw new UnsupportedOperationException();
    }
}

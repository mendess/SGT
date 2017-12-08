package main.sgt;

import main.dao.TurnoDAO;

import java.util.List;

public class UC {
    private String id;
    private String nome;
    private String responsavel;
    private List<String> docentes;
    private List<String> alunos;
    private TurnoDAO turnos;

    String getId() {
        return this.id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getNome() {
        return this.nome;
    }

    void setNome(String nome) {
        this.nome = nome;
    }

    List<Turno> getTurnos() {
        throw new UnsupportedOperationException();
    }
    Turno getTurno(int turno){
        throw new UnsupportedOperationException();
    }
    void marcarPresenca(String aluno, int turno, int aula) {
        throw new UnsupportedOperationException();
    }

    void removerAlunoDeTurno(String aluno, int turno) {
        throw new UnsupportedOperationException();
    }

    void adicionarAlunoTurno(String aluno, int turno) {
        throw new UnsupportedOperationException();
    }

    void trocarAlunos(String aluno1, String aluno2) {
        throw new UnsupportedOperationException();
    }

    void addTurno(int id, boolean ePratico) {
        throw new UnsupportedOperationException();
    }

    void removeTurno(int id) {
        throw new UnsupportedOperationException();
    }
}

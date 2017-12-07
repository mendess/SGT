package main.sgt;

import java.time.LocalDateTime;

public class Troca {
    private LocalDateTime data;
    private String aluno;
    private int turnoOrigem;
    private int turnoDestino;

    public String getAluno() {
        return this.aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public int getTurnoOrigem() {
        return this.turnoOrigem;
    }

    public void setTurnoOrigem(int turnoOrigem) {
        this.turnoOrigem = turnoOrigem;
    }

    public int getTurnoDestino() {
        return this.turnoDestino;
    }

    public void setTurnoDestino(int turnoDestino) {
        this.turnoDestino = turnoDestino;
    }
}
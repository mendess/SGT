package main.sgt;

import java.util.List;

public class Aula {
    private int numero;
    private List<String> presencas;

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void marcarPresenca(String aluno) {
        throw new UnsupportedOperationException();
    }
}
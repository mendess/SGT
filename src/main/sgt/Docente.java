package main.sgt;

import java.util.List;
import java.util.Map;

public class Docente extends Utilizador {
    private Map<String,List<Integer>> turnos;

    public List<String> getUCs() {
        throw new UnsupportedOperationException();
    }

    public void addUC(String uC) {
        throw new UnsupportedOperationException();
    }

    Map<String,List<Integer>> getTurnos() {
        throw new UnsupportedOperationException();
    }

    public void addTurno(String uc, int turno) {
        throw new UnsupportedOperationException();
    }

    public void removeTurno(String uc, int turno) {
        throw new UnsupportedOperationException();
    }

    public void removeUC(String uc) {
        throw new UnsupportedOperationException();
    }
}

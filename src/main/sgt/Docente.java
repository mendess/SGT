package main.sgt;

import main.sgt.exceptions.TurnoJaExisteException;
import main.sgt.exceptions.UCJaExisteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Docente extends Utilizador {

    /**
     * UCs e turnos do docente
     */
    private Map<String, List<TurnoKey>> ucsEturnos;

    /**
     * Construtor do Docente
     * @param userNum O identificador do docente
     * @param password A password do docente
     * @param email O email do docente
     * @param name O nome do docente
     * @param ucsEturnos As ucsEturnos do docente
     */
    public Docente(String userNum, String password, String email, String name, Map<String, List<TurnoKey>> ucsEturnos) {
        super(userNum,password,email,name);
        this.ucsEturnos = ucsEturnos;
    }

    /**
     * Constructor completamente parametrizado do Docente
     * @param userNum O identificador do docente
     * @param password A password do docente
     * @param email O email do docente
     * @param nome O nome do docente
     * @param loginAtivo Se o login esta ativo
     * @param ucsEturnos As ucsEturnos do docente
     */
    public Docente(String userNum, String password, String email, String nome, boolean loginAtivo,
                   Map<String, List<TurnoKey>> ucsEturnos){
        super(userNum, password, email, nome, loginAtivo);
        this.ucsEturnos = ucsEturnos;
    }

    /**
     * Retorna as UCs e turnos do Docente
     * @return UCs e turnos do docente
     */
    public Map<String, List<TurnoKey>> getUcsEturnos() {
        return new HashMap<>(this.ucsEturnos);
    }

    /**
     * Altera as ucs e turnos do docente
     * @param ucsEturnos Novas ucs e turnos
     */
    void setUcsEturnos(Map<String, List<TurnoKey>> ucsEturnos) {
        this.ucsEturnos = ucsEturnos;
    }

    /**
     * Adiciona uma UC ao Docente
     * @param uc Identificador da UC a adicionar
     * @throws UCJaExisteException Se o docente ja da aulas a esta UC
     */
    void addUC(String uc) throws UCJaExisteException {
        if(!this.ucsEturnos.containsKey(uc)){
            this.ucsEturnos.put(uc, new ArrayList<>());
        }else{
            throw new UCJaExisteException();
        }
    }

    /**
     * Adiciona um turno ao docente
     * @param uc Identificador da UC do turno a remover
     * @param turno Numero do turno
     */
    void addTurno(String uc, TurnoKey turno) throws TurnoJaExisteException {
        if(!this.ucsEturnos.get(uc).contains(turno)){
            this.ucsEturnos.get(uc).add(turno);
        }else{
            throw new TurnoJaExisteException();
        }
    }

    /**
     * Remove um turno ao docente
     * @param uc Identificador da UC do turno a remover
     * @param turno Numero do turno a remover
     */
    void removeTurno(String uc, int turno) {
        this.ucsEturnos.get(uc).remove(turno);
    }

    /**
     * Remove uma UC do Docente
     * @param uc Identificador da UC a remover
     */
    void removeUC(String uc) {
        this.ucsEturnos.remove(uc);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        Docente d = (Docente) o;
        return super.equals(o)
                && this.ucsEturnos.equals(d.getUcsEturnos());
    }

    @Override
    public String toString() {
        return "Docente:\t"
                +super.toString()+"\t"
                +"UC e Turnos: "+this.ucsEturnos+"\t";
    }
}
package main.sgt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Docente extends Utilizador {

    /**
     * UCs e turnos do docente
     */
	private Map<String, List<Integer>> ucsEturnos;

	/**
	 * Construtor do Docente
	 * @param userNum O identificador do docente
	 * @param password A password do docente
	 * @param email O email do docente
	 * @param name O nome do docente
	 * @param ucsEturnos As ucsEturnos do docente
	 */
	Docente(String userNum, String password, String email, String name, Map<String, List<Integer>> ucsEturnos) {
		super(userNum,password,email,name);
		this.ucsEturnos = ucsEturnos;
	}

    /**
     * Retorna as UCs e turnos do Docente
     * @return UCs e turnos do docente
     */
    Map<String, List<Integer>> getUcsEturnos() {
        return this.ucsEturnos;
    }

    /**
     * Altera as ucs e turnos do docente
     * @param ucsEturnos Novas ucs e turnos
     */
    void setUcsEturnos(Map<String, List<Integer>> ucsEturnos) {
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
	void addTurno(String uc, int turno) throws TurnoJaExisteException {
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

}
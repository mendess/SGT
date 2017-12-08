package main.sgt;

import java.util.List;
import java.util.Map;

public class Docente extends Utilizador {

	private Map<String, List<Integer>> ucsEturnos;

	/**
	 * 
	 * @param userNum
	 * @param password
	 * @param email
	 * @param name
	 * @param ucs
	 * @param turnos
	 */
	Docente(String userNum, String password, String email, String name, List<String> ucs, Map<String, Integer> turnos) {
		super(userNum,password,email,name);
		throw new UnsupportedOperationException();
	}

	List<String> getUCs() {
		// TODO - implement Docente.getUCs
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param UC
	 */
	void addUC(String UC) {
		// TODO - implement Docente.addUC
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param uc
	 * @param turnos
	 */
	void addTurno(String uc, int turnos) {
		// TODO - implement Docente.addTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param uc
	 * @param turno
	 */
	void removeTurno(String uc, int turno) {
		// TODO - implement Docente.removeTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param uc
	 */
	void removeUC(String uc) {
		// TODO - implement Docente.removeUC
		throw new UnsupportedOperationException();
	}

	Map<String, List<Integer>> getUcsEturnos() {
		return this.ucsEturnos;
	}

	/**
	 * 
	 * @param ucsEturnos
	 */
	void setUcsEturnos(Map<String, List<Integer>> ucsEturnos) {
		this.ucsEturnos = ucsEturnos;
	}

}
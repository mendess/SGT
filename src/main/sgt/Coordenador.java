package main.sgt;

import java.util.List;
import java.util.Map;

public class Coordenador extends Docente {

	/**
	 * O identificador da UC que este Coordenador rege.
	 */
	private String ucRegida;

	/**
	 * O construtor do coordenador
     * @param userNum O identificador do docente
     * @param password A password do docente
     * @param email O email do docente
     * @param name O nome do docente
     * @param ucsEturnos As ucsEturnos do docente
     * @param ucRegida A UC que o coordenador rege.
	 */
	Coordenador(String userNum, String password, String email, String name, Map<String, List<Integer>> ucsEturnos, String ucRegida) {
		super(userNum,password,email,name,ucsEturnos);
		this.ucRegida = ucRegida;
	}

	/**
     * Construtor do Coordenador que nao obriga a indicar qual a UC que este rege.
     * @param userNum O identificador do docente
     * @param password A password do docente
     * @param email O email do docente
     * @param name O nome do docente
     * @param ucsEturnos As ucsEturnos do docente
     */
	Coordenador(String userNum, String password, String email, String name, Map<String, List<Integer>> ucsEturnos) {
		super(userNum,password,email,name,ucsEturnos);
		this.ucRegida = null;
	}

    /**
     * Retorna a UC que este Coordenador rege.
     * @return A UC que este Coordenador rege.
     */
	String getUcRegida() {
		return this.ucRegida;
	}

	/**
	 * Altera a UC que este coordenador rege
	 * @param ucRegida O identificador da UC
	 */
	void setUcRegida(String ucRegida) {
		this.ucRegida = ucRegida;
	}

}
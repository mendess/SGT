package main.sgt;

import java.util.List;
import java.util.Map;

public class Coordenador extends Docente {

	private String ucRegida;

	/**
	 * 
	 * @param userNum
	 * @param password
	 * @param email
	 * @param name
	 * @param ucs
	 * @param turnos
	 * @param ucRegida
	 */
	Coordenador(String userNum, String password, String email, String name, List<String> ucs, Map<String, Integer> turnos, String ucRegida) {
		super(userNum,password,email,name,ucs,turnos);
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param userNum
	 * @param password
	 * @param email
	 * @param name
	 * @param ucs
	 * @param turnos
	 */
	Coordenador(String userNum, String password, String email, String name, List<String> ucs, Map<String, Integer> turnos) {
		super(userNum,password,email,name,ucs,turnos);
		throw new UnsupportedOperationException();
	}

	String getUcRegida() {
		return this.ucRegida;
	}

	/**
	 * 
	 * @param ucRegida
	 */
	void setUcRegida(String ucRegida) {
		this.ucRegida = ucRegida;
	}

}
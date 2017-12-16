package main.sgt;

import java.util.Map;

class Aluno extends Utilizador {

	private boolean eEspecial;
	private Map<String, Integer> horario;

	/**
	 * 
	 * @param userNum
	 * @param password
	 * @param email
	 * @param name
	 * @param eEspecial
	 * @param inscricoes
	 */
	Aluno(String userNum, String password, String email, String name, boolean eEspecial, Map<String, Integer> inscricoes) {
		super(userNum,password,email,name);
        //TODO - implement Aluno.Aluno
		throw new UnsupportedOperationException();
	}

	boolean getEespecial() {
		//TODO - implement Aluno.getEespecial
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param eEspecial
	 */
	void setEespecial(boolean eEspecial) {
		//TODO - implement Aluno.setEespecial
		throw new UnsupportedOperationException();
	}

	Map<String, Integer> getHorario() {
		return this.horario;
	}

	/**
	 * 
	 * @param uc
	 * @param turno
	 */
	void inscrever(String uc, int turno) {
		//TODO - implement Aluno.inscrever
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param uc
	 * @param turno
	 */
	void desinscrever(String uc, int turno) {
		//TODO - implement Aluno.desinscrever
		throw new UnsupportedOperationException();
	}

	void ativarLogin() {
		//TODO - implement Aluno.ativarLogin
		throw new UnsupportedOperationException();
	}

}
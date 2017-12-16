package main.sgt;

import java.util.HashMap;
import java.util.Map;

class Aluno extends Utilizador {

	private boolean eEspecial;
	private Map<String, Integer> horario;

	/**
	 * O construtor do aluno
	 * @param userNum O identificador do aluno
	 * @param password A password do aluno
	 * @param email O email do aluno
	 * @param name O nome do aluno
	 * @param eEspecial <tt>true</tt> se o aluno tiver estatuto especial
	 * @param inscricoes As ucs e turnos a que o aluno esta inscrito
	 */
	Aluno(String userNum, String password, String email, String name, boolean eEspecial, Map<String, Integer> inscricoes) {
		super(userNum,password,email,name);
		this.eEspecial = eEspecial;
		this.horario = inscricoes;
	}

    /**
     * Retorna se o aluno tem estatuto especial
     * @return <tt>true</tt> se o aluno tem estatuto especial, <tt>false</tt> caso contrario.
     */
	boolean eEspecial() {
	    return this.eEspecial;
	}

	/**
	 * Muda o estatuto do aluno
	 * @param eEspecial Novo estatuto.
	 */
	void setEespecial(boolean eEspecial) {
	    this.eEspecial = eEspecial;
	}

    /**
     * Retorna o horario do aluno.
     * @return O horario do aluno
     */
	Map<String, Integer> getHorario() {
		return new HashMap<>(this.horario);
	}

	/**
	 * Inscreve um aluno num turno. WARNING este metodo tambem retira o aluno do turno onde estava
	 * @param uc O identificador da UC do turno
	 * @param turno O numero do turno
	 */
	void inscrever(String uc, int turno) {
	    this.horario.put(uc,turno);
	}

	/**
	 * Desinscreve um aluno de um turno
	 * @param uc Identificador da UC do turno
	 * @param turno Numero do turno
	 */
	void desinscrever(String uc, int turno) {
	    this.horario.put(uc,null);
	}

    /**
     * Ativa o login deste aluno, enviando lhe um email com o seu numero e a sua password.
     */
	void ativarLogin() {
		//TODO - implement Aluno.ativarLogin
		throw new UnsupportedOperationException();
	}

}
package main.sgt;

import java.util.ArrayList;
import java.util.List;

public class Aula {

	private int numero;
	private List<String> presencas;
	private int turno;
	private String uc;

	/**
	 * Construtor de aula
	 * @param uc A UC do turno
     * @param turno O numero do turno da aula
     */
    Aula(int turno, String uc, int numero) {
        this.uc = uc;
        this.turno = turno;
        this.numero = numero;
        this.presencas = new ArrayList<>();
	}

	int getNumero() {
		return this.numero;
	}

	/**
	 * Marca presenca a um aluno
	 * @param aluno Identificador do aluno
	 */
	void marcarPresenca(String aluno) {
	    if(!this.presencas.contains(aluno)){
	        this.presencas.add(aluno);
        }
	}

	int getTurno() {
		return this.turno;
	}

	String getUc() {
		return this.uc;
	}

}
package main.sgt;

import java.util.ArrayList;
import java.util.List;

public class Aula {

    /**
     * O numero da aula
     */
	private int numero;
    /**
     * A lista dos alunos que estiveram presentes nesta aula
     */
    private List<String> presencas;
    /**
     * O numero do turno a que a aula pertence
     */
	private int turno;
    /**
     * O identificador da UC a que a aula pertence
     */
	private String uc;

	/**
	 * Construtor de aula
	 * @param uc A UC do turno
     * @param turno O numero do turno da aula
     */
    public Aula(int turno, String uc, int numero) {
        this.uc = uc;
        this.turno = turno;
        this.numero = numero;
        this.presencas = new ArrayList<>();
	}

    public Aula(int numero, String uc, int turno, List<String> presencas) {
        this.numero = numero;
        this.uc = uc;
        this.turno = turno;
        this.presencas = presencas;
    }

    /**
     * Retorna o numero da Aula
     * @return O numero da Aula
     */
	public int getNumero() {
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

    /**
     * Retorna o numero do turno a que a aula pertence
     * @return O numero do turno a que a aula pertence
     */
	public int getTurno() {
		return this.turno;
	}

    /**
     * Retorna o identificador da UC a que a aula pertence
     * @return O identificador da UC a que a aula pertence
     */
	public String getUc() {
		return this.uc;
	}



}
package main.sgt;

import java.time.LocalDateTime;

public class Troca {

	private String aluno;
	private String uc;
	private int turnoOrigem;
	private int turnoDestino;
	private LocalDateTime data;

	/**
	 * Construtor de troca.
	 * @param aluno O identificador do aluno que mudou de turno
	 * @param uc A UC dentro da qual a troca foi efetuada
	 * @param turnoOrigem O turno de onde o aluno veio
	 * @param turnoDestino O turno para onde o aluno foi
	 */
	Troca(String aluno, String uc, int turnoOrigem, int turnoDestino) {
		this.aluno = aluno;
		this.uc = uc;
		this.turnoOrigem = turnoOrigem;
		this.turnoDestino = turnoDestino;
		this.data = LocalDateTime.now();
	}

	/**
	 * Retorna o identificador do aluno
	 * @return o identificador do aluno
	 */
	String getAluno() {
		return this.aluno;
	}

	/**
	 * Retorna a UC onde foi feita a troca
	 * @return A UC onde foi feita a troca
	 */
	String getUc() {
		return this.uc;
	}

	/**
	 * Retorna o turno de onde o aluno que trocou veio
	 * @return O turno de onde o aluno que trocou veio
	 */
	int getTurnoOrigem() {
		return this.turnoOrigem;
	}

	/**
	 * Retorna o turno para onde o aluno foi
	 * @return O turno para onde o aluno foi
	 */
	int getTurnoDestino() {
		return this.turnoDestino;
	}

	/**
	 * Retorna a data da troca
	 * @return A data da troca
	 */
	LocalDateTime getData() {
		return this.data;
	}

}
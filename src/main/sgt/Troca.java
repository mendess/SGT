package main.sgt;

import java.time.LocalDateTime;

public class Troca {

	private String aluno;
	private String uc;
	private int turnoOrigem;
	private int turnoDestino;
	private LocalDateTime data;

	/**
	 * 
	 * @param aluno
	 * @param uc
	 * @param turnoOrigem
	 * @param turnoDestino
	 */
	Troca(String aluno, String uc, int turnoOrigem, int turnoDestino) {
		//TODO - implement Troca.Troca
		throw new UnsupportedOperationException();
	}

	String getAluno() {
		return this.aluno;
	}

	String getUc() {
		return this.uc;
	}

	int getTurnoOrigem() {
		return this.turnoOrigem;
	}

	int getTurnoDestino() {
		return this.turnoDestino;
	}

	LocalDateTime getData() {
		return this.data;
	}

}
package main.sgt;

import java.util.List;

public class Turno {

	private int id;
	private List<String> alunos;
	private String docente;
	private boolean ePratico;
	private int vagas;
	private String ucId;

	/**
	 * 
	 * @param id
	 * @param ePratico
	 * @param vagas
	 * @param ucId
	 * @param alunos
	 * @param docente
	 */
	Turno(int id, boolean ePratico, int vagas, String ucId, List<String> alunos, String docente) {
		// TODO - implement Turno.Turno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param ePratico
	 * @param vagas
	 * @param ucId
	 */
	Turno(int id, boolean ePratico, int vagas, String ucId) {
		// TODO - implement Turno.Turno
		throw new UnsupportedOperationException();
	}

	boolean getEPratico() {
		return this.ePratico;
	}

	/**
	 * 
	 * @param ePratico
	 */
	void setEPratico(boolean ePratico) {
		this.ePratico = ePratico;
	}

	List<String> getAlunos() {
		return this.alunos;
	}

	/**
	 * 
	 * @param alunos
	 */
	void addAluno(String alunos) {
		// TODO - implement Turno.addAluno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno
	 */
	void removeAluno(String aluno) {
		// TODO - implement Turno.removeAluno
		throw new UnsupportedOperationException();
	}

	int getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	void setId(int id) {
		this.id = id;
	}

	int getVagas() {
		return this.vagas;
	}

	/**
	 * 
	 * @param vagas
	 */
	void setVagas(int vagas) {
		this.vagas = vagas;
	}

	/**
	 * 
	 * @param aluno
	 * @param aula
	 */
	void marcarPresenca(String aluno, int aula) {
		// TODO - implement Turno.marcarPresenca
		throw new UnsupportedOperationException();
	}

	String getUcId() {
		return this.ucId;
	}

	/**
	 * 
	 * @param ucId
	 */
	void setUcId(String ucId) {
		this.ucId = ucId;
	}

}
package main.sgt;

import java.util.List;

public class UC {

	private String id;
	private String nome;
	private String responsavel;
	private List<String> docentes;
	private List<String> alunos;

	/**
	 * 
	 * @param id
	 * @param nome
	 * @param responsavel
	 * @param docentes
	 * @param alunos
	 */
	UC(String id, String nome, String responsavel, List<String> docentes, List<String> alunos) {
		// TODO - implement UC.UC
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param nome
	 */
	UC(String id, String nome) {
		// TODO - implement UC.UC
		throw new UnsupportedOperationException();
	}

	String getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	void setId(String id) {
		this.id = id;
	}

	String getNome() {
		return this.nome;
	}

	/**
	 * 
	 * @param nome
	 */
	void setNome(String nome) {
		this.nome = nome;
	}

	List<Turno> getTurnos() {
		// TODO - implement UC.getTurnos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno
	 * @param turno
	 * @param aula
	 */
	void marcarPresenca(String aluno, int turno, int aula) {
		// TODO - implement UC.marcarPresenca
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno
	 * @param turno
	 */
	void removerAlunoDeTurno(String aluno, int turno) {
		// TODO - implement UC.removerAlunoDeTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno
	 * @param turno
	 */
	void adicionarAlunoTurno(String aluno, int turno) {
		// TODO - implement UC.adicionarAlunoTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno1
	 * @param aluno2
	 */
	void trocarAlunos(String aluno1, String aluno2) {
		// TODO - implement UC.trocarAlunos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param ePratico
	 */
	void addTurno(int id, boolean ePratico) {
		// TODO - implement UC.addTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	void removeTurno(int id) {
		// TODO - implement UC.removeTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param turno
	 */
	Turno getTurno(int turno) {
		// TODO - implement UC.getTurno
		throw new UnsupportedOperationException();
	}

}
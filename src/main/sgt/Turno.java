package main.sgt;

import main.dao.AulaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Turno {

	private int id;
	private List<String> alunos;
	private String docente;
	private boolean ePratico;
	private int vagas;
	private String ucId;
	private AulaDAO aulas;

	/**
	 * Construtor completamente parametrizado do <tt>Turno</tt>.
	 * @param id Numero do turno
	 * @param ePratico Se o turno é pratico ou teorico
	 * @param vagas Numero de vagas do turno
	 * @param ucId Identificador da UC a que pertence
	 * @param alunos Alunos do turno
	 * @param docente Docentes do turno
	 */
	Turno(int id, boolean ePratico, int vagas, String ucId, List<String> alunos, String docente) {
		this.id = id;
		this.ePratico = ePratico;
		this.vagas = vagas;
		this.ucId = ucId;
		this.alunos = alunos;
		this.docente = docente;
		this.aulas = new AulaDAO();
	}

	/**
	 * Construtor com apenas o estritamente necessario para criar um <tt>Turno</tt>, ou seja, nao inclui os participantes
     * no mesmo.
	 * @param id Numero do turno
     * @param ePratico Se o turno é pratico ou teorico
     * @param vagas Numero de vagas do turno
     * @param ucId Identificador da UC a que pertence
	 */
	Turno(int id, boolean ePratico, int vagas, String ucId) {
        this.id = id;
        this.ePratico = ePratico;
        this.vagas = vagas;
        this.ucId = ucId;
        this.aulas = new AulaDAO();
	}

    /**
     * Retorna o numero do turno.
     * @return O numero do turno.
     */
    int getId() {
        return this.id;
    }

    /**
     * Retorna a lista de alunos do turno.
     * @return Lista de alunos do turno.
     */
    List<String> getAlunos() {
        return this.alunos;
    }

    /**
     * Retorna o identificador do docente.
     * @return O identificador do docente.
     */
    String getDocente(){
        return this.docente;
    }

    /**
     * Retorna <tt>true</tt> se o turno for pratico, <tt>false</tt> caso contrario.
     * @return <tt>true</tt> se o turno for pratico, <tt>false</tt> caso contrario.
     */
	boolean ePratico() {
		return this.ePratico;
	}

    /**
     * Retorna o numero de vagas
     * @return O numero de vagas
     */
    int getVagas() {
        return this.vagas;
    }

    /**
     * Retorna o identificador da UC a que o turno pertence.
     * @return o identificador da UC a que o turno pertence.
     */
    String getUcId() {
        return this.ucId;
    }

    /**
     * A lista de aulas a que o turno tem.
     * @return Lista de aulas que o turno tem.
     */
    List<Aula> getAulas(){
	    return new ArrayList<>(this.aulas.values());
    }

    /**
     *
     * @param ucId Novo identificador da UC
     */
    void setUcId(String ucId) {
        this.ucId = ucId;
    }

	/**
	 *
	 * @param ePratico O novo estado do turno.
	 */
	void setEPratico(boolean ePratico) {
		this.ePratico = ePratico;
	}

	/**
	 *
	 * @param id Novo id do turno
	 */
	void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 * @param vagas Numero de vagas
	 */
	void setVagas(int vagas) {
		this.vagas = vagas;
	}

    /**
     * Adiciona um aluno ao turno.
     * @param aluno Identificador do aluno
     */
    void addAluno(String aluno) throws UtilizadorJaExisteException {
        if (this.alunos.contains(aluno)){
            throw new UtilizadorJaExisteException();
        }else{
            this.alunos.add(aluno);
        }
    }

    /**
     * Remove um aluno do turno
     * @param aluno Identificador do aluno
     */
    void removeAluno(String aluno) {
        this.alunos.remove(aluno);
    }

	/**
	 * Marca um aluno como presente numa aula.
	 * @param aluno Identificador do aluno.
	 * @param aula Numero da aula
	 */
	void marcarPresenca(String aluno, int aula) {
	    Aula a = this.aulas.get(aula);
	    a.marcarPresenca(aluno);
	    this.aulas.put(a.getNumero(),a);
	}

}
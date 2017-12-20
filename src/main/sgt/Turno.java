package main.sgt;

import main.dao.AulaDAO;
import main.sgt.exceptions.UtilizadorJaExisteException;

import java.util.ArrayList;
import java.util.List;

public class Turno {


	public static Turno emptyShift(String uc){
	    return new Turno(0,false,Integer.MAX_VALUE,uc);
    }

    /**
     * Numero do turno
     */
	private int id;
    /**
     * Lista dos alunos
     */
	private List<String> alunos;
    /**
     * Docente que leciona o turno
     */
	private String docente;
    /**
     * Se e um turno pratico
     */
	private boolean ePratico;
    /**
     * Numero de vagas do turno
     */
	private int vagas;
    /**
     * Identificador a que o turno pertence
     */
	private String ucId;
    /**
     * Aulas do turno
     */
	private AulaDAO aulas;
    /**
     * Informacoes do turno
     */
    private List<TurnoInfo> tinfo;

    /**
	 * Construtor completamente parametrizado do <tt>Turno</tt>.
	 * @param id Numero do turno
	 * @param ePratico Se o turno é pratico ou teorico
	 * @param vagas Numero de vagas do turno
	 * @param ucId Identificador da UC a que pertence
	 * @param alunos Alunos do turno
	 * @param docente Docentes do turno
	 */
	public Turno(int id, boolean ePratico, int vagas, String ucId, List<String> alunos, String docente, List<TurnoInfo> tinfo) {
		this.id = id;
		this.ePratico = ePratico;
		this.vagas = vagas;
		this.ucId = ucId;
		this.alunos = alunos;
		this.docente = docente;
        this.tinfo = tinfo;
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
	public Turno(int id, boolean ePratico, int vagas, String ucId) {
        this.id = id;
        this.alunos = new ArrayList<>();
        this.docente= null;
        this.ePratico = ePratico;
        this.vagas = vagas;
        this.ucId = ucId;
        this.aulas = new AulaDAO();
        this.tinfo = new ArrayList<>();
	}

    /**
     * Retorna o numero do turno.
     * @return O numero do turno.
     */
	public int getId() {
        return this.id;
    }

    /**
     * Retorna a lista de alunos do turno.
     * @return Lista de alunos do turno.
     */
	public List<String> getAlunos() {
        return new ArrayList<>(this.alunos);
    }

    /**
     * Retorna o identificador do docente.
     * @return O identificador do docente.
     */
	public String getDocente(){
        return this.docente;
    }

    /**
     * Retorna <tt>true</tt> se o turno for pratico, <tt>false</tt> caso contrario.
     * @return <tt>true</tt> se o turno for pratico, <tt>false</tt> caso contrario.
     */
	public boolean ePratico() {
		return this.ePratico;
	}

    /**
     * Retorna o numero de vagas
     * @return O numero de vagas
     */
	public int getVagas() {
        return this.vagas;
    }

    /**
     * Retorna o identificador da UC a que o turno pertence.
     * @return o identificador da UC a que o turno pertence.
     */
	public String getUcId() {
        return this.ucId;
    }

    /**
     * A lista de aulas a que o turno tem.
     * @return Lista de aulas que o turno tem.
     */
	public List<Aula> getAulas(){
	    return new ArrayList<>(this.aulas.values());
    }

    /**
     * Retorna as informacoes do turno
     * @return As informacoes do turno
     */
	public List<TurnoInfo> getTurnoInfos() {
        List<TurnoInfo> tinfos = new ArrayList<>(this.tinfo.size());
        for(TurnoInfo tinfo : this.tinfo){
            tinfos.add(new TurnoInfo(tinfo));
        }
        return tinfos;
    }

    /**
     * Define as informacoes do turno
     * @param turnoInfos As novas informacoes do turno
     */
    void setTurnoInfos(List<TurnoInfo> turnoInfos) {
        this.tinfo = new ArrayList<>(turnoInfos.size());
        for(TurnoInfo t : turnoInfos){
            this.tinfo.add(new TurnoInfo(t));
        }
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
     * Altera o docente que leciona o turno
     * @param docente Novo docente
     */
	void setDocente(String docente){
	    this.docente=docente;
    }

    /**
	 * Adiciona um aluno ao turno
	 * @param aluno Identificador do aluno a adicionar
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
	    AulaKey aKey = new AulaKey(this.ucId,this.id,aula);
		Aula a = this.aulas.get(aKey);
	    a.marcarPresenca(aluno);
	    this.aulas.put(aKey,a);
	}

    /**
     * Adiciona uma aula ao turno
     */
	void addAula() {
        int num = this.aulas.maxID(this.ucId,this.id);
        Aula a = new Aula(this.id,this.ucId,num);
        this.aulas.put(new AulaKey(a),a);
    }

	/**
	 * Remove uma aula do turno
	 * @param aula Numero da aula a remover
	 */
    void removeAula(int aula) {
	    this.aulas.remove(new AulaKey(this.ucId,this.id,aula));
	}

}
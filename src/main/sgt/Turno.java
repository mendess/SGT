package main.sgt;

import main.dao.AulaDAO;
import main.sgt.exceptions.UtilizadorJaExisteException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Turno {


    public static Turno emptyShift(String uc){
        return new Turno(0, uc, Integer.MAX_VALUE, true);
    }

    /**
     * Numero do turno
     */
    private int id;

    /**
     * Identificador a que o turno pertence
     */
    private String ucId;
    /**
     * Docente que leciona o turno
     */
    private String docente;
    /**
     * Numero de vagas do turno
     */
    private int vagas;
    /**
     * Se e um turno pratico
     */
    private boolean ePratico;
    /**
     * Aulas do turno
     */
    private final AulaDAO aulas = new AulaDAO();
    /**
     * Lista dos alunos
     */
    private List<String> alunos = new ArrayList<>();
    /**
     * Informacoes do turno
     */
    private List<TurnoInfo> tinfo = new ArrayList<>();
    /**
     * Construtor completamente parametrizado do <tt>Turno<\tt>.
     * @param id Numero do turno
     * @param ucId Identificador da UC a que pertence
     * @param docente Docentes do turno
     * @param vagas Numero de vagas do turno
     * @param ePratico Se o turno é pratico ou teorico
     * @param alunos Alunos do turno
     * @param tinfo Informacoes do turno
     */
    public Turno(int id, String ucId, String docente, int vagas, boolean ePratico, List<String> alunos, List<TurnoInfo> tinfo) {
        this.id = id;
        this.ePratico = ePratico;
        this.vagas = vagas;
        this.ucId = ucId;
        this.alunos = alunos;
        this.docente = docente;
        this.tinfo = tinfo;
    }

    /**
     * Construtor parameterizado do <tt>Turno</tt> sem utilizadores
     * @param id Numero do turno
     * @param ucId Identificador da UC a que pertence
     * @param vagas Numero de vagas do turno
     * @param ePratico Se o turno é pratico ou teorico
     * @param tinfo Informacoes do turno
     */
    public Turno(int id, String ucId, int vagas, boolean ePratico, List<TurnoInfo> tinfo) {
        this.id = id;
        this.ucId = ucId;
        this.vagas = vagas;
        this.ePratico = ePratico;
        this.tinfo = tinfo;
    }

    /**
     * Construtor com apenas o estritamente necessario para criar um <tt>Turno<\tt>, ou seja, nao inclui os participantes
     * no mesmo.
     * @param id Numero do turno
     * @param ucId Identificador da UC a que pertence
     * @param vagas Numero de vagas do turno
     * @param ePratico Se o turno é pratico ou teorico
     */
    public Turno(int id, String ucId, int vagas, boolean ePratico) {
        this.id = id;
        this.alunos = new ArrayList<>();
        this.docente= null;
        this.ePratico = ePratico;
        this.vagas = vagas;
        this.ucId = ucId;
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
     * Retorna <tt>true<\tt> se o turno for pratico, <tt>false<\tt> caso contrario.
     * @return <tt>true<\tt> se o turno for pratico, <tt>false<\tt> caso contrario.
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
        return new ArrayList<>(this.aulas.values()
                            .stream()
                            .filter(a->a.getUc().equals(this.ucId) && a.getTurno()==this.id)
                            .collect(Collectors.toList()));
    }

    /**
     * Retorna a aula para a dada chave
     * @param aulaNum O numero da aula
     * @return A aula com o dado numero
     */
    public Aula getAula(int aulaNum){
        return this.aulas.get(new AulaKey(this.ucId,this.id,aulaNum,this.ePratico));
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
            if(this.id!=0) throw new UtilizadorJaExisteException();
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
        AulaKey aKey = new AulaKey(this.ucId,this.id,aula, ePratico);
        Aula a = this.aulas.get(aKey);
        a.marcarPresenca(aluno);
        this.aulas.put(aKey,a);
    }

    /**
     * Adiciona uma aula ao turno
     */
    void addAula() {
        int num = this.aulas.maxID(this.ucId,this.id, ePratico);
        Aula a = new Aula(num, this.ucId, this.id, ePratico);
        this.aulas.put(new AulaKey(a),a);
    }

    /**
     * Remove uma aula do turno
     * @param aula Numero da aula a remover
     */
    void removeAula(int aula) {
        this.aulas.remove(new AulaKey(this.ucId,this.id,aula, ePratico));
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        Turno a = (Turno) o;
        return this.id == a.getId() &&
                this.ucId.equals(a.getUcId()) &&
                (this.docente==null ?
                    a.getDocente()==null :
                    this.docente.equals(a.getDocente())) &&
                this.vagas == a.getVagas() &&
                this.ePratico==(a.ePratico()) &&
                this.alunos.equals(a.getAlunos()) &&
                this.tinfo.equals(a.getTurnoInfos());
    }
    public String toString() {
        return "Turno \t"
                + "Id: "+ this.id + ": \t"
                + "UC do Turno: " + this.ucId + "\t"
                + "Docente: " + this.docente + "\t"
                + "Vagas: " + this.vagas + "\t"
                + "Pratico: " + this.ePratico + "\t"
                + "Alunos: " + this.alunos.toString() + "\t"
                + "Informações: " + this.tinfo.toString();
    }
}
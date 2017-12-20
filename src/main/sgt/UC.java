package main.sgt;

import main.dao.TurnoDAO;
import main.sgt.exceptions.AlunoNaoEstaInscritoNaUcException;
import main.sgt.exceptions.TurnoNaoVazioException;
import main.sgt.exceptions.UtilizadorJaExisteException;
import main.sgt.exceptions.UtilizadorNaoExisteException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UC {

    /**
     * Id da UC
     */
    private String id;
    /**
     * Nome da UC
     */
    private String nome;
    /**
     * Acronimo da cadeira
     */
    private String acron;
    /**
     * Docente responsavel pela UC (Coordenador)
     */
    private String responsavel;
    /**
     * Docentes da UC
     */
    private List<String> docentes;
    /**
     * Alunos da UC
     */
    private List<String> alunos;
    /**
     * Turnos desta UC
     */
    private TurnoDAO turnos;

    /**
     * Construtor de UC que aceita o id e o nome
     * @param id Identificador da UC
     * @param nome Nome da UC
     * @param acron Acronimo da UC
     */
    public UC(String id, String nome, String acron) {
        this.id = id;
        this.nome = nome;
        this.acron = acron;
        this.responsavel = null;
        this.docentes = new ArrayList<>();
        this.alunos = new ArrayList<>();
        this.turnos = new TurnoDAO();
    }

    public UC(String id, String nome, String acron, String responsavel){
        this.id = id;
        this.nome = nome;
        this.acron = acron;
        this.responsavel = responsavel;
    }

    public UC(String id, String nome, String acron, String responsavel,List<String> docentes,List<String> alunos){
        this.id = id;
        this.nome = nome;
        this.acron = acron;
        this.responsavel = responsavel;
        this.docentes = docentes;
        this.alunos = alunos;
    }

    /**
     * Retorna o identificador da UC
     * @return O identificador da UC
     */
    public String getId() {
        return this.id;
    }

    /**
     * Altera o identificador da UC
     * @param id Novo identificador
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna o nome da UC
     * @return O nome da UC
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Altera o Nome da UC
     * @param nome Novo nome
     */
    void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o acronimo da UC
     * @return O acronimo da UC
     */
    public String getAcron() {
        return acron;
    }

    /**
     * Altera o acronimo da UC
     * @param acron Novo acronimo da UC
     */
    public void setAcron(String acron) {
        this.acron = acron;
    }

    /**
     * Retorna o identificador do Docente responsavel
     * @return O identificador do Docente responsavel
     */
    public String getResponsavel() {
        return this.responsavel;
    }

    /**
     * Altera o responsavel pela UC
     * @param responsavel Numero do novo responsavel
     */
    void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    /**
     * Retorna os docentes que lecionam a UC
     * @return Os docentes que lecionam a UC
     */
    public List<String> getDocentes() {
        return new ArrayList<>(this.docentes);
    }

    /**
     * Retorna a lista de alunos do turno
     * @return A lista de alunos do turno
     */
    public List<String> getAlunos(){
        return new ArrayList<>(this.alunos);
    }

    /**
     * Adiciona um docente à UC
     * @param docente Numero do docente a adicionar
     * @throws UtilizadorJaExisteException Quando o docente já leciona a UC
     */
    @Deprecated
    void addDocente(String docente) throws UtilizadorJaExisteException {
        if(!this.docentes.contains(docente)){
            this.docentes.add(docente);
        }else{
            throw new UtilizadorJaExisteException();
        }
    }

    /**
     * Remove um Docente da UC
     * @param docente Numero do docente a remover
     * @throws UtilizadorNaoExisteException Quando o docente nao leciona a UC
     */
    @Deprecated
    void removeDocente(String docente) throws UtilizadorNaoExisteException {
        if(this.docentes.contains(docente)){
            this.docentes.add(docente);
        }else{
            throw new UtilizadorNaoExisteException();
        }
    }

    /**
     * Retorna a lista de turno desta UC
     * @return A lista de turnos desta UC
     */
    public List<Turno> getTurnos() {
        return new ArrayList<>(this.turnos.values());
    }

    /**
     * Marca um aluno como presente numa aula
     * @param aluno Aluno
     * @param turno Turno a que a aula pertence
     * @param aula Aula
     */
    void marcarPresenca(String aluno, int turno, int aula) {
        this.turnos.get(new TurnoKey(this.id,turno)).marcarPresenca(aluno,aula);
    }

    /**
     * Remove um aluno de um turno
     * @param aluno Aluno a remover
     * @param turno Turno de onde remover
     */
    void removerAlunoDeTurno(String aluno, int turno) {
        this.turnos.get(new TurnoKey(this.id,turno)).removeAluno(aluno);
    }

    /**
     * Adiciona um aluno a um turno
     * @param aluno Aluno a adicionar
     * @param turno Turno onde adicionar
     */
    void adicionarAlunoTurno(String aluno, int turno) throws UtilizadorJaExisteException {
        this.turnos.get(new TurnoKey(this.id,turno)).addAluno(aluno);
    }

    /**
     * Troca dois alunos entre si
     * @param aluno1 Aluno 1
     * @param aluno2 Aluno 2
     * @return Lista das trocas efetuadas
     * @throws AlunoNaoEstaInscritoNaUcException Um dos alunos nao esta inscrito nesta UC
     */
    Troca[] trocarAlunos(Aluno aluno1, Aluno aluno2) throws AlunoNaoEstaInscritoNaUcException {
        Troca t1 = moveAlunoToTurno(aluno1,aluno2.getHorario().get(this.id));
        Troca t2 = moveAlunoToTurno(aluno2,aluno1.getHorario().get(this.id));
        return new Troca[]{t1,t2};
    }

    /**
     * Move um aluno do turno em que esta para outro
     * @param aluno Aluno
     * @param turno Numero do turno
     * @return Registo da Troca efetuada
     * @throws AlunoNaoEstaInscritoNaUcException O aluno nao esta inscrito nesta UC
     */
    Troca moveAlunoToTurno(Aluno aluno, int turno) throws AlunoNaoEstaInscritoNaUcException {
        if(this.alunos.contains(aluno.getUserNum())){
            Turno OldShift1 = this.getTurno(aluno.getHorario().get(this.id));
            Turno OldShift2 = this.getTurno(turno);
            try {
                OldShift2.addAluno(aluno.getUserNum());
            } catch (UtilizadorJaExisteException e) {
                e.printStackTrace();
                return null;
            }
            OldShift1.removeAluno(aluno.getUserNum());
            this.turnos.put(new TurnoKey(OldShift1),OldShift1);
            this.turnos.put(new TurnoKey(OldShift2),OldShift2);
            // TODO CHANGE THIS
            return new Troca(1,aluno.getUserNum(), this.getId(),OldShift1.getId(),OldShift2.getId());
        }else{
            throw new AlunoNaoEstaInscritoNaUcException(aluno.getUserNum());
        }
    }

    /**
     * Adiciona um turno à UC
     * @param ePratico <tt>True</tt> se for um turno pratico
     * @param vagas Numero de vagas do turno
     * @return id O numero do turno
     */
    int addTurno(boolean ePratico, int vagas) {
        int id = this.turnos.maxID();
        Turno t = new Turno(id,ePratico,vagas,this.id);
        this.turnos.put(new TurnoKey(t),t);
        return id;
    }

    /**
     * Remove um turno da UC
     * @param id Identificador do turno a remover
     */
    void removeTurno(int id) throws TurnoNaoVazioException {
        TurnoKey tKey = new TurnoKey(this.id,id);
        if(this.turnos.get(tKey).getAlunos().isEmpty()){
            this.turnos.remove(tKey);
        }else{
            throw new TurnoNaoVazioException();
        }
    }

    /**
     * Retorna um <tt>Turno</tt>
     * @param turno Identificador do turno
     * @return O turno com o dado id
     */
    public Turno getTurno(int turno) {
        return this.turnos.get(new TurnoKey(this.id,turno));
    }

    /**
     * Adiciona um aluno a um curso
     * @param aluno Numero do aluno a adicionar
     * @throws UtilizadorJaExisteException Quando o aluno já esta inscrito na UC
     */
    void addAluno(String aluno) throws UtilizadorJaExisteException {
        if(!this.alunos.contains(aluno)){
            this.alunos.add(aluno);
        }else{
            throw new UtilizadorJaExisteException();
        }
    }

    /**
     * Remove um aluno do turno
     * @param aluno Numero do aluno a remover
     * @throws UtilizadorNaoExisteException Quando o aluno não esta inscrito na UC
     */
    void removeAluno(String aluno) throws UtilizadorNaoExisteException {
        if(this.alunos.contains(aluno)){
            this.alunos.remove(aluno);
        }else{
            throw new UtilizadorNaoExisteException();
        }
    }

    /**
     * Adiciona uma nova aula a um turno
     * @param turno Numero do turno
     */
    void addAula(int turno) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno));
        tmpTurno.addAula();
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
    }

    /**
     * Remove uma aula a um turno
     * @param turno Numero do turno onde remover
     * @param aula Numero da aula a remover
     */
    void removeAula(int turno, int aula) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno));
        tmpTurno.removeAula(aula);
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
    }

    void addDocenteToTurno(int turno, String docente) {
        if(this.docentes.contains(docente)){
            this.docentes.add(docente);
        }
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno));
        tmpTurno.setDocente(docente);
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
    }

    void removeDocenteFromTurno(int turno, String docente) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno));
        tmpTurno.setDocente(null);
        this.turnos.put(new TurnoKey(this.id,tmpTurno.getId()),tmpTurno);
        if(this.numTurnosLecionados(docente)==1){
            this.docentes.remove(docente);
        }
    }

    private int numTurnosLecionados(String docente) {
        int count=0;
        for(Turno t: this.turnos.values()){
            if(t.getDocente().equals(docente)) count++;
        }
        return count;
    }

    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        UC a =(UC) o;
        return this.id.equals(a.getId()) &&
                this.nome.equals(a.getNome()) &&
                this.acron.equals(a.getAcron()) &&
                this.responsavel.equals(a.getResponsavel()) &&
                this.docentes.equals(a.getDocentes()) &&
                this.alunos.equals(a.getAlunos());
    }

    public String toString() {
        return "UC: \t" +
                "ID: " + this.id + "\t" +
                "Nome: " + this.nome + "\t" +
                "Acronimo: " + this.acron + "\t" +
                "responsavel: " + this.responsavel + "\t" +
                "Docentes: " + this.docentes.toString() + "\t" +
                "Alunos: " + this.alunos.toString() + "\t";
    }

}
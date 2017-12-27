package main.sgt;

import main.dao.TurnoDAO;
import main.sgt.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final TurnoDAO turnos = new TurnoDAO();

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
        return new ArrayList<>(this.turnos.values()
                .stream()
                .filter(t->t.getUcId().equals(this.id))
                .collect(Collectors.toList()));
    }

    /**
     * Marca um aluno como presente numa aula
     * @param aluno Aluno
     * @param turno Turno a que a aula pertence
     * @param aula Aula
     * @param ePratico Se o turno e pratico
     */
    void marcarPresenca(String aluno, int turno, int aula, boolean ePratico) {
        this.turnos.get(new TurnoKey(this.id,turno,ePratico)).marcarPresenca(aluno,aula);
    }

    /**
     * Remove um aluno de um turno
     * @param aluno Aluno a remover
     * @param turno Turno de onde remover
     * @return A troca resultante
     */
    Troca removerAlunoDeTurno(Aluno aluno, int turno) throws AlunoNaoEstaInscritoNaUcException, UtilizadorJaExisteException {
        try {
            return moveAlunoToTurno(aluno,0);
        } catch (TurnoCheioException e) {
            return null;
        }
    }

    /**
     * Troca dois alunos entre si
     * @param aluno1 Aluno 1
     * @param aluno2 Aluno 2
     * @return Lista das trocas efetuadas
     * @throws AlunoNaoEstaInscritoNaUcException Um dos alunos nao esta inscrito nesta UC
     */
    Troca[] trocarAlunos(Aluno aluno1, Aluno aluno2) throws AlunoNaoEstaInscritoNaUcException, UtilizadorJaExisteException, TurnoCheioException {
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
     * @throws UtilizadorJaExisteException Se o aluno ja existe no turno
     * @throws TurnoCheioException Se o turno esta cheio
     */
    Troca moveAlunoToTurno(Aluno aluno, int turno) throws AlunoNaoEstaInscritoNaUcException, UtilizadorJaExisteException, TurnoCheioException {
        if(this.alunos.contains(aluno.getUserNum())){
            Turno turnoOrigem = this.getTurno(aluno.getHorario().get(this.id),true);
            Turno turnoDestino = this.getTurno(turno, true);
            turnoDestino.addAluno(aluno.getUserNum());
            turnoOrigem.removeAluno(aluno.getUserNum());
            this.turnos.put(new TurnoKey(turnoOrigem),turnoOrigem);
            this.turnos.put(new TurnoKey(turnoDestino),turnoDestino);
            return new Troca(aluno.getUserNum(),this.getId(),
                    turnoOrigem.getId(),turnoOrigem.ePratico(),turnoDestino.getId(),turnoDestino.ePratico());
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
        int id = this.turnos.maxID(this.id,ePratico);
        Turno t = new Turno(id, this.id, vagas, ePratico);
        this.turnos.put(new TurnoKey(t),t);
        return id;
    }

    /**
     * Remove um turno da UC
     * @param id Identificador do turno a remover
     * @param ePratico Se o turno e pratico
     */
    void removeTurno(int id, boolean ePratico) throws TurnoNaoVazioException {
        TurnoKey tKey = new TurnoKey(this.id,id,ePratico);
        if(this.turnos.get(tKey).getAlunos().isEmpty()){
            this.turnos.remove(tKey);
        }else{
            throw new TurnoNaoVazioException();
        }
    }

    /**
     * Retorna um <tt>Turno</tt>
     * @param turno Identificador do turno
     * @param ePratico Se o turno e pratico
     * @return O turno com o dado id
     */
    public Turno getTurno(int turno, boolean ePratico) {
        return this.turnos.get(new TurnoKey(this.id,turno,ePratico));
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
     * @param ePratico Se o turno e pratico
     * @return O numero da aula adicionada
     */
    int addAula(int turno, boolean ePratico) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno,ePratico));
        int aulaNum = tmpTurno.addAula();
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
        return aulaNum;
    }

    /**
     * Remove uma aula a um turno
     * @param turno Numero do turno onde remover
     * @param aula Numero da aula a remover
     * @param ePratico Se o turno e pratico
     */
    void removeAula(int turno, int aula, boolean ePratico) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno,ePratico));
        tmpTurno.removeAula(aula);
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
    }

    /**
     * Adiciona um docente ao turno
     * @param turno Numero do turno
     * @param docente Identificador do Docente
     * @param ePratico Se o turno e pratico
     */
    void addDocenteToTurno(int turno, String docente, boolean ePratico) {
        if(this.docentes.contains(docente)){
            this.docentes.add(docente);
        }
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno,ePratico));
        tmpTurno.setDocente(docente);
        this.turnos.put(new TurnoKey(tmpTurno),tmpTurno);
    }

    /**
     * Remove um docente do turno
     * @param turno Numero do turno
     * @param docente Identificador do Docente
     * @param ePratico Se o turno e pratico
     */
    void removeDocenteFromTurno(int turno, String docente, boolean ePratico) {
        Turno tmpTurno = this.turnos.get(new TurnoKey(this.id,turno,ePratico));
        tmpTurno.setDocente(null);
        this.turnos.put(new TurnoKey(this.id,tmpTurno.getId(),ePratico),tmpTurno);
        if(this.numTurnosLecionados(docente)==1){
            this.docentes.remove(docente);
        }
    }

    /**
     * Retorna o numero de turnos lecionados por um docente
     * @param docente Identificador do docente
     * @return O numero de turnos lecionados
     */
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
                (this.responsavel==null ?
                        a.getResponsavel()==null :
                        this.responsavel.equals(a.getResponsavel())) &&
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
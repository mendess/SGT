package main.sgt;

import main.dao.TurnoDAO;

import java.util.ArrayList;
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
	 */
	UC(String id, String nome) {
		this.id = id;
		this.nome = nome;
		this.responsavel = null;
		this.docentes = new ArrayList<>();
		this.alunos = new ArrayList<>();
		this.turnos = new TurnoDAO();
	}

	String getId() {
		return this.id;
	}

	/**
	 * Altera o identificador da UC
	 * @param id Novo identificador
	 */
	void setId(String id) {
		this.id = id;
	}

	String getNome() {
		return this.nome;
	}

	/**
	 * Altera o Nome da UC
	 * @param nome Novo nome
	 */
	void setNome(String nome) {
		this.nome = nome;
	}

    String getResponsavel() {
        return this.responsavel;
    }

    /**
     * Altera o responsavel pela UC
     * @param responsavel Numero do novo responsavel
     */
    void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    List<String> getDocentes() {
        return this.docentes;
    }

    /**
     * Adiciona um docente à UC
     * @param docente Numero do docente a adicionar
     * @throws UtilizadorJaExisteException Quando o docente já leciona a UC
     */
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
	List<Turno> getTurnos() {
		return new ArrayList<>(this.turnos.values());
	}

	/**
	 * Marca um aluno como presente numa aula
	 * @param aluno Aluno
	 * @param turno Turno a que a aula pertence
	 * @param aula Aula
	 */
	void marcarPresenca(String aluno, int turno, int aula) {
		this.turnos.get(turno).marcarPresenca(aluno,aula);
	}

	/**
	 * Remove um aluno de um turno
	 * @param aluno Aluno a remover
	 * @param turno Turno de onde remover
	 */
	void removerAlunoDeTurno(String aluno, int turno) {
		this.turnos.get(turno).removeAluno(aluno);
	}

	/**
	 * Adiciona um aluno a um turno
	 * @param aluno Aluno a adicionar
	 * @param turno Turno onde adicionar
	 */
	void adicionarAlunoTurno(String aluno, int turno) throws UtilizadorJaExisteException {
		this.turnos.get(turno).addAluno(aluno);
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
            this.turnos.put(OldShift1.getId(),OldShift1);
            this.turnos.put(OldShift2.getId(),OldShift2);
            return new Troca(aluno.getUserNum(), this.getId(),OldShift1.getId(),OldShift2.getId());
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
        this.turnos.put(this.turnos.maxID(),new Turno(id,ePratico,vagas,this.id));
        return id;
    }
    /**
	 * Remove um turno da UC
	 * @param id Identificador do turno a remover
	 */
	void removeTurno(int id) throws TurnoNaoVazioException {
	    if(this.turnos.get(id).getAlunos().isEmpty()){
	        this.turnos.remove(id);
        }else{
	        throw new TurnoNaoVazioException();
        }
	}

    /**
	 * Retorna um <tt>Turno</tt>
	 * @param turno Identificador do turno
     * @return O turno com o dado id
	 */
	Turno getTurno(int turno) {
	    return this.turnos.get(turno);
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
}
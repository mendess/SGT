package main.sgt;

import main.dao.PedidosDAO;
import main.dao.TrocaDAO;
import main.dao.UCDAO;
import main.dao.UserDAO;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SGT {

	/**
	 * Utilizador que está neste momento a utilizar a aplicação
	 */
	private Utilizador loggedUser;
	/**
	 * DAO de acesso aos pedidos de troca.
	 */
	private final PedidosDAO pedidosDAO;
	/**
	 * Pedidos de todos os utilizadores. Key: Aluno
	 */
	private final Map<String, List<Pedido>> pedidos;
	/**
	 * Registo das trocas efetuadas
	 */
	private final TrocaDAO trocas;
	/**
	 * Map de ucs no sistema
	 */
	private final UCDAO ucs;
	/**
	 * Map de utilizadores registados
	 */
	private final UserDAO utilizadores;

	/**
	 * Construtor do SGT
	 */
	public SGT() {
		this.pedidosDAO = new PedidosDAO();
		this.trocas = new TrocaDAO();
		this.ucs = new UCDAO();
		this.utilizadores = new UserDAO();
		Collection<List<Pedido>> e = this.pedidosDAO.values();
		Map<String,List<Pedido>> pedidos = new HashMap<>();
		e.forEach(ps -> {
			if(!ps.isEmpty()){
				pedidos.put(ps.get(0).getAlunoNum(),new ArrayList<>());
				ps.forEach(p -> pedidos.get(p.getAlunoNum()).add(p));
			}
		});
		this.pedidos = pedidos;
	}

	/**
	 * Autentica o utilizador
	 * @param userNum Número do utilizador
	 * @param password Password do utilizador
	 * @throws WrongCredentialsException Se o utilizador
	 */
	public void login(String userNum, String password) throws WrongCredentialsException {
		if(this.utilizadores.containsKey(userNum)){
			Utilizador user = this.utilizadores.get(userNum);
			if(user.getPassword().equals(password)){
				this.loggedUser=user;
			}else{
				throw new WrongCredentialsException("Wrong password");
			}
		}else{
			throw new WrongCredentialsException("No such user");
		}
	}

	/**
	 * Devolve os Turnos de uma UC
	 * @param uc UC
	 * @return Lista de turnos da UC
	 */
	public List<Turno> getTurnosOfUC(String uc) {
		return this.ucs.get(uc).getTurnos();
	}

    /**
	 * Devolve os turnos do utilizador que esta autenticado
	 * @return Lista de turnos do utilizador autenticado
	 * @throws InvalidUserTypeException Quando o utilizador autenticado não pode ter turnos
	 */
	public List<Turno> getTurnosUser() throws InvalidUserTypeException {
		if(this.loggedUser instanceof Aluno){
			Aluno aluno = (Aluno) this.loggedUser;
			return aluno.getHorario().entrySet()
					.stream()
					.map(e -> this.ucs.get(e.getKey()).getTurnos().get(e.getValue()))
					.collect(Collectors.toList());
		}
		if(this.loggedUser instanceof Docente){
			Docente docente = (Docente) this.loggedUser;
			List<Turno> turnos = new ArrayList<>();
			Set<Map.Entry<String,List<Integer>>> ucs = docente.getUcsEturnos().entrySet();
			for (Map.Entry<String,List<Integer>> uc : ucs){
				UC tmpUC = this.ucs.get(uc.getKey());
				List<Integer> tmpTurnos = uc.getValue();
				for(Integer turno : tmpTurnos){
					turnos.add(tmpUC.getTurno(turno));
				}
			}
			return turnos;
		}
		throw new InvalidUserTypeException();
	}

	/**
	 * Remove um aluno de um turno
	 * @param uc Identificador da UC a que o turno pertence
	 * @param aluno Numero do aluno a remover
	 * @param turno Numero do turno de onde remover
	 */
	public void removerAlunoDeTurno(String uc, String aluno, int turno) {
		UC newUC = this.ucs.get(uc);
		newUC.removerAlunoDeTurno(aluno,turno);
		this.ucs.put(newUC.getId(),newUC);
	}

    /**
     * Marca um aluno como presente
     * @param aluno Numero do aluno
     * @param uc Identificador da UC
     * @param turno Numero do turno
     * @param aula Aula
     */
    public void marcarPresenca(String aluno, String uc, int turno, int aula) {
        UC newUC = this.ucs.get(uc);
        newUC.marcarPresenca(aluno,turno,aula);
        this.ucs.put(newUC.getId(),newUC);
    }

	/**
	 * Adiciona um aluno a uma UC
	 * @param uc Identificador da UC a que pertence o turno
	 * @param aluno Numero do aluno a adicionar
	 * @param turno Numero do turno onde adicionar
     * @throws UtilizadorJaExisteException Se o aluno ja esta inscrito no turno
	 */
	public void adicionarAlunoTurno(String uc, String aluno, int turno) throws UtilizadorJaExisteException {
		this.ucs.get(uc).adicionarAlunoTurno(aluno,turno);
		this.trocas.add(new Troca(aluno,uc,-1,turno));
	}

	/**
	 * Verifica se o horario do utilizador autenticado conflite com o turno
	 * @param uc Identificador da UC a que pertence o turno
	 * @param turno Numero do turno
     * @return Retorna <tt>true</tt> se o horario conflite com o turno
	 */
	public boolean horarioConfilcts(String uc, int turno) throws InvalidUserTypeException {
		if(this.loggedUser instanceof Aluno){
			Turno novoT = this.ucs.get(uc).getTurno(turno);
			List<Turno> turnos = ((Aluno) this.loggedUser).getHorario().entrySet()
					.stream()
					.map(e -> this.ucs.get(e.getKey()).getTurno(e.getValue()))
					.collect(Collectors.toList());
			return turnos.stream()
					.anyMatch(t ->turnoConflicts(t,novoT));
		}else{
			throw new InvalidUserTypeException();
		}

	}

	/**
	 * Verifica se dois turnos estao em conflito
	 * @param t1 Turno 1
	 * @param t2 Turno 2
     * @return Retorna <tt>true</tt> se os turno conflitem
	 */
	private boolean turnoConflicts(Turno t1, Turno t2) {
		List<TurnoInfo> tinf1 = t1.getTurnoInfos();
		List<TurnoInfo> tinf2 = t2.getTurnoInfos();
		for(TurnoInfo tif1 : tinf1){
		    for(TurnoInfo tif2 : tinf2){
		        if(tif1.getDia() == tif2.getDia()){
		            if(tif1.getHoraInicio().isBefore(tif2.getHoraInicio())){
                        if (!tif2.getHoraFim().isBefore(tif2.getHoraInicio())){return false;}
                    }else{
                        if (!tif1.getHoraInicio().isAfter(tif2.getHoraFim())) {return false;}
                    }
                }
            }
        }
		return true;
	}

	/**
	 * Regista um pedido de troca do Aluno que esta autenticado
	 * @param uc Identificador da UC a que pertence o turno
	 * @param turno Numero do turno que pretende pedir
	 */
	public void pedirTroca(String uc, int turno) throws InvalidUserTypeException {
		if(this.loggedUser instanceof Aluno){
			Pedido newPedido = new Pedido(this.loggedUser.getUserNum(),this.loggedUser.getName(),uc,turno);
			if(this.pedidos.containsKey(this.loggedUser.getUserNum())){
				this.pedidos.get(this.loggedUser.getUserNum()).add(newPedido);
				this.pedidosDAO.put(this.loggedUser.getUserNum(),newPedido);
			}else{
				List<Pedido> newPedidos = new ArrayList<>();
				newPedidos.add(newPedido);
				this.pedidos.put(this.loggedUser.getUserNum(),newPedidos);
				this.pedidosDAO.put(this.loggedUser.getUserNum(),newPedidos);
			}
		}else{
			throw new InvalidUserTypeException();
		}
	}

    /**
     * Devolve uma lista de sujestoes de troca, ou seja, os pedidos que o aluno autenticado pode aceitar
     * @return Lista de pedidos que o aluno autenticado pode aceitar
     */
    public List<Pedido> getSujestoesTroca() {
		if(this.loggedUser instanceof Aluno){
			Map<String,Integer> inscricoes = ((Aluno) this.loggedUser).getHorario();
			return this.pedidos.entrySet().stream()
					.map(ps -> ps.getValue().stream()
							.filter(p -> inscricoes.containsKey(p.getUc())
									&& inscricoes.get(p.getUc()).equals(p.getTurno()))
							.findFirst()
							.orElse(null))
					.map(pedido -> new Pedido(pedido.getAlunoNum(),
                                                this.utilizadores.get(pedido.getAlunoNum()).getName(),
                                                pedido.getUc(),
												pedido.getTurno()))
					.collect(Collectors.toList());
		}
		return null;
	}

	/**
	 * Realiza a troca de um pedido
	 * @param pedido Pedido de troca
     * @throws InvalidUserTypeException Quando o utilizador autenticado nao e um Aluno
     * @throws AlunoNaoEstaInscritoNaUcException Um dos alunos nao esta inscrito na UC
	 */
	public void realizarTroca(Pedido pedido) throws InvalidUserTypeException, AlunoNaoEstaInscritoNaUcException {
		UC tmpUC = this.ucs.get(pedido.getUc());
		Utilizador u = this.utilizadores.get(pedido.getAlunoNum());
		if(this.loggedUser instanceof  Aluno && u instanceof Aluno){
			Troca[] trocas = tmpUC.trocarAlunos((Aluno) this.loggedUser, (Aluno) u);
			this.trocas.add(trocas[0]);
			this.trocas.add(trocas[1]);
		}else{
			throw new InvalidUserTypeException();
		}
	}

    /**
     * Move um aluno para outro turno de uma UC
     * @param aluno Numero do aluno
     * @param uc UC onde pertence o turno
     * @param turno Numero do turno para onde pretende ir
     * @throws InvalidUserTypeException O numero de aluno nao e valido
     * @throws AlunoNaoEstaInscritoNaUcException O aluno nao esta inscrito na UC
     */
    public void moveAlunoToTurno(String aluno, String uc, int turno) throws InvalidUserTypeException, AlunoNaoEstaInscritoNaUcException {
        Utilizador u = this.utilizadores.get(aluno);
        if(u instanceof Aluno){
            this.trocas.add(this.ucs.get(uc).moveAlunoToTurno((Aluno) u,turno));
        }else{
            throw new InvalidUserTypeException();
        }
    }

	/**
	 * Inscreve um aluno numa UC
	 * @param aluno Numero do Aluno a inscrever
	 * @param uc Numero da UC onde inscrever
     * @throws UtilizadorJaExisteException Quando o aluno ja esta na UC
	 */
	public void addAlunoToUC(String aluno, String uc) throws UtilizadorJaExisteException {
		UC newUC = this.ucs.get(uc);
		newUC.addAluno(aluno);
		this.ucs.put(newUC.getId(),newUC);
	}

    /**
     * Remove um aluno de uma UC
     * @param aluno Numero do aluno a remover
     * @param uc Numero da UC onde remover
     * @throws UtilizadorNaoExisteException Quando o aluno nao esta inscrito a UC
     */
    public void removeAlunoFromUC(String aluno, String uc) throws UtilizadorNaoExisteException {
        UC newUC = this.ucs.get(uc);
        newUC.removeAluno(aluno);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Devolve os docentes de uma UC
     * @param uc Numero da UC
     */
    public List<String> getDocentesOfUC(String uc) {
        return this.ucs.get(uc).getDocentes();
    }

    /**
     * Altera o coordenador de uma UC
     * @param uc Numero da UC
     * @param reponsavel Numero do responsavel
     */
    public void setResponsavelOfUC(String uc, String reponsavel) {
        UC newUC = this.ucs.get(uc);
        newUC.setResponsavel(reponsavel);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Adiciona um docente a uma UC
     * @param docente Numero do docente a adicionar
     * @param uc Numero da UC onde adicionar
     * @throws UtilizadorJaExisteException Quando o docente ja leciona esta UC
     */
    public void addDocenteToUC(String docente, String uc) throws UtilizadorJaExisteException {
        UC newUC = this.ucs.get(uc);
        newUC.addDocente(docente);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Adiciona um docente a uma UC
     * @param docente Numero do docente a adicionar
     * @param uc Numero da UC onde adicionar
     * @throws UtilizadorNaoExisteException Quando o docente nao esta a lecionar esta na UC
     */
    public void removeDocenteFromUC(String docente, String uc) throws UtilizadorNaoExisteException {
        UC newUC = this.ucs.get(uc);
        newUC.removeDocente(docente);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Devolve as todas as UCs
     * @return Lista das UCs
     */
	public List<UC> getUCs() {
		return new ArrayList<>(this.ucs.values());
	}

	/**
	 * Remove um turno de uma UC
	 * @param id Numero do turno a remover
	 * @param uc Numero da UC onde remover
     * @throws TurnoNaoVazioException Quando o turno tem alunos associados
	 */
	public void removeTurno(int id, String uc) throws TurnoNaoVazioException {
		UC newUC = this.ucs.get(uc);
		newUC.removeTurno(id);
		this.ucs.put(newUC.getId(),newUC);
	}

	/**
	 * Adiciona um turno a uma UC
	 * @param ePratico Se o turno e pratico
	 * @param vagas O numero de vagas do turno
	 * @param uc A UC do turno
	 */
	public int addTurno(boolean ePratico, int vagas, String uc) {
		UC newUC = this.ucs.get(uc);
	    int newID = newUC.addTurno(ePratico,vagas);
	    this.ucs.put(newUC.getId(),newUC);
	    return newID;
	}

	/**
	 * Importa os turnos de um ficheiro
	 * @param filepath Caminho para o ficheiro
	 */
	public void importTurnos(String filepath) {
		/*File file = new File(filePath);
        JsonReader jsonReader;
        try {
            jsonReader = Json.createReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Set<String> keySet = jsonReader.readObject().keySet();
        */
        throw new UnsupportedOperationException();
    }

	/**
     * Importa os alunos de um ficheiro
     * @param filepath Caminho para o ficheiro
	 */
	public void importAlunos(String filepath) {
		// TODO - implement SGT.importAlunos
		throw new UnsupportedOperationException();
	}

	/**
     * Importa as UCs de um ficheiro
     * @param filepath Caminho para o ficheiro
	 */
	public void importUCs(String filepath) {
		// TODO - implement SGT.importUCs
		throw new UnsupportedOperationException();
	}

    /**
     * Atribui os turnos aos alunos
     */
	public void assignShifts() {
		// TODO - implement SGT.assignShifts
		throw new UnsupportedOperationException();
	}

    /**
     * Ativa os logins para os alunos
     */
	public void activateLogins() {
		this.utilizadores.values().forEach(utilizador -> {
			if(utilizador instanceof Aluno){
				((Aluno) utilizador).ativarLogin();
			}
		});
	}

    /**
     * Devolve todas as trocas efetuadas
     * @return Lista de trocas efetuadas
     */
	public List<Troca> getTrocas() {
		return this.trocas;
	}

	/**
	 * Adiciona uma aula a um turno
	 * @param uc Identificador da UC to turno
	 * @param turno Numero do turno
	 */
	public void addAula(String uc, int turno) {
	    this.ucs.get(uc).addAula(turno);
	}

	/**
	 * Remove uma aula de um turno
	 * @param uc Identificador da UC do turno
	 * @param turno Numero do turno
	 * @param aula Numero da aula a remover
	 */
	public void removeAula(String uc, int turno, int aula) {
	    this.ucs.get(uc).removeAula(turno,aula);
	}
}
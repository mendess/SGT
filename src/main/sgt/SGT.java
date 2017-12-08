package main.sgt;

import main.dao.PedidosDAO;
import main.dao.TrocaDAO;
import main.dao.UCDAO;
import main.dao.UserDAO;

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
	 * 
	 * @param userNum
	 * @param password
	 */
	public void login(String userNum, String password) throws WrongCredentialsException {
		if(utilizadores.containsKey(userNum)){
			Utilizador user = utilizadores.get(userNum);
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
	 * 
	 * @param uc
	 */
	public List<Turno> getTurnosOfUC(String uc) {
		return ucs.get(uc).getTurnos();
	}

	public List<Turno> getTurnosUser() throws InvalidUserTypeException {
		if(loggedUser instanceof Aluno){
			Aluno aluno = (Aluno) loggedUser;
			return aluno.getInscricoes().entrySet()
					.stream()
					.map(e -> ucs.get(e.getKey()).getTurnos().get(e.getValue()))
					.collect(Collectors.toList());
		}
		if(loggedUser instanceof Docente){
			Docente docente = (Docente) loggedUser;
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
	 * 
	 * @param aluno
	 * @param uc
	 * @param turno
	 * @param aula
	 */
	public void marcarPresenca(String aluno, String uc, int turno, int aula) {
		ucs.get(uc).marcarPresenca(aluno,turno,aula);
	}

	/**
	 * 
	 * @param uc
	 * @param aluno
	 * @param turno
	 */
	public void removerAlunoDeTurno(String uc, String aluno, int turno) {
		ucs.get(uc).removerAlunoDeTurno(aluno,turno);
	}

	/**
	 * 
	 * @param uc
	 * @param aluno
	 * @param turno
	 */
	public void adicionarAlunoTurno(String uc, String aluno, int turno) {
		ucs.get(uc).adicionarAlunoTurno(aluno,turno);
	}

	/**
	 * 
	 * @param uc
	 * @param turno
	 */
	public void pedirTroca(String uc, int turno) throws InvalidUserTypeException {
		if(this.loggedUser instanceof Aluno){
			Pedido newPedido = new Pedido(this.loggedUser.getUserNum(),uc,turno);
			if(pedidos.containsKey(this.loggedUser.getUserNum())){
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

	public List<Sugestao> getSujestoesTroca() {
		if(this.loggedUser instanceof Aluno){
			Map<String,Integer> inscricoes = ((Aluno) this.loggedUser).getInscricoes();
			return this.pedidos.entrySet().stream()
					.map(ps -> ps.getValue().stream()
							.filter(p -> inscricoes.containsKey(p.getUc()) && inscricoes.get(p.getUc()).equals(p.getTurno()))
							.findFirst()
							.orElse(null))
					.map(pedido -> new Sugestao(pedido.getUc(),pedido.getTurno(),pedido.getAlunoNum(),pedido.getAlunoNum()))
					.collect(Collectors.toList());
		}
		return null;
	}

	/**
	 * 
	 * @param aluno
	 * @param uc
	 */
	public void realizarTroca(String aluno, String uc) {
		// TODO - implement SGT.realizarTroca
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aluno
	 * @param uc
	 */
	public void addAlunoToUC(String aluno, String uc) {
		// TODO - implement SGT.addAlunoToUC
		throw new UnsupportedOperationException();
	}

	public List<UC> getUCs() {
		// TODO - implement SGT.getUCs
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param uc
	 */
	public void removeTurno(int id, String uc) {
		// TODO - implement SGT.removeTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param ePratico
	 * @param uc
	 */
	public void addTurno(int id, boolean ePratico, String uc) {
		// TODO - implement SGT.addTurno
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param filepath
	 */
	public void importTurnos(String filepath) {
		// TODO - implement SGT.importTurnos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param filepath
	 */
	public void importAlunos(String filepath) {
		// TODO - implement SGT.importAlunos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param filepath
	 */
	public void importUCs(String filepath) {
		// TODO - implement SGT.importUCs
		throw new UnsupportedOperationException();
	}

	public void assignShifts() {
		// TODO - implement SGT.assignShifts
		throw new UnsupportedOperationException();
	}

	public void activateLogins() {
		// TODO - implement SGT.activateLogins
		throw new UnsupportedOperationException();
	}

	public List<Troca> getTrocas() {
		// TODO - implement SGT.getTrocas
		throw new UnsupportedOperationException();
	}

}
package main.sgt;

import main.dao.*;
import main.sgt.exceptions.*;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


import static main.sgt.NotifyFlags.*;

@SuppressWarnings("unused")
public class SGT extends Observable {

    /**
     * DAO de acesso aos pedidos de troca.
     */
    private final PedidosDAO pedidosDAO;
    /**
     * Pedidos de todos os utilizadores. Key: Aluno
     */
    private final Map<String,List<Pedido>> pedidos;
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
     * Utilizador que está neste momento a utilizar a aplicação
     */
    private Utilizador loggedUser;
    /**
     * Estado das UCs
     */
    private boolean ucsRegistadas;
    /**
     * Estado dos utilizadores
     */
    private boolean usersRegistados;
    /**
     * Estado dos turnos
     */
    private boolean turnosRegistados;
    /**
     * Estado dos logins
     */
    private boolean loginsAtivos;
    /**
     * Estado da atribuicao do turnos
     */
    private boolean turnosAtribuidos;

    /**
     * Se as trocas sao permitidas
     */
    private boolean trocasPermitidas;

    /**
     * Construtor do SGT
     */
    public SGT(){
        this.pedidosDAO = new PedidosDAO();
        this.trocas = new TrocaDAO();
        this.ucs = new UCDAO();
        this.utilizadores = new UserDAO();

        long startTime = System.nanoTime();
        long totalTime = startTime;
        System.out.println("A carregar dias");
        new DiaDAO().initDias();
        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");

        startTime = System.nanoTime();
        System.out.println("A carregar pedidos");
        Collection<List<Pedido>> pedidosDB = this.pedidosDAO.values();
        Map<String,List<Pedido>> pedidosMEM = new HashMap<>();
        pedidosDB.stream()
                .filter(pedidoListDB->!pedidoListDB.isEmpty())
                .forEach(pedidoListDB->pedidoListDB.forEach(p->{
                            if(!pedidosMEM.containsKey(p.getAlunoNum()))
                                pedidosMEM.put(p.getAlunoNum(), new ArrayList<>());
                            pedidosMEM.get(p.getAlunoNum()).add(p);
                        })
                );
        this.pedidos = pedidosMEM;
        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");

        startTime = System.nanoTime();
        System.out.println("A carregar utilizadores");
        Collection<Utilizador> utilizadores = this.utilizadores.values();
        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");


        startTime = System.nanoTime();
        System.out.println("A carregar ucs");
        Collection<UC> ucsValues = this.ucs.values();
        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");


        startTime = System.nanoTime();
        System.out.println("A verificar se as UCS estão registadas");
        this.setUcsRegistadas(!ucsValues.isEmpty());
        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");

        if(this.ucsRegistadas){
            startTime = System.nanoTime();
            System.out.println("A verificar se os users estão registados");
            this.setUsersRegistados(!utilizadores.isEmpty());
            System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");
            if(this.usersRegistados){
                startTime = System.nanoTime();
                System.out.println("A verificar se os turnos estão registados");
                this.setTurnosRegistados(ucsValues.stream().noneMatch(uc->uc.getTurnos().isEmpty()));
                System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");
                if(this.turnosRegistados){
                    startTime = System.nanoTime();
                    System.out.println("A verificar se os logins estão ativos");
                    this.setLoginsAtivos(utilizadores.stream().allMatch(Utilizador::isLoginAtivo));
                    System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");
                    if(this.loginsAtivos){
                        startTime = System.nanoTime();
                        System.out.println("A verifcar se os turnos estão atribuidos");
                        boolean b = true;
                        for(Iterator<Utilizador> iterator = utilizadores.iterator(); b && iterator.hasNext(); ){
                            Utilizador u = iterator.next();
                            if(u instanceof Aluno){
                                if(((Aluno) u).getHorario().isEmpty()){
                                    b = false;
                                }
                            }
                        }
                        this.setTurnosAtribuidos(b);
                        System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");
                        if(this.turnosAtribuidos){
                            startTime = System.nanoTime();
                            System.out.println("A verifcar se as trocas são permitidas");
                            this.setTrocasPermitidas(this.utilizadores.isTrocasPermitidas());
                            System.out.println((System.nanoTime() - startTime) / 1000000 + " milisegundos");
                        }
                    }
                }
            }
        }
        System.out.println("100%");
        System.out.println("Tempo total: " + (System.nanoTime() - totalTime) / 1000000);
    }

    /**
     * Retorna o estado das ucs
     *
     * @return O estado das ucs
     */
    public boolean isUcsRegistadas(){
        return this.ucsRegistadas;
    }

    /**
     * Muda o estado do sistema comecando pelas UCs
     *
     * @param ucsRegistadas Novo estado
     */
    private void setUcsRegistadas(boolean ucsRegistadas){
        if(ucsRegistadas){
            this.setChanged();
            this.notifyObservers(UCS_IMPORTADAS);
        }
        this.ucsRegistadas = ucsRegistadas;
        this.setUsersRegistados(false);
    }

    /**
     * Retorna o estado dos utilizadores
     *
     * @return O estado dos utilizadores
     */
    public boolean isUsersRegistados(){
        return this.usersRegistados;
    }

    /**
     * Muda o estado do sistema comecando pelos utilizadores
     *
     * @param usersRegistados Novo estado
     */
    private void setUsersRegistados(boolean usersRegistados){
        if(usersRegistados){
            this.setChanged();
            this.notifyObservers(UTILIZADORES_IMPORTADOS);
        }
        this.usersRegistados = usersRegistados;
        this.setTurnosRegistados(false);
    }

    /**
     * Retorna o estado dos turnos
     *
     * @return O estado dos turnos
     */
    public boolean isTurnosRegistados(){
        return this.turnosRegistados;
    }

    /**
     * Muda o estado do sistema comecando pelos turnos
     *
     * @param turnosRegistados Novo estado
     */
    private void setTurnosRegistados(boolean turnosRegistados){
        if(turnosRegistados){
            this.setChanged();
            this.notifyObservers(TURNOS_IMPORTADOS);
        }
        this.turnosRegistados = turnosRegistados;
        this.setLoginsAtivos(false);
    }

    /**
     * Retorna o estado dos logins
     *
     * @return O estado dos logins
     */
    public boolean isLoginsAtivos(){
        return this.loginsAtivos;
    }

    /**
     * Muda o estado do sistema comecando pelos logins
     *
     * @param loginsAtivos Novo estado
     */
    private void setLoginsAtivos(boolean loginsAtivos){
        if(loginsAtivos){
            this.setChanged();
            this.notifyObservers(LOGINS_ATIVADOS);
        }
        this.loginsAtivos = loginsAtivos;
        this.setTurnosAtribuidos(false);
    }

    /**
     * Retorna se os turnos foram atribuidos
     *
     * @return <tt>True</tt> se os turnos estao atribuidos
     */
    public boolean isTurnosAtribuidos(){
        return this.turnosAtribuidos;
    }

    /**
     * Muda o estado do sistema comecando pela atribuicao dos turnos
     *
     * @param turnosAtribuidos Novo estado
     */
    private void setTurnosAtribuidos(boolean turnosAtribuidos){
        if(turnosAtribuidos){
            this.setChanged();
            this.notifyObservers(TURNOS_ATRIBUIDOS);
        }
        this.turnosAtribuidos = turnosAtribuidos;
    }

    /**
     * Retorna se as trocas de turno entre alunos sao permitidas
     * @return Se as trocas de turno entre alunos sao permitidas
     */
    public boolean isTrocasPermitidas(){
        return this.turnosAtribuidos && this.trocasPermitidas;
    }

    /**
     * Altera se as trocas de turno entre alunos sao permitidas
     * @param trocasPermitidas Novo estado
     */
    public void setTrocasPermitidas(boolean trocasPermitidas){
        if(!trocasPermitidas){
            this.setChanged();
            this.notifyObservers(TROCAS_PROIBIDAS);
        }
        this.trocasPermitidas = this.utilizadores.setTrocasPermitidas(trocasPermitidas);
    }

    /**
     * Autentica o utilizador
     *
     * @param userNum  Número do utilizador
     * @param password Password do utilizador
     * @throws WrongCredentialsException Se o utilizador
     */
    public void login(String userNum, String password) throws WrongCredentialsException{
        if(this.utilizadores.containsKey(userNum)){
            Utilizador user = this.utilizadores.get(userNum);
            if(user.getPassword().equals(password)){
                this.loggedUser = user;
            }else{
                throw new WrongCredentialsException("Wrong password");
            }
        }else{
            throw new WrongCredentialsException("No such user");
        }
    }

    /**
     * Desautentica o utilizador
     */
    public void logout(){
        this.loggedUser = null;
    }

    /**
     * Devolve o utilizador autenticado
     *
     * @return O utilizador autenticado
     */
    public Utilizador getLoggedUser(){
        return this.loggedUser = this.utilizadores.get(this.loggedUser.getUserNum());
    }

    /**
     * Devolve os Turnos de uma UC
     *
     * @param uc UC
     * @return Lista de turnos da UC
     */
    public List<Turno> getTurnosOfUC(String uc){
        List<Turno> turnos = this.ucs.get(uc).getTurnos();
        turnos = turnos.stream().filter(t->t.getId() > 0).collect(Collectors.toList());
        turnos.sort(new ComparatorTurnos());
        return turnos;
    }

    /**
     * Devolve os turnos do utilizador que esta autenticado
     *
     * @return Lista de turnos do utilizador autenticado
     * @throws InvalidUserTypeException Quando o utilizador autenticado não pode ter turnos
     */
    public List<Turno> getTurnosUser() throws InvalidUserTypeException{
        if(this.loggedUser instanceof Aluno){
            Aluno aluno = (Aluno) this.loggedUser;
            return aluno.getHorario().entrySet()
                    .stream()
                    .map(e->this.ucs.get(e.getKey()).getTurnos().get(e.getValue()))
                    .sorted(new ComparatorTurnos())
                    .collect(Collectors.toList());
        }
        if(this.loggedUser instanceof Docente){
            Docente docente = (Docente) this.loggedUser;
            List<Turno> turnos = new ArrayList<>();
            Set<Map.Entry<String,List<TurnoKey>>> ucs = docente.getUcsEturnos().entrySet();
            for(Map.Entry<String,List<TurnoKey>> uc : ucs){
                UC tmpUC = this.ucs.get(uc.getKey());
                List<TurnoKey> tmpTurnos = uc.getValue();
                for(TurnoKey turno : tmpTurnos){
                    turnos.add(tmpUC.getTurno(turno.getTurno_id(), turno.ePratico()));
                }
            }
            turnos.sort(new ComparatorTurnos());
            return turnos;
        }
        throw new InvalidUserTypeException();
    }

    /**
     * Devolve a UC com o dado identificador
     *
     * @param uc O Identificador da UC
     * @return Uma instancia da UC
     */
    public UC getUC(String uc){
        return this.ucs.get(uc);
    }

    /**
     * Devolve uma lista de ucs do utilizador autenticador
     *
     * @return Uma lista de ucs do utilizador autenticador
     * @throws InvalidUserTypeException O utilizador autenticado nao pode ter turnos
     */
    public List<UC> getUCsOfUser() throws InvalidUserTypeException{
        if(this.loggedUser instanceof Aluno){
            Aluno aluno = (Aluno) this.loggedUser;
            return aluno.getHorario().keySet()
                    .stream()
                    .map(this.ucs::get)
                    .sorted((uc1,uc2) -> String.CASE_INSENSITIVE_ORDER.compare(uc1.getId(),uc2.getId()))
                    .collect(Collectors.toList());
        }
        if(this.loggedUser instanceof Docente){
            Docente docente = (Docente) this.loggedUser;
            return docente.getUcsEturnos().keySet()
                    .stream()
                    .map(this.ucs::get)
                    .sorted((uc1,uc2) -> String.CASE_INSENSITIVE_ORDER.compare(uc1.getId(),uc2.getId()))
                    .collect(Collectors.toList());
        }
        throw new InvalidUserTypeException();
    }

    /**
     * Devolve as todas as UCs
     *
     * @return Lista das UCs
     */
    public List<UC> getUCs(){
        List<UC> ucs = new ArrayList<>(this.ucs.values());
        ucs.sort((uc1,uc2) -> String.CASE_INSENSITIVE_ORDER.compare(uc1.getId(),uc2.getId()));
        return ucs;
    }

    /**
     * Devolve um utilizador
     *
     * @param user Identfificador do utilizador
     * @return Uma instancia do aluno
     */
    public Utilizador getUser(String user){
        return this.utilizadores.get(user);
    }

    /**
     * Devolve os docentes de uma UC
     *
     * @param uc Numero da UC
     */
    public List<String> getDocentesOfUC(String uc){
        List<String> doc = this.ucs.get(uc).getDocentes();
        doc.sort(String.CASE_INSENSITIVE_ORDER);
        return doc;
    }

    /**
     * Devolve todas as trocas efetuadas
     *
     * @return Lista de trocas efetuadas
     */
    public List<Troca> getTrocas(){
        return new ArrayList<>(this.trocas);
    }

    /**
     * Inscreve um aluno numa UC
     *
     * @param aluno Numero do Aluno a inscrever
     * @param uc    Numero da UC onde inscrever
     * @throws UtilizadorJaExisteException Quando o aluno ja esta na UC
     */
    public void addAlunoToUC(String aluno, String uc) throws UtilizadorJaExisteException, InvalidUserTypeException{
        Utilizador u = this.utilizadores.get(aluno);
        if(! (u instanceof Aluno)) throw new InvalidUserTypeException();
        ((Aluno) u).inscrever(uc,0);
        this.utilizadores.put(u.getUserNum(),u);
        this.setChanged();
        this.notifyObservers(ALUNO_ADDED_TO_UC);
    }

    /**
     * Remove um aluno de uma UC
     *
     * @param aluno Numero do aluno a remover
     * @param uc    Numero da UC onde remover
     * @throws UtilizadorNaoExisteException Quando o aluno nao esta inscrito a UC
     */
    public void removeAlunoFromUC(String aluno, String uc) throws UtilizadorNaoExisteException,
                                                                  InvalidUserTypeException{
        Utilizador u = this.utilizadores.get(aluno);
        if(u == null) throw new UtilizadorNaoExisteException();
        if(! (u instanceof Aluno)) throw new InvalidUserTypeException();
        ((Aluno) u).desinscrever(uc,((Aluno) u).getHorario().get(uc));
        this.utilizadores.put(u.getUserNum(),u);
        this.setChanged();
        this.notifyObservers(ALUNO_REMOVED_FROM_UC);
    }

    /**
     * Altera o coordenador de uma UC
     *
     * @param uc         Numero da UC
     * @param reponsavel Numero do responsavel
     */
    public void setResponsavelOfUC(String uc, String reponsavel){
        UC newUC = this.ucs.get(uc);
        newUC.setResponsavel(reponsavel);
        this.ucs.put(newUC.getId(), newUC);
    }

    /**
     * Move um aluno para outro turno de uma UC
     *
     * @param aluno Numero do aluno
     * @param uc    UC onde pertence o turno
     * @param turno Numero do turno para onde pretende ir
     * @throws InvalidUserTypeException          O numero de aluno nao e valido
     * @throws AlunoNaoEstaInscritoNaUcException O aluno nao esta inscrito na UC
     * @throws UtilizadorJaExisteException       Se o utilizador ja esta no turno
     * @throws TurnoCheioException               Se o turno esta cheio
     */
    public void moveAlunoToTurno(String aluno, String uc, int turno) throws InvalidUserTypeException,
                                                                            AlunoNaoEstaInscritoNaUcException,
                                                                            UtilizadorJaExisteException,
                                                                            TurnoCheioException{
        Utilizador u = this.utilizadores.get(aluno);
        if(u instanceof Aluno){
            this.trocas.add(this.ucs.get(uc).moveAlunoToTurno((Aluno) u, turno));
        }else{
            throw new InvalidUserTypeException();
        }
    }

    /**
     * Adiciona um aluno a uma UC
     *
     * @param aluno Numero do aluno a adicionar
     * @param uc    Identificador da UC a que pertence o turno
     * @param turno Numero do turno onde adicionar
     * @throws UtilizadorJaExisteException       Se o aluno ja esta inscrito no turno
     * @throws AlunoNaoEstaInscritoNaUcException Se o aluno nao esta inscrito na UC
     */
    private void addAlunoTurno(String aluno, String uc, int turno) throws UtilizadorJaExisteException,
                                                                          AlunoNaoEstaInscritoNaUcException,
                                                                          TurnoCheioException{
        this.ucs.get(uc).moveAlunoToTurno((Aluno) this.utilizadores.get(aluno), turno);
    }

    /**
     * Remove um aluno de um turno
     *
     * @param uc    Identificador da UC a que o turno pertence
     * @param aluno Numero do aluno a remover
     * @param turno Numero do turno de onde remover
     * @throws AlunoNaoEstaInscritoNaUcException Se o aluno nao estiver inscrito
     * @throws UtilizadorJaExisteException       Se o utilizador ja esta inscrito
     */
    public void removeAlunoFromTurno(String uc, String aluno, int turno) throws AlunoNaoEstaInscritoNaUcException,
                                                                                UtilizadorJaExisteException{
        this.trocas.add(this.ucs.get(uc).removerAlunoDeTurno((Aluno) this.utilizadores.get(aluno), turno));
    }

    /**
     * Adiciona um docente a um turno
     *
     * @param uc       O identificador da UC do turno
     * @param turno    O numero do turno
     * @param docente  O identificador do docente
     * @param ePratico Se o turno e pratico
     **/
    public void setDocenteOfTurno(String uc, int turno, String docente, boolean ePratico){
        UC tmpUC = this.ucs.get(uc);
        tmpUC.addDocenteToTurno(turno, docente, ePratico);
        this.ucs.put(tmpUC.getId(), tmpUC);
    }

    /**
     * Remove um docente de uma UC
     *
     * @param uc       O identificador da UC do turno
     * @param turno    O numero do turno
     * @param docente  O identificador do docente
     * @param ePratico Se o turno e pratico
     */
    public void removeDocenteFromTurno(String uc, int turno, String docente, boolean ePratico){
        UC tmpUC = this.ucs.get(uc);
        tmpUC.removeDocenteFromTurno(turno, docente, ePratico);
        this.ucs.put(tmpUC.getId(), tmpUC);
    }

    /**
     * Adiciona um turno a uma UC
     *
     * @param ePratico Se o turno e pratico
     * @param vagas    O numero de vagas do turno
     * @param uc       A UC do turno
     */
    public int addTurno(boolean ePratico, int vagas, String uc){
        UC newUC = this.ucs.get(uc);
        int newID = newUC.addTurno(ePratico, vagas);
        this.ucs.put(newUC.getId(), newUC);
        return newID;
    }

    /**
     * Remove um turno de uma UC
     *
     * @param id       Numero do turno a remover
     * @param uc       Numero da UC onde remover
     * @param ePratico Se o turno e pratico
     * @throws TurnoNaoVazioException Quando o turno tem alunos associados
     */
    public void removeTurno(int id, String uc, boolean ePratico) throws TurnoNaoVazioException{
        UC newUC = this.ucs.get(uc);
        newUC.removeTurno(id, ePratico);
        this.ucs.put(newUC.getId(), newUC);
    }

    /**
     * Marca um aluno como presente
     *
     * @param aluno    Numero do aluno
     * @param uc       Identificador da UC
     * @param turno    Numero do turno
     * @param aula     Aula
     * @param ePratico Se o turno e pratico
     **/
    public void marcarPresenca(String aluno, String uc, int turno, int aula, boolean ePratico){
        UC newUC = this.ucs.get(uc);
        newUC.marcarPresenca(aluno, turno, aula, ePratico);
        this.ucs.put(newUC.getId(), newUC);
    }

    /**
     * Regista um pedido de troca do Aluno que esta autenticado
     *
     * @param uc    Identificador da UC a que pertence o turno
     * @param turno Numero do turno que pretende pedir
     * @throws InvalidUserTypeException Se o utilizador que esta autenticado nao pode realizar esta operacao
     */
    public void pedirTroca(String uc, int turno) throws InvalidUserTypeException{
        if(this.loggedUser instanceof Aluno){
            Pedido newPedido = new Pedido(this.loggedUser.getUserNum(), 
                                          this.loggedUser.getName(),
                                          uc, turno, true);
            if(this.pedidos.containsKey(this.loggedUser.getUserNum())){
                this.pedidos.get(this.loggedUser.getUserNum()).add(newPedido);
            }else{
                List<Pedido> newPedidos = new ArrayList<>();
                newPedidos.add(newPedido);
                this.pedidos.put(this.loggedUser.getUserNum(), newPedidos);
            }
            this.pedidosDAO.put(newPedido);
        }else{
            throw new InvalidUserTypeException();
        }
    }

    /**
     * Verifica se o horario de um aluno conflite com o turno
     *
     * @param aluno    Instancia do aluno
     * @param uc       Identificador da UC a que pertence o turno
     * @param turno    Numero do turno
     * @param ePratico Se o turno e pratico
     * @return Retorna <tt>true</tt> se o horario conflite com o turno
     */
    public boolean horarioConfilcts(Aluno aluno, String uc, int turno, boolean ePratico){
        Turno novoT = this.ucs.get(uc).getTurno(turno, ePratico);
        List<Turno> turnos = aluno.getHorario().entrySet()
                .stream()
                .map(e->this.ucs.get(e.getKey())
                                .getTurno(e.getValue(), ePratico))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return turnos.stream()
                .anyMatch(t->turnoConflicts(t, novoT));
    }

    /**
     * Verifica se dois turnos estao em conflito
     *
     * @param t1 Turno 1
     * @param t2 Turno 2
     * @return Retorna <tt>true</tt> se os turno conflitem
     */
    private boolean turnoConflicts(Turno t1, Turno t2){
        List<TurnoInfo> tinf1 = t1.getTurnoInfos();
        List<TurnoInfo> tinf2 = t2.getTurnoInfos();
        for(TurnoInfo tif1 : tinf1){
            for(TurnoInfo tif2 : tinf2){
                if(tif1.getDia() == tif2.getDia()){
                    if(tif1.getHoraInicio().isBefore(tif2.getHoraInicio())){
                        if(tif2.getHoraInicio().isBefore(tif1.getHoraFim())){
                            return true;
                        }
                    }else{
                        if(tif1.getHoraInicio().isBefore(tif2.getHoraFim())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determina a lista de alunos com excesso de faltas a uma UC
     * @param alunos O identificador do aluno
     * @param uc O identificador da UC
     * @return <tt>True</tt> se o aluno tiver excesso de faltas
     */
    public List<String> alComExcessoDeFaltas(List<String> alunos, String uc,int turno,boolean ePratico){
        return this.ucs.get(uc).alComExcessoDeFaltas(alunos,turno,ePratico);
    }

    /**
     * Devolve uma lista de sujestoes de troca, ou seja, os pedidos que o aluno autenticado pode aceitar
     *
     * @return Lista de pedidos que o aluno autenticado pode aceitar
     */
    public List<Pedido> getSujestoesTroca(){
        if(this.loggedUser instanceof Aluno){
            Map<String,Integer> inscricoes = ((Aluno) this.loggedUser).getHorario();
            return this.pedidos.entrySet().stream()
                    .map(ps->ps.getValue().stream()
                            .filter(p->inscricoes.containsKey(p.getUc())
                                    && inscricoes.get(p.getUc()).equals(p.getTurno()))
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .map(pedido->new Pedido(pedido.getAlunoNum(),
                            this.utilizadores.get(pedido.getAlunoNum()).getName(),
                            pedido.getUc(),
                            pedido.getTurno(),
                            pedido.ePratico()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Realiza a troca de um pedido
     *
     * @param pedido Pedido de troca
     * @throws InvalidUserTypeException          Quando o utilizador autenticado nao e um Aluno
     * @throws AlunoNaoEstaInscritoNaUcException Um dos alunos nao esta inscrito na UC
     * @throws UtilizadorJaExisteException       Se algum dos utilizadores esta inscrito nos turnos para onde vao
     */
    public void realizarTroca(Pedido pedido) throws InvalidUserTypeException,
                                                    AlunoNaoEstaInscritoNaUcException,
                                                    UtilizadorJaExisteException{
        UC tmpUC = this.ucs.get(pedido.getUc());
        Utilizador u = this.utilizadores.get(pedido.getAlunoNum());
        try{
            if(this.loggedUser instanceof Aluno && u instanceof Aluno){
                Troca[] trocas = tmpUC.trocarAlunos((Aluno) this.loggedUser, (Aluno) u);
                this.trocas.add(trocas[0]);
                this.trocas.add(trocas[1]);
            }else{
                throw new InvalidUserTypeException();
            }
        }catch(TurnoCheioException ignored){
        }
        this.pedidosDAO.remove(pedido);
        this.pedidos.get(pedido.getAlunoNum()).remove(pedido);
        this.setChanged();
        this.notifyObservers(TROCA_REALIZADA);
    }

    /**
     * Adiciona uma aula a um turno
     *
     * @param uc       Identificador da UC to turno
     * @param turno    Numero do turno
     * @param ePratico Se o turno e pratico
     * @return O numero da nova aula
     **/
    public int addAula(String uc, int turno, boolean ePratico){
        return this.ucs.get(uc).addAula(turno, ePratico);
    }

    /**
     * Remove uma aula de um turno
     *
     * @param uc       Identificador da UC do turno
     * @param turno    Numero do turno
     * @param aula     Numero da aula a remover
     * @param ePratico Se o turno e pratico
     */
    public void removeAula(String uc, int turno, int aula, boolean ePratico){
        this.ucs.get(uc).removeAula(turno, aula, ePratico);
    }

    /**
     * Importa as UCs de um ficheiro
     *
     * @param filepath Caminho para o ficheiro
     * @throws FileNotFoundException      Se o ficheiro nao existe
     * @throws BadlyFormatedFileException Se o ficheiro tem a sintaxe errada
     */
    public void importUCs(String filepath) throws FileNotFoundException, BadlyFormatedFileException{
        try{
            JsonReader jreader = Json.createReader(new FileReader(new File(filepath)));
            JsonArray jarray = jreader.readArray();
            this.ucs.clear();
            this.utilizadores.clear();
            for(JsonValue j : jarray){
                JsonObject jobj = (JsonObject) j;
                String id = jobj.getString("id");
                String name = jobj.getString("name");
                String acron = jobj.getString("acron");
                this.ucs.put(id, new UC(id, name, acron));
            }
            this.setUcsRegistadas(true);
        }catch(NullPointerException | JsonParsingException e){
            throw new BadlyFormatedFileException();
        }
    }

    /**
     * Importa os utilizadores de um ficheiro
     *
     * @param filepath Caminho para o ficheiro
     * @throws FileNotFoundException      Se o ficheiro nao existe
     * @throws BadlyFormatedFileException Se o ficheiro tem a sintaxe errada
     */
    public void importUtilizadores(String filepath) throws FileNotFoundException, BadlyFormatedFileException{
        try{
            JsonReader jreader = Json.createReader(new FileReader(new File(filepath)));
            JsonArray jarray = jreader.readArray();
            this.utilizadores.clear();
            for(JsonValue j : jarray){
                JsonObject jobj = (JsonObject) j;
                String num = jobj.getString("Num");
                String nome = jobj.getString("Nome");
                String email = jobj.getString("Email");
                String password = jobj.getString("password");
                Utilizador user = null;
                switch(jobj.getString("Type")){
                    case "A":{
                        boolean eEspecial = jobj.getBoolean("eEspecial");
                        user = new Aluno(num, password, email, nome, eEspecial, new HashMap<>());
                        break;
                    }
                    case "D":{
                        user = new Docente(num, password, email, nome, new HashMap<>());
                        break;
                    }
                    case "C":{
                        String ucRegida = jobj.getString("ucRegida");
                        user = new Coordenador(num, password, email, nome, new HashMap<>(), ucRegida);
                        break;
                    }
                    case "K":{
                        user = new DiretorDeCurso(num, password, email, nome);
                        break;
                    }
                }
                if(user == null)
                    throw new BadlyFormatedFileException();

                this.utilizadores.put(num, user);
            }
        }catch(NullPointerException | JsonParsingException e){
            throw new BadlyFormatedFileException();
        }
        this.setUsersRegistados(true);
    }

    /**
     * Importa os turnos de um ficheiro
     *
     * @param filepath Caminho para o ficheiro
     * @throws FileNotFoundException      Se o ficheiro nao existe
     * @throws BadlyFormatedFileException Se o ficheiro tem a sintaxe errada
     */
    public void importTurnos(String filepath) throws FileNotFoundException, BadlyFormatedFileException{
        try{
            JsonReader jsonReader = Json.createReader(new FileReader(new File(filepath)));
            JsonObject jsonObject = jsonReader.readObject();
            new TurnoDAO().clear();
            Set<String> keySet = jsonObject.keySet();
            for(String key : keySet){
                JsonArray jsonArray = jsonObject.getJsonArray(key);
                int tCount = 1;
                int tpCount = 1;
                for(JsonValue j : jsonArray){
                    JsonObject jTurno = (JsonObject) j;
                    boolean ePratico = jTurno.getBoolean("ePratico");
                    int id = ePratico ? tpCount++ : tCount++;
                    List<TurnoInfo> tInfos = new ArrayList<>();
                    JsonArray jTinfos = jTurno.getJsonArray("tinfo");
                    for(JsonValue jvInfo : jTinfos){
                        JsonObject jTinfo = (JsonObject) jvInfo;
                        LocalTime horaInicio = LocalTime.parse(jTinfo.getString("horaInicio"));
                        LocalTime horaFim = LocalTime.parse(jTinfo.getString("horaFim"));
                        DiaSemana dia = DiaSemana.fromString(jTinfo.getString("dia"));
                        TurnoInfo tinfo = new TurnoInfo(horaInicio, horaFim, dia);
                        tInfos.add(tinfo);
                    }
                    Turno t = new Turno(id, key, jTurno.getString("docente"), jTurno.getInt("vagas"), ePratico, tInfos);
                    new TurnoDAO().put(new TurnoKey(t), t);
                }
            }
        }catch(NullPointerException | JsonParsingException e){
            throw new BadlyFormatedFileException();
        }
        this.setTurnosRegistados(true);
    }

    /**
     * Ativa os logins para os alunos
     */
    public void activateLogins(){
        Collection<Utilizador> users = this.utilizadores.values();
        users.forEach(Utilizador::ativarLogin);
        this.utilizadores.putAll(users.stream().collect(Collectors.toMap(Utilizador::getUserNum, user->user, (a, b)->b)));
        this.setLoginsAtivos(true);
    }

    /**
     * Atribui os turnos aos alunos
     */
    public void assignShifts() throws TurnoCheioException, NaoFoiPossivelAtribuirTurnosException{
        Map<String,List<Turno>> UCsETurnos = new HashMap<>();
        Set<Map.Entry<String,UC>> entries = this.ucs.entrySet();
        for(Map.Entry<String,UC> e : entries){
            UCsETurnos.put(e.getKey(), e.getValue().getTurnos());
        }
        List<Aluno> alunos = this.utilizadores.values().stream().filter(e -> e instanceof Aluno).map(e -> (Aluno) e).collect(Collectors.toList());
        List<UC> allUCs = new ArrayList<>(this.ucs.values());
        alunos.sort(Comparator.comparingInt((Aluno a)->a.getHorario().keySet().size()).reversed());
        allUCs.sort((uc1, uc2)->{
            int tIsize1 = uc1.getTurno(1,true).getTurnoInfos().size();
            int tIsize2 = uc2.getTurno(1,true).getTurnoInfos().size();
            if(tIsize1==tIsize2){
                int size1 = uc1.getTurnos().size();
                int size2 = uc2.getTurnos().size();
                if(size1==size2) return 0;
                return size1<size2 ? -1 : 1;
            }
            return tIsize1>tIsize2 ?-1 : 1;
        });
        //Para cada aluno
        for(Aluno a : alunos){
            //Para cada UC do aluno
            Set<String> horario = a.getHorario().keySet();
            List<UC> ucs = new ArrayList<>(allUCs).stream().filter(uc1 -> horario.contains(uc1.getId())).collect(Collectors.toList());
            for(UC uc : ucs){
                String ucID = uc.getId();
                List<Turno> turnos = UCsETurnos.get(ucID);
                turnos.sort(Comparator.comparingInt(Turno::getId));
                boolean haVagas = false;
                boolean temTurnoNaUC = a.getHorario().get(ucID)!=0;
                //Para cada turno da UC
                for(int i = 0; i < turnos.size() && !temTurnoNaUC; i++){
                    Turno t = turnos.get(i);
                    int tID = t.getId();
                    //Tentar que o aluno possa ser inserido no turno
                    if(tID!=0 && t.ePratico() && t.temVagas() && !horarioConfilcts(a, ucID, tID, t.ePratico())){
                        try{
                            a.inscrever(ucID,tID);
                        }catch(UtilizadorJaExisteException ignored){
                        }
                        temTurnoNaUC = true;
                    }
                    if(t.temVagas()) haVagas = true;
                }
                if(!haVagas) throw new TurnoCheioException(a.getUserNum());
                if(!temTurnoNaUC) throw new NaoFoiPossivelAtribuirTurnosException(a.getUserNum());
            }
        }
        Map<String,Utilizador> alunosMap = new HashMap<>();
        for(Aluno a : alunos){
            alunosMap.put(a.getUserNum(), a);
        }
        this.utilizadores.putAll(alunosMap);

        this.setTurnosAtribuidos(true);
    }

}
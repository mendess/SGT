package main.sgt;

import main.dao.*;
import main.sgt.exceptions.*;

import javax.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class SGT extends Observable{

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
     * Construtor do SGT
     */
    public SGT() {
        this.pedidosDAO = new PedidosDAO();
        this.trocas = new TrocaDAO();
        this.ucs = new UCDAO();
        this.utilizadores = new UserDAO();
        new DiaDAO().initDias();
        Collection<List<Pedido>> pedidosDB = this.pedidosDAO.values();
        Map<String,List<Pedido>> pedidosMEM = new HashMap<>();
        pedidosDB.stream()
                .filter(pedidoListDB -> !pedidoListDB.isEmpty())
                .forEach(pedidoListDB -> pedidoListDB.forEach(p -> {
                            if (!pedidosMEM.containsKey(p.getAlunoNum()))
                                pedidosMEM.put(p.getAlunoNum(), new ArrayList<>());
                            pedidosMEM.get(p.getAlunoNum()).add(p);
                        })
                );
        this.pedidos = pedidosMEM;
        Collection<Utilizador> utilizadores = this.utilizadores.values();
        Collection<UC> ucsValues = this.ucs.values();
        this.ucsRegistadas = !ucsValues.isEmpty();
        this.usersRegistados = !utilizadores.isEmpty();
        this.turnosRegistados = ucsValues.stream().noneMatch(uc-> uc.getTurnos().isEmpty());
        this.loginsAtivos = utilizadores.stream()
                                        .allMatch(Utilizador::isLoginAtivo);
        this.turnosAtribuidos = utilizadores.stream()
                                            .filter(u -> u instanceof Aluno)
                                            .noneMatch(a -> ((Aluno) a).getHorario().isEmpty());
        //TODO remove this:
        /*try {
            this.importUCs("jsons/ucs.json");
            this.importUtilizadores("jsons/utilizadores.json");
            this.importTurnos("jsons/turnos.json");
        } catch (FileNotFoundException | BadlyFormatedFileException e1) {
            e1.printStackTrace();
        }*/
    }

    /**
     * Retorna o estado das ucs
     * @return O estado das ucs
     */
    public boolean isUcsRegistadas() {
        return ucsRegistadas;
    }

    /**
     * Retorna o estado dos utilizadores
     * @return O estado dos utilizadores
     */
    public boolean isUsersRegistados() {
        return usersRegistados;
    }

    /**
     * Retorna o estado dos turnos
     * @return O estado dos turnos
     */
    public boolean isTurnosRegistados() {
        return turnosRegistados;
    }

    /**
     * Retorna o estado dos logins
     * @return O estado dos logins
     */
    public boolean isLoginsAtivos() {
        return loginsAtivos;
    }

    /**
     * Retorna se os turnos foram atribuidos
     * @return <tt>True</tt> se os turnos estao atribuidos
     */
    public boolean isTurnosAtribuidos() {
        return turnosAtribuidos;
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

    public Utilizador getLoggedUser(){
        return this.loggedUser;
    }
    /**
     * Devolve os Turnos de uma UC
     * @param uc UC
     * @return Lista de turnos da UC
     */
    public List<Turno> getTurnosOfUC(String uc) {
        List<Turno> turnos = this.ucs.get(uc).getTurnos();
        turnos.remove(Turno.emptyShift(uc));
        return turnos;
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
            Set<Map.Entry<String, List<TurnoKey>>> ucs = docente.getUcsEturnos().entrySet();
            for (Map.Entry<String, List<TurnoKey>> uc : ucs){
                UC tmpUC = this.ucs.get(uc.getKey());
                List<TurnoKey> tmpTurnos = uc.getValue();
                for(TurnoKey turno : tmpTurnos){
                    turnos.add(tmpUC.getTurno(turno.getTurno_id(), turno.ePratico()));
                }
            }
            return turnos;
        }
        throw new InvalidUserTypeException();
    }
    public List<UC> getUCsOfUser() throws InvalidUserTypeException {
        if(this.loggedUser instanceof Aluno){
            Aluno aluno = (Aluno) this.loggedUser;
            return aluno.getHorario().keySet()
                    .stream()
                    .map(this.ucs::get)
                    .collect(Collectors.toList());
        }
        if(this.loggedUser instanceof Docente){
            Docente docente = (Docente) this.loggedUser;
            return docente.getUcsEturnos().keySet()
                    .stream()
                    .map(this.ucs::get)
                    .collect(Collectors.toList());
        }
        throw new InvalidUserTypeException();
    }
    /**
     * Remove um aluno de um turno
     * @param uc Identificador da UC a que o turno pertence
     * @param aluno Numero do aluno a remover
     * @param turno Numero do turno de onde remover
     * @param ePratico Se o turno e pratico
     */
    public void removerAlunoDeTurno(String uc, String aluno, int turno, boolean ePratico) {
        UC newUC = this.ucs.get(uc);
        newUC.removerAlunoDeTurno(aluno,turno, ePratico);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Marca um aluno como presente
     * @param aluno Numero do aluno
     * @param uc Identificador da UC
     * @param turno Numero do turno
     * @param aula Aula
     * @param ePratico Se o turno e pratico     */
    public void marcarPresenca(String aluno, String uc, int turno, int aula, boolean ePratico) {
        UC newUC = this.ucs.get(uc);
        newUC.marcarPresenca(aluno,turno,aula, ePratico);
        this.ucs.put(newUC.getId(),newUC);
    }

    /**
     * Adiciona um aluno a uma UC
     * @param uc Identificador da UC a que pertence o turno
     * @param aluno Numero do aluno a adicionar
     * @param turno Numero do turno onde adicionar
     * @throws UtilizadorJaExisteException Se o aluno ja esta inscrito no turno
     */
    public void adicionarAlunoTurno(String uc, String aluno, int turno,boolean ePratico) throws UtilizadorJaExisteException {
        this.ucs.get(uc).adicionarAlunoTurno(aluno,turno, ePratico);
    }

    /**
     * Verifica se o horario do utilizador autenticado conflite com o turno
     * @param uc Identificador da UC a que pertence o turno
     * @param turno Numero do turno
     * @param ePratico Se o turno e pratico     * @return Retorna <tt>true</tt> se o horario conflite com o turno
     */
    public boolean horarioConfilcts(String uc, int turno, boolean ePratico) throws InvalidUserTypeException {
        if(this.loggedUser instanceof Aluno){
            Turno novoT = this.ucs.get(uc).getTurno(turno, ePratico);
            List<Turno> turnos = ((Aluno) this.loggedUser).getHorario().entrySet()
                    .stream()
                    .map(e -> this.ucs.get(e.getKey()).getTurno(e.getValue(), ePratico))
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
            Pedido newPedido = new Pedido(this.loggedUser.getUserNum(),this.loggedUser.getName(),uc,turno,true);
            if(this.pedidos.containsKey(this.loggedUser.getUserNum())){
                this.pedidos.get(this.loggedUser.getUserNum()).add(newPedido);
                this.pedidosDAO.put(newPedido);
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
                                                pedido.getTurno(),
                                                pedido.ePratico()))
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
     * @param ePratico Se o turno e pratico     * @throws InvalidUserTypeException O numero de aluno nao e valido
     * @throws AlunoNaoEstaInscritoNaUcException O aluno nao esta inscrito na UC
     */
    public void moveAlunoToTurno(String aluno, String uc, int turno, Object ePratico) throws InvalidUserTypeException, AlunoNaoEstaInscritoNaUcException {
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
     * Adiciona um docente a um turno
     * @param uc O identificador da UC do turno
     * @param turno O numero do turno
     * @param docente O identificador do docente
     * @param ePratico Se o turno e pratico     */
    public void setDocenteOfTurno(String uc, int turno, String docente, boolean ePratico){
        UC tmpUC = this.ucs.get(uc);
        tmpUC.addDocenteToTurno(turno,docente, ePratico);
        this.ucs.put(tmpUC.getId(),tmpUC);
    }

    public void removeDocenteFromTurno(String uc, int turno, String docente, boolean ePratico){
        UC tmpUC = this.ucs.get(uc);
        tmpUC.removeDocenteFromTurno(turno,docente, ePratico);
        this.ucs.put(tmpUC.getId(),tmpUC);
    }

    /**
     * Adiciona um docente a uma UC
     * @param docente Numero do docente a adicionar
     * @param uc Numero da UC onde adicionar
     * @throws UtilizadorJaExisteException Quando o docente ja leciona esta UC
     */
    @Deprecated
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
    @Deprecated
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
     * @param ePratico Se o turno e pratico     * @throws TurnoNaoVazioException Quando o turno tem alunos associados
     */
    public void removeTurno(int id, String uc, boolean ePratico) throws TurnoNaoVazioException {
        UC newUC = this.ucs.get(uc);
        newUC.removeTurno(id, ePratico);
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
    public void importTurnos(String filepath) throws FileNotFoundException, BadlyFormatedFileException {
        File file = new File(filepath);
        JsonReader jsonReader;
        jsonReader = Json.createReader(new FileReader(file));
        JsonObject jsonObject = jsonReader.readObject();
        Set<String> keySet = jsonObject.keySet();
        try{
            for(String key: keySet){
                JsonArray jsonArray = jsonObject.getJsonArray(key);
                int tCount = 1;
                int tpCount = 1;
                for(JsonValue j: jsonArray){
                    JsonObject jTurno = (JsonObject) j;
                    boolean ePratico = jTurno.getBoolean("ePratico");
                    int id = ePratico ? tpCount++ : tCount++;
                    List<TurnoInfo> tInfos = new ArrayList<>();
                    JsonArray jTinfos = jTurno.getJsonArray("tinfo");
                    for (JsonValue jvInfo: jTinfos){
                        JsonObject jTinfo = (JsonObject) jvInfo;
                        LocalTime horaInicio = LocalTime.parse(jTinfo.getString("horaInicio"));
                        LocalTime horaFim = LocalTime.parse(jTinfo.getString("horaFim"));
                        DiaSemana dia = DiaSemana.fromString(jTinfo.getString("dia"));
                        TurnoInfo tinfo = new TurnoInfo(horaInicio,horaFim,dia);
                        tInfos.add(tinfo);
                    }
                    Turno t = new Turno(id,key,jTurno.getInt("vagas"),ePratico, tInfos);
                    new TurnoDAO().put(new TurnoKey(t),t);
                }
            }
        }catch (NullPointerException e){
            throw new BadlyFormatedFileException();
        }
    }

    /**
     * Importa os alunos de um ficheiro
     * @param filepath Caminho para o ficheiro
     */
    public void importUtilizadores(String filepath) throws FileNotFoundException, BadlyFormatedFileException {
        JsonReader jreader = Json.createReader(new FileReader(new File(filepath)));
        JsonArray jarray = jreader.readArray();
        for(JsonValue j: jarray){
            JsonObject jobj = (JsonObject) j;
            String num = jobj.getString("Num");
            String nome = jobj.getString("Nome");
            String email = jobj.getString("Email");
            String password = jobj.getString("password");
            Utilizador user = null;
            switch (jobj.getString("Type")){
                case "A": {
                    boolean eEspecial = jobj.getBoolean("eEspecial");
                    user = new Aluno(num,password,email,nome,eEspecial,new HashMap<>());
                    break;
                }
                case "D": {
                    user = new Docente(num,password,email,nome,new HashMap<>());
                    break;
                }
                case "C": {
                    String ucRegida = jobj.getString("ucRegida");
                    user = new Coordenador(num,password,email,nome,new HashMap<>(),ucRegida);
                    break;
                }
                case "K": {
                    user = new DiretorDeCurso(num,password,email,nome);
                    break;
                }
            }
            if(user==null)
                throw new BadlyFormatedFileException();

            this.utilizadores.put(num,user);
        }
    }

    /**
     * Importa as UCs de um ficheiro
     * @param filepath Caminho para o ficheiro
     */
    public void importUCs(String filepath) throws FileNotFoundException, BadlyFormatedFileException {
        JsonReader jreader = Json.createReader(new FileReader(new File(filepath)));
        JsonArray jarray = jreader.readArray();
        try{
            for (JsonValue j : jarray) {
                JsonObject jobj = (JsonObject) j;
                String id = jobj.getString("id");
                String name = jobj.getString("name");
                String acron = jobj.getString("acron");
                this.ucs.put(id,new UC(id,name,acron));
            }
        }catch (NullPointerException e){
            throw new BadlyFormatedFileException();
        }
    }

    /**
     * Atribui os turnos aos alunos
     */
    public void assignShifts() {
        Map<String,List<Turno>> turnos = new HashMap<>();
        for(Map.Entry<String,UC> e : this.ucs.entrySet()){
            turnos.put(e.getKey(),e.getValue().getTurnos());
        }
        Set<Map.Entry<String, Aluno>> alunos = new HashSet<>();
        for (Map.Entry<String, Utilizador> e : this.utilizadores.entrySet()) {
            if (e.getValue() instanceof Aluno) {
                AbstractMap.SimpleEntry<String, Aluno> stringAlunoSimpleEntry = new AbstractMap.SimpleEntry<>(e.getKey(), (Aluno) e.getValue());
                alunos.add(stringAlunoSimpleEntry);
            }
        }


        // TODO - implement SGT.assignShifts
        this.turnosAtribuidos=true;
        throw new UnsupportedOperationException();
    }

    /**
     * Ativa os logins para os alunos
     */
    public void activateLogins() {
        this.utilizadores.values().forEach(Utilizador::ativarLogin);
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
     * @param ePratico Se o turno e pratico     */
    public void addAula(String uc, int turno, boolean ePratico) {
        this.ucs.get(uc).addAula(turno, ePratico);
    }

    /**
     * Remove uma aula de um turno
     * @param uc Identificador da UC do turno
     * @param turno Numero do turno
     * @param aula Numero da aula a remover
     * @param ePratico Se o turno e pratico     */
    public void removeAula(String uc, int turno, int aula, boolean ePratico) {
        this.ucs.get(uc).removeAula(turno,aula, ePratico);
    }

    public Aluno getAluno(String a) {
        Utilizador u = this.utilizadores.get(a);
        if(u instanceof Aluno){
            return (Aluno) u;
        }else {
            return null;
        }
    }

    public UC getUC(String uc) {
        return this.ucs.get(uc);
    }
}
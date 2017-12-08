package main.sgt;

import main.dao.UCDAO;
import main.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SGT {
    /**
     * Utilizador que está neste momento a utilizar a aplicação
     */
    private Utilizador loggedUser = null;
    /**
     * Pedidos de todos os utilizadores. Key: Aluno
     */
    private Map<String,List<Pedido>> pedidos;
    /**
     * Registo das trocas efetuadas
     */
    private List<Troca> trocas;
    /**
     * Map de ucs no sistema
     */
    private UCDAO ucs;
    /**
     * Map de utilizadores registados
     */
    private UserDAO utilizadores;


    public void login(String userNum, String password) {
        if(utilizadores.containsKey(userNum)){
            Utilizador user = utilizadores.get(userNum);
            if(user.getPassword().equals(password)){
                this.loggedUser=user;
            }else{
                //throw new WrongPasswordException();
            }
        }else{
            //throw new UserDoesntExistException();
        }
    }

    public List<Turno> getTurnosOfUC(String uc) {
        return ucs.get(uc).getTurnos();
    }

    public List<Turno> getTurnosUser() {
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
            Set<Map.Entry<String,List<Integer>>> ucs = docente.getTurnos().entrySet();
            for (Map.Entry<String,List<Integer>> uc : ucs){
                UC tmpUC = this.ucs.get(uc.getKey());
                List<Integer> tmpTurnos = uc.getValue();
                for(Integer turno : tmpTurnos){
                    turnos.add(tmpUC.getTurno(turno));
                }
            }
            return turnos;
        }
        return null;
    }

    public void marcarPresenca(String aluno, String uc, int turno, int aula) {
        ucs.get(uc).marcarPresenca(aluno,turno,aula);
    }

    public void removerAlunoDeTurno(String uc, String aluno, int turno) {
        ucs.get(uc).removerAlunoDeTurno(aluno,turno);
    }

    public void adicionarAlunoTurno(String uc, String aluno, int turno) {
        ucs.get(uc).adicionarAlunoTurno(aluno,turno);
    }

    public void pedirTroca(String uc, int turno) {
        this.pedidos.get(this.loggedUser.getUserNum()).add(new Pedido(this.loggedUser.getUserNum(),uc,turno));
    }

    public List<Pedido> getSujestoesTroca() {
        if(this.loggedUser instanceof Aluno){
            Aluno aluno = (Aluno) this.loggedUser;
            Map<String,Integer> inscricoes = aluno.getInscricoes();
            List<Pedido> sujestoes = new ArrayList<>();
            //TODO usar uma stream aqui para nao destruir tudo
            /*
            this.pedidos.forEach((key, value) -> value
                                                .stream()
                                                .filter(v -> inscricoes.containsKey(v.getUc())
                                                        && inscricoes.containsValue(v.getTurno())))
                        .;*/
        }
        return null;
    }

    public void realizarTroca(String aluno, String uc) {
        throw new UnsupportedOperationException();
    }

    public void addAlunoToUC(String aluno, String uc) {
        throw new UnsupportedOperationException();
    }

    public List<UC> getUCs() {
        throw new UnsupportedOperationException();
    }

    public void removeTurno(int id, String uc) {
        throw new UnsupportedOperationException();
    }

    public void addTurno(int id, boolean ePratico, String uc) {
        throw new UnsupportedOperationException();
    }

    public void importTurnos(String filepath) {
        throw new UnsupportedOperationException();
    }

    public void importAlunos(String filepath) {
        throw new UnsupportedOperationException();
    }

    public void importUCs(String filepath) {
        throw new UnsupportedOperationException();
    }

    public void assignShifts() {
        throw new UnsupportedOperationException();
    }

    public void activateLogins() {
        throw new UnsupportedOperationException();
    }

    public List<Troca> getTrocas() {
        throw new UnsupportedOperationException();
    }
}

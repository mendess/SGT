package main.sgt;

import main.sgt.exceptions.UtilizadorJaExisteException;
import main.sgt.exceptions.UtilizadorNaoExisteException;

import java.util.HashMap;
import java.util.Map;

public class Aluno extends Utilizador {

    /**
     * Se p aluno tem estatuto especial
     */
    private boolean eEspecial;
    /**
     * O horario do Aluno
     */
    private final Map<String, Integer> horario;

    /**
     * O construtor do aluno
     * @param userNum O identificador do aluno
     * @param password A password do aluno
     * @param email O email do aluno
     * @param name O nome do aluno
     * @param eEspecial <tt>true</tt> se o aluno tiver estatuto especial
     * @param horario As ucs e turnos a que o aluno esta inscrito
     */
    public Aluno(String userNum, String password, String email, String name, boolean eEspecial, Map<String, Integer> horario) {
        super(userNum,password,email,name);
        this.eEspecial = eEspecial;
        this.horario = horario;
    }

    /**
     * Construtor completamente parametrizado do Aluno
     * @param userNum O identificador do aluno
     * @param password A password do aluno
     * @param email O email do aluno
     * @param nome O nome do aluno
     * @param loginAtivo Se o login esta ativo
     * @param eEspecial <tt>true</tt> se o aluno tiver estatuto especial
     * @param horario As ucs e turnos a que o aluno esta inscrito
     */
    public Aluno(String userNum, String password, String email, String nome, boolean loginAtivo, boolean eEspecial,
                 Map<String, Integer> horario){
        super(userNum, password, email, nome, loginAtivo);
        this.eEspecial = eEspecial;
        this.horario = horario;
    }

    /**
     * Retorna se o aluno tem estatuto especial
     * @return <tt>true</tt> se o aluno tem estatuto especial, <tt>false</tt> caso contrario.
     */
    public boolean eEspecial() {
        return this.eEspecial;
    }

    /**
     * Muda o estatuto do aluno
     * @param eEspecial Novo estatuto.
     */
    void setEespecial(boolean eEspecial) {
        this.eEspecial = eEspecial;
    }

    /**
     * Retorna o horario do aluno.
     * @return O horario do aluno
     */
    public Map<String, Integer> getHorario() {
        return new HashMap<>(this.horario);
    }

    /**
     * Inscreve um aluno num turno. WARNING este metodo tambem retira o aluno do turno onde estava
     * @param uc O identificador da UC do turno
     * @param turno O numero do turno
     */
    void inscrever(String uc, int turno) throws UtilizadorJaExisteException{
        if(turno == 0 && this.horario.containsKey(uc)){
            throw new UtilizadorJaExisteException();
        }
        this.horario.put(uc,turno);
    }

    /**
     * Desinscreve um aluno de um turno
     * @param uc Identificador da UC do turno
     * @param turno Numero do turno
     */
    void desinscrever(String uc, int turno) throws UtilizadorNaoExisteException{
        if(turno==0 && !this.horario.containsKey(uc)) throw new UtilizadorNaoExisteException();
        this.horario.put(uc,0);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        Aluno a = (Aluno) o;
        return super.equals(o)
                && this.eEspecial == a.eEspecial()
                && this.horario.equals(a.getHorario());
    }

    @Override
    public String toString() {
        return "Aluno: \t"
                +super.toString()+"\t"
                +"e Especial: "+this.eEspecial+"\t"
                +"Horario: "+this.horario+"\t";
    }
}
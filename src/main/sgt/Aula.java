package main.sgt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Aula {

    /**
     * O numero da aula
     */
    private int numero;
    /**
     * A lista dos alunos que estiveram presentes nesta aula
     */
    private List<String> presencas;
    /**
     * O numero do turno a que a aula pertence
     */
    private int turno;
    /**
     * O identificador da UC a que a aula pertence
     */
    private String uc;
    /**
     * Se a aula pertence a um turno pratico
     */
    private boolean ePratico;

    /**
     * Construtor de aula
     * @param uc A UC do turno
     * @param turno O numero do turno da aula
     * @param ePratico Se a aula e pratica
     */
    public Aula(int numero, String uc, int turno, boolean ePratico) {
        this.uc = uc;
        this.turno = turno;
        this.numero = numero;
        this.ePratico = ePratico;
        this.presencas = new ArrayList<>();
    }

    /**
     * Construtor completamente parametrizado de Aula
     * @param numero Numero da Aula
     * @param uc A UC do turno
     * @param turno O numero do turno da aula
     * @param ePratico Se a aula e pratica
     * @param presencas A lista de presencas da aula
     */
    public Aula(int numero, String uc, int turno, boolean ePratico, List<String> presencas) {
        this.numero = numero;
        this.uc = uc;
        this.turno = turno;
        this.ePratico = ePratico;
        this.presencas = presencas;
    }

    /**
     * Retorna o numero da Aula
     * @return O numero da Aula
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Marca presenca a um aluno
     * @param aluno Identificador do aluno
     */
    void marcarPresenca(String aluno) {
        if(!this.presencas.contains(aluno)){
            this.presencas.add(aluno);
        }
    }

    /**
     * Retorna o numero do turno a que a aula pertence
     * @return O numero do turno a que a aula pertence
     */
    public int getTurno() {
        return this.turno;
    }

    /**
     * Retorna o identificador da UC a que a aula pertence
     * @return O identificador da UC a que a aula pertence
     */
    public String getUc() {
        return this.uc;
    }

    /**
     * Retorna se a Aula pertence a um turno pratico
     * @return Se a Aula pertence a um turno pratico
     */
    public boolean ePratico() {
        return this.ePratico;
    }

    /**
     * Retorna a lista ordenada de alunos presentes na aula
     * @return A lista ordenada de alunos presentes na aula
     */
    public List<String> getPresencas() {
        List<String> alPresentes = new ArrayList<>(presencas);
        Collections.sort(alPresentes);
        return alPresentes;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        Aula a = (Aula) o;
        return super.equals(o)
                && this.numero == a.getNumero()
                && this.turno == a.getTurno()
                && this.uc.equals(a.getUc())
                && this.ePratico == a.ePratico()
                && this.presencas.equals(a.getPresencas());
    }

    @Override
    public String toString() {
        return  "Aula: \t"
                +"Numero: "+this.numero+"\t"
                +"Turno: "+this.turno+"\t"
                +"UC: "+this.uc+"\t"
                +"E Pratica: "+ this.ePratico +"\t"
                +"Presencas: "+this.presencas.toString();
    }
}
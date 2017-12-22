package main.sgt;

@SuppressWarnings("WeakerAccess")
public class Pedido {

    /**
     *  Numero do aluno que fez o pedido
     */
    private String alunoNum;
    /**
     * Nome do aluno que fez o pedido
     */
    private String alunoNome;
    /**
     * Uc do pedido
     */
    private String uc;
    /**
     * Turno pedido
     */
    private int turno;
    private boolean ePratico;

    /**
     * Construtor do <tt>Pedido</tt>
     * @param alunoNum Numero do aluno que fez o pedido
     * @param alunoNome Nome do aluno que fez o pedido
     * @param uc Identificador da UC a que pertence o turno pedido
     * @param turno Numero do turno pedido
     * @param ePratico
     */
    public Pedido(String alunoNum, String alunoNome, String uc, int turno, boolean ePratico) {
        this.alunoNum = alunoNum;
        this.alunoNome = alunoNome;
        this.uc = uc;
        this.turno = turno;
        this.ePratico = ePratico;
    }

    /**
     * Retorna o numero do aluno que fez o pedido
     * @return O numero do aluno que fez o pedido
     */
    public String getAlunoNum() {
        return this.alunoNum;
    }

    /**
     * Retorna o nome do aluno que fez o pedido
     * @return O nome do aluno que fez o pedido
     */
    public String getAlunoNome() {
        return this.alunoNome;
    }

    /**
     * Retorna o identificador da UC a que o turno pedido pertence
     * @return O identificador da UC a que o turno pedido pertence
     */
    public String getUc() {
        return this.uc;
    }

    /**
     * Retorna o numero do turno pedido
     * @return O numero do turno pedido
     */
    public int getTurno() {
        return this.turno;
    }

    public boolean ePratico() {
        return this.ePratico;
    }

    @Override
    public String toString() {
        return "Pedido: "
                +"Aluno#: "+ this.alunoNum+"\t"
                +"AlunoNome: "+ this.alunoNome+"\t"
                +"Turno: "+ this.turno+"\t"
                +"UC: "+this.uc;
    }
}
package main.sgt;

@SuppressWarnings("ALL")
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

	/**
	 * Construtor do <tt>Pedido</tt>
	 * @param alunoNum Numero do aluno que fez o pedido
	 * @param alunoNome Nome do aluno que fez o pedido
	 * @param uc Identificador da UC a que pertence o turno pedido
	 * @param turno Numero do turno pedido
	 */
	Pedido(String alunoNum, String alunoNome, String uc, int turno) {
		this.alunoNum = alunoNum;
		this.alunoNome = alunoNome;
		this.uc = uc;
		this.turno = turno;
	}

	public String getAlunoNum() {
		return this.alunoNum;
	}

    public String getAlunoNome() {
        return this.alunoNome;
    }

	public String getUc() {
		return this.uc;
	}

	public int getTurno() {
	    return this.turno;
	}
}
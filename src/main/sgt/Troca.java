package main.sgt;

import java.time.LocalDateTime;

@SuppressWarnings("WeakerAccess")
public class Troca {

    /**
     * O id da Troca
     */
    private final int id;
    /**
     * O aluno que trocou de turno
     */
    private final String aluno;
    /**
     * A UC a que os turnos da Troca pertencem
     */
    private final String uc;
    /**
     * O Turno de onde o Aluno veio
     */
    private final int turnoOrigem;
    private final boolean turnoOrigem_ePratico;
    /**
     * O Turno para onde o Aluno foi
     */
    private final int turnoDestino;
    private final boolean turnoDestino_ePratico;
    /**
     * A data em que a Troca foi efetuada
     */
    private LocalDateTime data;

    /**
     * Construtor de Troca completamente parameterizado
     * @param id Id unico da troca
     * @param aluno O identificador do aluno que mudou de turno
     * @param uc A UC dentro da qual a troca foi efetuada
     * @param turnoOrigem O turno de onde o aluno veio
     * @param turnoOrigem_ePratico Se o turno origem e pratico
     * @param turnoDestino O turno para onde o aluno foi
     * @param turnoDestino_ePratico Se o turno destino e pratico
     * @param data Data da troca
     */
    public Troca(int id, String aluno, String uc, int turnoOrigem, boolean turnoOrigem_ePratico, int turnoDestino, boolean turnoDestino_ePratico, LocalDateTime data){
        this.id = id;
        this.aluno = aluno;
        this.uc = uc;
        this.turnoOrigem = turnoOrigem;
        this.turnoOrigem_ePratico = turnoOrigem_ePratico;
        this.turnoDestino = turnoDestino;
        this.turnoDestino_ePratico = turnoDestino_ePratico;
        this.data = data;
    }
    /**
     * Construtor de troca.
     * @param id Id unico da troca
     * @param aluno O identificador do aluno que mudou de turno
     * @param uc A UC dentro da qual a troca foi efetuada
     * @param turnoOrigem O turno de onde o aluno veio
     * @param turnoOrigem_ePratico Se o turno origem e pratico
     * @param turnoDestino O turno para onde o aluno foi
     * @param turnoDestino_ePratico Se o turno destino e pratico
     */
    public Troca(int id, String aluno, String uc, int turnoOrigem, boolean turnoOrigem_ePratico, int turnoDestino, boolean turnoDestino_ePratico) {
        this.id = id;
        this.aluno = aluno;
        this.uc = uc;
        this.turnoOrigem = turnoOrigem;
        this.turnoOrigem_ePratico = turnoOrigem_ePratico;
        this.turnoDestino = turnoDestino;
        this.turnoDestino_ePratico = turnoDestino_ePratico;
        this.data = LocalDateTime.now();
    }

    /**
     * Contrutor de troca sem id
     * @param aluno O identificador do aluno que mudou de turno
     * @param uc A UC dentro da qual a troca foi efetuada
     * @param turnoOrigem O turno de onde o aluno veio
     * @param turnoOrigem_ePratico Se o turno origem e pratico
     * @param turnoDestino O turno para onde o aluno foi
     * @param turnoDestino_ePratico Se o turno destino e pratico
     */
    public Troca(String aluno, String uc, int turnoOrigem, boolean turnoOrigem_ePratico, int turnoDestino, boolean turnoDestino_ePratico){
        this.id=1;
        this.turnoOrigem_ePratico = turnoOrigem_ePratico;
        this.turnoDestino_ePratico = turnoDestino_ePratico;
        this.aluno = aluno;
        this.uc = uc;
        this.turnoOrigem = turnoOrigem;
        this.turnoDestino = turnoDestino;
    }

    /**
     * Retorna o identificador do aluno
     * @return o identificador do aluno
     */
    public String getAluno() {
        return this.aluno;
    }

    /**
     * Retorna a UC onde foi feita a troca
     * @return A UC onde foi feita a troca
     */
    public String getUc() {
        return this.uc;
    }

    /**
     * Retorna o turno de onde o aluno que trocou veio
     * @return O turno de onde o aluno que trocou veio
     */
    public int getTurnoOrigem() {
        return this.turnoOrigem;
    }

    /**
     * Retorna o turno para onde o aluno foi
     * @return O turno para onde o aluno foi
     */
    public int getTurnoDestino() {
        return this.turnoDestino;
    }

    /**
     * Retorna a data da troca
     * @return A data da troca
     */
    public LocalDateTime getData() {
        return this.data;
    }

    /**
     * Retorna o id da Troca
     * @return O id da Troca
     */
    public int getId() {
        return id;
    }

    /**
     * Se o turno origem e Pratico
     * @return <tt>True</tt> se o turno origem e pratico
     */
    public boolean isTurnoOrigem_Pratico() {
        return this.turnoOrigem_ePratico;
    }

    /**
     * Se o turno destino e pratico
     * @return <tt>True</tt> se o turno destino for pratico
     */
    public boolean isTurnoDestino_Pratico() {
        return this.turnoDestino_ePratico;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        Troca t = (Troca) o;
        return super.equals(t)
                && this.id == t.getId()
                && this.uc.equals(t.getUc())
                && this.aluno.equals(t.getAluno())
                && this.turnoOrigem == t.getTurnoOrigem()
                && this.turnoDestino == t.getTurnoDestino();
    }

    @Override
    public String toString() {
        return "Troca: \t"
                +"Id: "+this.id+"\t"
                +"Aluno: "+this.aluno+"\t"
                +"UC: "+this.uc+"\t"
                +"TurnoOrigem: "+this.turnoOrigem+"\t"
                +"TurnoDestino: "+this.turnoDestino+"\t"
                +"Data: "+this.data;
    }
}
package main.sgt;

public class AulaKey {
    private String uc_id;
    private int turno_id;
    private int aula_id;
    private boolean ePratico;

    public AulaKey(String uc_id, int turno_id, int aula_id, boolean ePratico){
        this.uc_id = uc_id;
        this.turno_id = turno_id;
        this.aula_id = aula_id;
        this.ePratico = ePratico;
    }

    public AulaKey(Aula a) {
        this.uc_id = a.getUc();
        this.turno_id = a.getTurno();
        this.aula_id = a.getNumero();
        this.ePratico = a.ePratico();
    }

    public String getUc_id() {
        return this.uc_id;
    }

    public int getTurno_id() {
        return this.turno_id;
    }

    public int getAula_id() {
        return this.aula_id;
    }

    public boolean ePratico() {
        return this.ePratico;
    }

    @Override
    public String toString() {
        return "AulaKey: \t"
                + "Num: " + this.aula_id +"\t"
                + "Turno: " + this.turno_id + "\t"
                + "UC: " + this.uc_id +"\t"
                + "E Pratica: " + this.ePratico;
    }
}

package main.sgt;

public class TurnoKey {
    private String uc_id;
    private int turno_id;
    private boolean ePratico;

    public TurnoKey(String uc_id,int turno_id,boolean ePratico){
        this.uc_id = uc_id;
        this.turno_id = turno_id;
        this.ePratico = ePratico;
    }

    public TurnoKey(Turno t) {
        this.uc_id = t.getUcId();
        this.turno_id = t.getId();
        this.ePratico = t.ePratico();
    }

    public String getUc_id() {
        return this.uc_id;
    }

    public int getTurno_id() {
        return this.turno_id;
    }

    public String toString() {
        return "TurnoKey: \t"
                + "Turno: " + this.turno_id + "\t"
                + "Pratico: "+ this.ePratico + "\t"
                + "UC: " + this.uc_id +"\t";
    }

    public boolean ePratico() {
        return this.ePratico;
    }
}

package main.sgt;

public class TurnoKey {
    private String uc_id;
    private int turno_id;

    public TurnoKey(String uc_id,int turno_id){
        this.uc_id = uc_id;
        this.turno_id = turno_id;
    }

    public TurnoKey(Turno t) {
        this.uc_id = t.getUcId();
        this.turno_id = t.getId();
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
                + "UC: " + this.uc_id +"\t";
    }
}

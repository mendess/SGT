package main.sgt;

public class AulaKey {
    private String uc_id;
    private int turno_id;
    private int aula_id;

    public AulaKey(String uc_id, int turno_id, int aula_id){
        this.uc_id = uc_id;
        this.turno_id = turno_id;
        this.aula_id = aula_id;
    }

    public AulaKey(Aula a) {
        this.uc_id = a.getUc();
        this.turno_id = a.getTurno();
        this.aula_id = a.getNumero();
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
}

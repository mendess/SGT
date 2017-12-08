package main.sgt;

public class Pedido {
    private String userNum;
    private String uc;
    private int turno;

    Pedido(String userNum, String uc, int turno) {
        this.userNum = userNum;
        this.uc = uc;
        this.turno = turno;
    }

    String getUc() {
        return this.uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    int getTurno() {
        return this.turno;
    }

    void setTurno(int turno) {
        this.turno = turno;
    }

    String getUserNum() {
        return userNum;
    }

    void setUserNum(String userNum) {
        this.userNum = userNum;
    }
}

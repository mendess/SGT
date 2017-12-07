package main.sgt;

import java.time.LocalTime;

public class TurnoInfo {
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private int vagas;
    public DiaSemana _dia;

    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return this.horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public int getVagas() {
        return this.vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public DiaSemana getDia() {
        throw new UnsupportedOperationException();
    }

    public void setDia(DiaSemana dia) {
        throw new UnsupportedOperationException();
    }
}

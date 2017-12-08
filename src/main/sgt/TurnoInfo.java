package main.sgt;

import java.time.LocalTime;

class TurnoInfo {
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private int vagas;
    private DiaSemana dia;

    LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    LocalTime getHoraFim() {
        return this.horaFim;
    }

    void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    int getVagas() {
        return this.vagas;
    }

    void setVagas(int vagas) {
        this.vagas = vagas;
    }

    DiaSemana getDia() {
        throw new UnsupportedOperationException();
    }

    void setDia(DiaSemana dia) {
        throw new UnsupportedOperationException();
    }
}

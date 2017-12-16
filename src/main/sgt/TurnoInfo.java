package main.sgt;

import java.time.LocalTime;

class TurnoInfo {

	private LocalTime horaInicio;
	private LocalTime horaFim;
	private DiaSemana dia;

	/**
	 * Construtor do <tt>TurnoInfo</tt>
	 * @param horaInicio Hora de inicio
	 * @param horaFim Hora de fim
	 * @param dia Dia da semana
	 */
	TurnoInfo(LocalTime horaInicio, LocalTime horaFim, DiaSemana dia) {
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.dia = dia;
	}

    /**
     * Construtor de copia
     * @param tinfo <tt>TurnoInfo</tt> a copiar
     */
	TurnoInfo(TurnoInfo tinfo) {
		this.horaInicio = tinfo.getHoraInicio();
		this.horaFim = tinfo.getHoraFim();
		this.dia = tinfo.getDia();
	}

    /**
     * Retorna a hora de inicio
     * @return A hora de inicio
     */
	LocalTime getHoraInicio() {
		return this.horaInicio;
	}

	/**
	 * Muda a hora de inicio
	 * @param horaInicio Nova hora de inicio
	 */
	void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

    /**
     * Retorna a hora de fim
     * @return A hora de fim
     */
	LocalTime getHoraFim() {
		return this.horaFim;
	}

	/**
	 * Mudifica a hora de fim
	 * @param horaFim Nova hora de fim
	 */
	void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

    /**
     * Retorna o dia
     * @return O dia
     */
	DiaSemana getDia() {
		return this.dia;
	}

	/**
	 * Muda o dia do turno
	 * @param dia Novo dia
	 */
	void setDia(DiaSemana dia) {
		this.dia = dia;
	}

}
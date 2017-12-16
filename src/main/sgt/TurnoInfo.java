package main.sgt;

import java.time.LocalTime;

public class TurnoInfo {

	private LocalTime horaInicio;
	private LocalTime horaFim;

	/**
	 * 
	 * @param horaInicio
	 * @param horaFim
	 * @param dia
	 */
	TurnoInfo(LocalTime horaInicio, LocalTime horaFim, DiaSemana dia) {
		//TODO - implement TurnoInfo.TurnoInfo
		throw new UnsupportedOperationException();
	}

	LocalTime getHoraInicio() {
		return this.horaInicio;
	}

	/**
	 * 
	 * @param horaInicio
	 */
	void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	LocalTime getHoraFim() {
		return this.horaFim;
	}

	/**
	 * 
	 * @param horaFim
	 */
	void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

	DiaSemana getDia() {
		//TODO - implement TurnoInfo.getDia
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param dia
	 */
	void setDia(DiaSemana dia) {
		//TODO - implement TurnoInfo.setDia
		throw new UnsupportedOperationException();
	}

}
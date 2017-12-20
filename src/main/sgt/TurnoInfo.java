package main.sgt;

import java.time.LocalTime;

public class TurnoInfo {

	private LocalTime horaInicio;
	private LocalTime horaFim;
	private DiaSemana dia;


	/**
	 * Construtor do <tt>TurnoInfo</tt>
	 * @param horaInicio Hora de inicio
	 * @param horaFim Hora de fim
	 * @param dia Dia da semana
	 */
	public TurnoInfo(LocalTime horaInicio, LocalTime horaFim, DiaSemana dia) {
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.dia = dia;
	}

    /**
	 * Construtor de copia
     * @param tinfo <tt>TurnoInfo</tt> a copiar
     */
	public TurnoInfo(TurnoInfo tinfo) {
		this.horaInicio = tinfo.getHoraInicio();
		this.horaFim = tinfo.getHoraFim();
		this.dia = tinfo.getDia();
	}

    /**
     * Retorna a hora de inicio
     * @return A hora de inicio
     */
	public LocalTime getHoraInicio() {
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
	public LocalTime getHoraFim() {
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
	public DiaSemana getDia() {
		return this.dia;
	}

	/**
	 * Muda o dia do turno
	 * @param dia Novo dia
	 */
	void setDia(DiaSemana dia) {
		this.dia = dia;
	}

	public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        
        TurnoInfo a = (TurnoInfo) o;
        
        return this.horaInicio.equals(a.getHoraInicio()) &&
        			this.horaFim.equals(a.getHoraFim()) &&
        			this.dia.equals(a.getDia());
	}
	
	public String toString() {
        return "Hora Inicio: " + this.horaFim.toString() + "/t"
        			+ "Hora fim: " + this.horaInicio.toString() + "/t"
        			+ "Dia: " + this.dia.toString();
    }
	

}
package main.sgt;

public enum DiaSemana {
	SEGUNDA,
	TERCA,
	QUARTA,
	QUINTA,
	SEXTA,
	SABADO,
	DOMINGO;

	public static DiaSemana fromString(String dia) {
		switch (dia){
			case "SEG": return SEGUNDA;
			case "TER": return TERCA;
			case "QUA": return QUARTA;
			case "QUI": return QUINTA;
			case "SEX": return SEXTA;
			case "SAB": return SABADO;
			case "DOM": return DOMINGO;
			default: return null;
		}
	}
}
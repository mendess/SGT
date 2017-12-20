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

    public static String toString(DiaSemana dia){
        switch (dia){
            case SEGUNDA: return "SEG";
            case TERCA: return "TER";
            case QUARTA: return "QUA";
            case QUINTA: return "QUI";
            case SEXTA: return "SEX";
            case SABADO: return "SAB";
            case DOMINGO: return "DOM";
            default: return null;
        }
    }
}
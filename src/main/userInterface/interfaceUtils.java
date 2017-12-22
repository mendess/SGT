package main.userInterface;

import main.sgt.Turno;

class interfaceUtils {
    static boolean shiftTypeFromStr(String turno) {
        return turno.contains("TP");
    }

    static int shiftFromString(String turno) {
        return Integer.parseInt(turno.replaceAll("[^\\d.]", ""));
    }

    static String makeShiftString(Turno t){
        return t.ePratico() ? "TP" + t.getId() : "T" + t.getId();
    }
}

package main.userInterface;

import main.sgt.Turno;
import main.sgt.TurnoInfo;
import main.sgt.TurnoKey;

import javax.swing.table.DefaultTableModel;
import java.util.List;

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
    static String makeShiftString(TurnoKey t) {
        return t.ePratico() ? "TP" + t.getTurno_id() : "T" + t.getTurno_id();
    }

    static DefaultTableModel prepareTable(int size,int numCol, DefaultTableModel tModel){
        while (size>tModel.getRowCount()){
            tModel.addRow(new String[numCol]);
        }
        while (size<tModel.getRowCount()){
            tModel.removeRow(tModel.getRowCount()-1);
        }
        return tModel;
    }
    static DefaultTableModel makeShiftLookUpTable(DefaultTableModel tModel, List<Turno> turnos){
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            StringBuilder dias = new StringBuilder();
            StringBuilder horasInicio = new StringBuilder();
            StringBuilder horasFim = new StringBuilder();
            for (TurnoInfo ti : t.getTurnoInfos()) {
                dias.append(ti.getDia()).append("\n");
                horasInicio.append(ti.getHoraInicio()).append("\n");
                horasFim.append(ti.getHoraFim()).append("\n");
            }
            dias.setLength(Math.max(dias.length() - 1, 0));
            horasInicio.setLength(Math.max(horasInicio.length() - 1, 0));
            horasFim.setLength(Math.max(horasFim.length() - 1, 0));

            tModel.setValueAt(t.getId(), i, 0);
            tModel.setValueAt(dias.toString(), i, 1);
            tModel.setValueAt(horasInicio.toString(), i, 2);
            tModel.setValueAt(horasFim.toString(), i, 3);
        }
        return tModel;
    }
}

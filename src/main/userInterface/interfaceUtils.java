package main.userInterface;

import main.sgt.Turno;

import javax.swing.table.DefaultTableModel;

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
    static DefaultTableModel prepareTable(int size,int numCol, DefaultTableModel tModel){
        while (size>tModel.getRowCount()){
            tModel.addRow(new String[numCol]);
        }
        while (size<tModel.getRowCount()){
            tModel.removeRow(tModel.getRowCount()-1);
        }
        return tModel;
    }
}

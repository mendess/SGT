package main.userInterface;

import main.sgt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.Set;

class InterfaceUtils {
    static boolean shiftTypeFromStr(String turno) {
        return turno.contains("TP");
    }

    static int shiftFromString(String turno) {
        return Integer.parseInt(turno.replaceAll("[^\\d.]", ""));
    }

    static String makeShiftString(Turno t){
        return t.ePratico() ? "TP" + t.getId() : "T" + t.getId();
    }
    private static String makeShiftString(TurnoKey t) {
        return t.ePratico() ? "TP" + t.getTurno_id() : "T" + t.getTurno_id();
    }

    static DefaultTableModel prepareTable(int size, int numCol, JTable jTable){
        DefaultTableModel tModel = (DefaultTableModel) jTable.getModel();
        while (size>tModel.getRowCount()){
            tModel.addRow(new String[numCol]);
        }
        while (size<tModel.getRowCount()){
            tModel.removeRow(tModel.getRowCount()-1);
        }
        return tModel;
    }
    static DefaultTableModel makeShiftLookUpTable(JTable jTable, List<Turno> turnos) {
        DefaultTableModel tModel = prepareTable(turnos.size(),4,jTable);
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            StringBuilder dias = new StringBuilder();
            StringBuilder horasInicio = new StringBuilder();
            StringBuilder horasFim = new StringBuilder();
            for (TurnoInfo ti : t.getTurnoInfos()) {
                dias.append(ti.getDia()).append(" | ");
                horasInicio.append(ti.getHoraInicio()).append(" | ");
                horasFim.append(ti.getHoraFim()).append(" | ");
            }
            dias.setLength(Math.max(dias.length() - 2, 0));
            horasInicio.setLength(Math.max(horasInicio.length() - 2, 0));
            horasFim.setLength(Math.max(horasFim.length() - 2, 0));

            tModel.setValueAt(t.getId(), i, 0);
            tModel.setValueAt(dias.toString(), i, 1);
            tModel.setValueAt(horasInicio.toString(), i, 2);
            tModel.setValueAt(horasFim.toString(), i, 3);
        }
        return tModel;
    }

    static TableModel makeUCLookupTable(JTable jTable, List<UC> ucs) {
        DefaultTableModel tModel = prepareTable(ucs.size(),2,jTable);
        for (int i = 0; i < ucs.size(); i++) {
            UC uc = ucs.get(i);
            tModel.setValueAt(uc.getId(), i, 0);
            tModel.setValueAt(uc.getNome(),i,1);
        }
        return tModel;
    }
    static DefaultTableModel makeStudentLookupTable(JTable jTable, List<Aluno> alunos){
        DefaultTableModel tModel = prepareTable(alunos.size(),2, jTable);
        for (int i = 0; i < alunos.size(); i++) {
            Aluno a = alunos.get(i);
            tModel.setValueAt(a.getUserNum(), i, 0);
            tModel.setValueAt(a.getName(), i, 1);
        }
        return tModel;
    }
    static String makeComboBoxUCs(JComboBox<String> jComboBoxUC, List<UC> uCsOfUser){
        jComboBoxUC.removeAllItems();
        uCsOfUser.stream().map(UC::getId).forEach(jComboBoxUC::addItem);
        return (String) jComboBoxUC.getSelectedItem();
    }

    static String makeComboBoxUCs(JComboBox<String> jComboBoxUC, Set<String> uCsOfUser){
        jComboBoxUC.removeAllItems();
        uCsOfUser.forEach(jComboBoxUC::addItem);
        return (String) jComboBoxUC.getSelectedItem();
    }

    static String makeComboBoxTurnos(JComboBox<String> jComboBoxTurno, List<Turno> turnos, String uc){
        jComboBoxTurno.removeAllItems();
        turnos.stream()
                .filter(t->t.getUcId().equals(uc))
                .map(InterfaceUtils::makeShiftString)
                .forEach(jComboBoxTurno::addItem);
        return (String) jComboBoxTurno.getSelectedItem();
    }

    static String makeComboBoxTurnos(JComboBox<String> jComboBoxTurno, List<TurnoKey> turnoKeys) {
        jComboBoxTurno.removeAllItems();
        turnoKeys.stream()
                .map(InterfaceUtils::makeShiftString)
                .forEach(jComboBoxTurno::addItem);
        return (String) jComboBoxTurno.getSelectedItem();
    }
}

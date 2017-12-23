/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.userInterface;

import main.sgt.*;
import main.sgt.exceptions.WrongCredentialsException;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static main.userInterface.interfaceUtils.*;

/**
 *
 * @author pedro
 */
@SuppressWarnings({"TryWithIdenticalCatches", "Convert2Lambda", "Anonymous2MethodRef"})
public class JDocenteMarcarPresencas extends javax.swing.JFrame {

    private final SGT sgt;
    private Docente loggedUser;
    private String uc;
    private String turno;
    private int aula;


    //TODO add action listener to table
    /**
     * Creates new form DocenteMarcarPresencas
     */
    public JDocenteMarcarPresencas(SGT sgt) {
        this.sgt = sgt;
        try {
            this.sgt.login("D18686", "password");
        } catch (WrongCredentialsException e) {
            e.printStackTrace();
            return;
        }
        this.loggedUser = (Docente) this.sgt.getLoggedUser();
        initComponents();
        initComboBoxUCs();
//        ButtonRenderer buttonPresenca = new ButtonRenderer();
//        buttonPresenca.addTableButtonListener(new TableButtonListener() {
//            @Override
//            public void tableButtonClicked(int row, int col) {
//                marcarPrescenca(row);
//            }
//        });
//        this.jTablePresencas.getColumn("Presenca").setCellRenderer(new Checkbox());
//        this.jTablePresencas.getColumn("Presenca").setCellEditor(buttonPresenca);

    }

    private void initComboBoxUCs() {
        this.jComboBoxUCs.removeAllItems();
        Set<String> ucs = this.loggedUser.getUcsEturnos().keySet();
        for(String uc: ucs){
            this.jComboBoxUCs.addItem(uc);
        }
        this.uc = (String) this.jComboBoxUCs.getSelectedItem();
        initComboBoxTurnos();
    }

    private void initComboBoxTurnos() {
        this.jComboBoxTurnos.removeAllItems();
        String uc = (String) this.jComboBoxUCs.getSelectedItem();
        if(uc==null) return;
        for(TurnoKey turno: this.loggedUser.getUcsEturnos().get(uc)){
            this.jComboBoxTurnos.addItem(makeShiftString(turno));
        }
        this.turno = (String) this.jComboBoxTurnos.getSelectedItem();
        initComboBoxAulas();
    }

    private void initComboBoxAulas() {
        this.jComboBoxAula.removeAllItems();
        String uc = (String) this.jComboBoxUCs.getSelectedItem();
        String turno = (String) this.jComboBoxTurnos.getSelectedItem();
        if(uc==null || turno==null) return;
        for(Aula a: this.sgt.getUC(uc).getTurno(shiftFromString(turno),shiftTypeFromStr(turno)).getAulas()){
            this.jComboBoxAula.addItem(String.valueOf(a.getNumero()));
        }
        String aula = (String) this.jComboBoxAula.getSelectedItem();
        if(aula !=null){
            this.aula = Integer.parseInt(aula);
            initPresencas();
        }else {
            ((DefaultTableModel) this.jTablePresencas.getModel()).setRowCount(0);
        }
    }

    private void initPresencas() {
        Turno t = this.sgt.getUC(this.uc).getTurno(shiftFromString(this.turno),shiftTypeFromStr(this.turno));
        List<String> naoPresentes = t.getAlunos();
        List<String> presentes = t.getAula(this.aula).getPresencas();
        naoPresentes.removeAll(presentes);
        DefaultTableModel tModel = (DefaultTableModel) this.jTablePresencas.getModel();
        tModel = prepareTable(naoPresentes.size()+presentes.size(),3,tModel);
        int i = 0;
        for(String aluno: naoPresentes){
            Aluno a = this.sgt.getAluno(aluno);
            tModel.setValueAt(a.getUserNum(),i,0);
            tModel.setValueAt(a.getName(),i,1);
//            tModel.setValueAt("Marcar Presente",i,2);
            tModel.setValueAt(false,i,2);
            i++;
        }
        for (String aluno: presentes){
            Aluno a = this.sgt.getAluno(aluno);
            tModel.setValueAt(a.getUserNum(),i,0);
            tModel.setValueAt(a.getName(),i,1);
//            tModel.setValueAt("Presente",i,2);
            tModel.setValueAt(true,i,2);
            i++;
        }
        this.jTablePresencas.setModel(tModel);
    }

    private void marcarPrescenca(int row) {
        this.sgt.marcarPresenca((String) jTablePresencas.getValueAt(row,0),
                                this.uc,
                                shiftFromString(this.turno),
                                this.aula,
                                shiftTypeFromStr(this.turno));
        System.out.println("Marcar Presenca on row: "+row);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelUCs = new javax.swing.JLabel();
        jComboBoxUCs = new javax.swing.JComboBox<>();
        jLabelTurnos = new javax.swing.JLabel();
        jComboBoxTurnos = new javax.swing.JComboBox<>();
        jLabelAulas = new javax.swing.JLabel();
        jComboBoxAula = new javax.swing.JComboBox<>();
        jScrollPanePresencas = new javax.swing.JScrollPane();
        jTablePresencas = new javax.swing.JTable();
        jButtonAdicionarAula = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelUCs.setText("UCs:");

        jComboBoxUCs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxUCsActionPerformed(evt);
            }
        });

        jLabelTurnos.setText("Turnos");

        jComboBoxTurnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTurnosActionPerformed(evt);
            }
        });

        jLabelAulas.setText("Aulas");

        jComboBoxAula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAulaActionPerformed(evt);
            }
        });

        jTablePresencas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Nome", "Presenca"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePresencas.setColumnSelectionAllowed(true);
        jTablePresencas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePresencasMouseClicked(evt);
            }
        });
        jScrollPanePresencas.setViewportView(jTablePresencas);
        jTablePresencas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jButtonAdicionarAula.setText("Adicionar Aula");
        jButtonAdicionarAula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdicionarAulaActionPerformed(evt);
            }
        });

        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonFechar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelUCs)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jComboBoxUCs, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxTurnos, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelTurnos))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBoxAula, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonAdicionarAula))
                                    .addComponent(jLabelAulas))))
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPanePresencas, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(32, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAulas)
                    .addComponent(jLabelTurnos)
                    .addComponent(jLabelUCs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdicionarAula)
                    .addComponent(jComboBoxTurnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxUCs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPanePresencas, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonFechar)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAdicionarAulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdicionarAulaActionPerformed
        this.aula = this.sgt.addAula(this.uc,shiftFromString(this.turno),shiftTypeFromStr(this.turno));
        initComboBoxAulas();
    }//GEN-LAST:event_jButtonAdicionarAulaActionPerformed

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonFecharActionPerformed

    private void jComboBoxAulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAulaActionPerformed
        String aula = (String) this.jComboBoxAula.getSelectedItem();
        if(aula!=null){
            this.aula = Integer.parseInt(aula);
            initPresencas();
        }else {
            ((DefaultTableModel) this.jTablePresencas.getModel()).setRowCount(0);
        }
    }//GEN-LAST:event_jComboBoxAulaActionPerformed

    private void jComboBoxTurnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTurnosActionPerformed
        this.turno = (String) this.jComboBoxTurnos.getSelectedItem();
        initComboBoxAulas();
    }//GEN-LAST:event_jComboBoxTurnosActionPerformed

    private void jComboBoxUCsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUCsActionPerformed
        this.uc = (String) this.jComboBoxUCs.getSelectedItem();
        initComboBoxTurnos();
    }//GEN-LAST:event_jComboBoxUCsActionPerformed

    private void jTablePresencasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePresencasMouseClicked
        int selRow = this.jTablePresencas.getSelectedRow();
        String aluno = (String) this.jTablePresencas.getValueAt(selRow,0);
//        this.sgt.marcarPresenca(aluno,this.uc,shiftFromString(this.turno),this.aula,shiftTypeFromStr(this.turno));
        System.out.println(selRow+","+this.jTablePresencas.getSelectedColumn());
        System.out.println(this.jTablePresencas.getValueAt(selRow,2));
    }//GEN-LAST:event_jTablePresencasMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JDocenteMarcarPresencas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDocenteMarcarPresencas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDocenteMarcarPresencas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDocenteMarcarPresencas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JDocenteMarcarPresencas(new SGT()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdicionarAula;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JComboBox<String> jComboBoxAula;
    private javax.swing.JComboBox<String> jComboBoxTurnos;
    private javax.swing.JComboBox<String> jComboBoxUCs;
    private javax.swing.JLabel jLabelAulas;
    private javax.swing.JLabel jLabelTurnos;
    private javax.swing.JLabel jLabelUCs;
    private javax.swing.JScrollPane jScrollPanePresencas;
    private javax.swing.JTable jTablePresencas;
    // End of variables declaration//GEN-END:variables

    class ButtonRenderer extends JButton implements TableCellRenderer, TableCellEditor {

        private int selectedRow;
        private int selectedColumn;
        private List<TableButtonListener> listener;

        ButtonRenderer() {
            super();
            this.listener = new ArrayList<>();
            addActionListener(new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    for(TableButtonListener l : listener) {
                        l.tableButtonClicked(selectedRow, selectedColumn);
                    }
                }
            });
            setOpaque(true);
        }

        void addTableButtonListener(TableButtonListener l) {
            this.listener.add(l);
        }

        void removeTableButtonListener( TableButtonListener l ) {
            this.listener.remove(l);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }

        @Override
        public Component getTableCellEditorComponent(JTable jTable, Object o, boolean b, int row, int col) {
            this.selectedRow = row;
            this.selectedColumn = col;
            return this;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean isCellEditable(EventObject eventObject) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject eventObject) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public void cancelCellEditing() {

        }

        @Override
        public void addCellEditorListener(CellEditorListener cellEditorListener) {

        }

        @Override
        public void removeCellEditorListener(CellEditorListener cellEditorListener) {

        }
    }

    public interface TableButtonListener extends EventListener {
        void tableButtonClicked(int row, int col);
    }

/*
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.userInterface;

/**
 *
 * @author pedro
 */
public class AlunoEspecial extends javax.swing.JFrame {

    /**
     * Creates new form AlunoEspecial
     */
    public AlunoEspecial() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonEscolherUCs = new javax.swing.JButton();
        jScrollPaneUCsETurnos = new javax.swing.JScrollPane();
        jTableUCsETurnos = new javax.swing.JTable();
        jButtonPedirTroca = new javax.swing.JButton();
        jScrollPanePropsTroca = new javax.swing.JScrollPane();
        jTable2PropsTroca = new javax.swing.JTable();
        jButtonMudarDeTurno = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonEscolherUCs.setText("Escolher UCs");
        jButtonEscolherUCs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEscolherUCsActionPerformed(evt);
            }
        });

        jTableUCsETurnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UCs:", "Turnos:"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneUCsETurnos.setViewportView(jTableUCsETurnos);

        jButtonPedirTroca.setText("Pedir Troca");
        jButtonPedirTroca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPedirTrocaActionPerformed(evt);
            }
        });

        jTable2PropsTroca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Propostas de Troca"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPanePropsTroca.setViewportView(jTable2PropsTroca);

        jButtonMudarDeTurno.setText("Mudar de turno");
        jButtonMudarDeTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMudarDeTurnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonEscolherUCs)
                    .addComponent(jScrollPaneUCsETurnos, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(98, 98, 98)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPanePropsTroca, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(185, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonPedirTroca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonMudarDeTurno)
                        .addGap(32, 32, 32))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonEscolherUCs)
                    .addComponent(jButtonPedirTroca)
                    .addComponent(jButtonMudarDeTurno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPaneUCsETurnos, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(jScrollPanePropsTroca, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(61, 61, 61))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEscolherUCsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEscolherUCsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEscolherUCsActionPerformed

    private void jButtonPedirTrocaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPedirTrocaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPedirTrocaActionPerformed

    private void jButtonMudarDeTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMudarDeTurnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonMudarDeTurnoActionPerformed

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
            java.util.logging.Logger.getLogger(AlunoEspecial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlunoEspecial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlunoEspecial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlunoEspecial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AlunoEspecial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEscolherUCs;
    private javax.swing.JButton jButtonMudarDeTurno;
    private javax.swing.JButton jButtonPedirTroca;
    private javax.swing.JScrollPane jScrollPanePropsTroca;
    private javax.swing.JScrollPane jScrollPaneUCsETurnos;
    private javax.swing.JTable jTable2PropsTroca;
    private javax.swing.JTable jTableUCsETurnos;
    // End of variables declaration//GEN-END:variables
}

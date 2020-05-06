/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package agenday.views;

import agenday.Absen;
import agenday.Main_AgenDay;
import agenday.Siswa;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author swan
 */
public class V_Absensi extends javax.swing.JFrame {
    /**
     * Creates new form LayoutAbsensi
     */
//    DefaultTableModel dtm;
    V_AbsensiTabelModel model;
    
    public V_Absensi() {
        initComponents();
        Main_AgenDay.koneksi();
        showData();
    }
    
    public void showData(){
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String nama_kelas = Main_AgenDay.kelas.getNamaKelas();
            
            String query = "SELECT * FROM data_absen WHERE nama_kelas = '" + nama_kelas + "'";
            ResultSet rs = stmt.executeQuery(query);
            model = new V_AbsensiTabelModel();
            int no = 0;
            while (rs.next()){
                Main_AgenDay.siswa = new Siswa();
                Main_AgenDay.absen[no] = new Absen();
                Absen absen = new Absen();
                
                Main_AgenDay.siswa.setNoabsen(no+1);
                Main_AgenDay.siswa.setNIS(rs.getString("nis"));
                Main_AgenDay.siswa.setNama(rs.getString("nama"));
                Main_AgenDay.siswa.setJenisKelamin(rs.getString("jk"));
                Main_AgenDay.siswa.setSakit(false);
                Main_AgenDay.siswa.setIzin(false);
                Main_AgenDay.siswa.setAlpha(false);
                Main_AgenDay.absen[no].setSakit(rs.getInt("sakit"));
                Main_AgenDay.absen[no].setIzin(rs.getInt("izin"));
                Main_AgenDay.absen[no].setAlfa(rs.getInt("alpha"));
                
                model.add(Main_AgenDay.siswa, absen);
                no++;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        tblSiswa.setModel(model);
        baris = model.getRowCount();
    }
    
    int baris;
    
    public void simpanData(){
        String[] query = new String[baris];
        int[] sakit = new int[baris];
        int[] izin = new int[baris];
        int[] alpha = new int[baris];
        String[] nis = new String[baris];
        int berhasil = 0;
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            for(int a=0; a < baris; a++){
                nis[a] = model.list.get(a).getNIS();
                sakit[a] = model.absen.get(a).getSakit() + Main_AgenDay.absen[a].getSakit();
                izin[a] = model.absen.get(a).getIzin() + Main_AgenDay.absen[a].getIzin();
                alpha[a] = model.absen.get(a).getAlfa() + Main_AgenDay.absen[a].getAlfa();
                
                query[a] = "UPDATE absensi SET sakit = '"+sakit[a]+"',"
                    +"izin = '"+izin[a]+"',"
                    +"alpha = '"+alpha[a]+"' WHERE id_absen = substr("+nis[a]+", -3, 3 )";
                
                berhasil = stmt.executeUpdate(query[a]);
                System.out.println(query[a]);
            }
            
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            }
            else{
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada database");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        home = new java.awt.Button();
        Simpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 0, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel2.setText("Absensi");

        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "NIS", "Nama", "JK", "Sakit", "Izin", "Alpha"
            }
        ));
        jScrollPane1.setViewportView(tblSiswa);

        home.setLabel("Home");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        Simpan.setText("Simpan");
        Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        // TODO add your handling code here:
        V_Menu menu = new V_Menu();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_homeActionPerformed

    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        V_Menu menu = new V_Menu();
        menu.setVisible(true);
        simpanData();
    }//GEN-LAST:event_SimpanActionPerformed

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
            java.util.logging.Logger.getLogger(V_Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Absensi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Simpan;
    private java.awt.Button home;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tblSiswa;
    // End of variables declaration//GEN-END:variables
}

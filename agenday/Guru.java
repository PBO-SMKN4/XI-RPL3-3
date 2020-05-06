/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

import agenday.views.V_Agenda;
import agenday.views.V_Login;
import agenday.views.V_Menu;
import agenday.views.V_TambahAgenda;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author RamdanRohendi
 */
public class Guru extends WargaSekolah implements Pengajar{
    private String NIP;
    private String mapel;
    
    @Override
    public void setNIP(String NIP) {
        this.NIP = NIP;
    }
    
    @Override
    public String getNIP() {
        return NIP;
    }
    
    @Override
    public void setMapel(String mapel) {
        this.mapel = mapel;
    }
    
    @Override
    public String getMapel() {
        return mapel;
    }

    @Override
    public void setNama(String Nama) {
        super.setNama(Nama);
    }

    @Override
    public String getNama() {
        return super.getNama();
    }

    @Override
    public void setTanggalahir(String Tanggalahir) {
        super.setTanggalahir(Tanggalahir);
    }

    @Override
    public String getTanggalahir() {
        return super.getTanggalahir();
    }

    @Override
    public void setTempatLahir(String TempatLahir) {
        super.setTempatLahir(TempatLahir);
    }

    @Override
    public String getTempatLahir() {
        return super.getTempatLahir();
    }
    
    @Override
    public void Mengabsen() {}
    
    //Dibuat oleh Zahy Habibi dibantu PM
    //Method untuk mengisi data agenda ke database
    @Override
    public void MengisiAgenda(javax.swing.JLabel txtTgl, javax.swing.JTextArea txtCttn) {
        String nip = V_Login.login.getUsername();
        String kd_mapel = "mapel";
        String tanggal = txtTgl.getText();
        String id_kelas = "kelas";
        String cttn = txtCttn.getText();
        String hari = Main_AgenDay.hari_sekarang();
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String queryguru = "SELECT * FROM guru WHERE NIP = '"+nip+"'";
            String queryajar = "SELECT * FROM jdwl_ajar WHERE NIP = '"+nip+"' AND hari = '"+hari+"'";
            ResultSet rsg = stmt.executeQuery(queryguru);
            while(rsg.next()){
                kd_mapel = rsg.getString("kd_mapel");
            }
            ResultSet rsa = stmt.executeQuery(queryajar);
            while(rsa.next()){
                id_kelas = rsa.getString("id_kelas");
            }
            
            String query = "INSERT INTO agenday (id_kelas,kd_mapel,tanggal,cttn)" + "VALUES"
                    + "('"+id_kelas+"','"+kd_mapel+"','"+tanggal+"','"+cttn+"')";
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null,"Data Behasil Masuk");
            }else{
                JOptionPane.showMessageDialog(null,"Data Gagal Dimasukan");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Terjadi Kesalahan Pada Database");
        }
    }

    @Override
    public void setTugas(String Tugas) {
        super.setTugas(Tugas);
    }

    @Override
    public String getTugas() {
        return super.getTugas();
    }
    
    //Fungsi untuk mengecek apakah sudah mengisi form agenda atau belum pada saat itu
    @Override
    public void cekmengisi(V_Menu menu) {
        V_TambahAgenda tAgenda = new V_TambahAgenda();
        V_Agenda agenda = new V_Agenda();
        
        String kelas = Main_AgenDay.kelas.getNamaKelas();
        String hari = Main_AgenDay.tanggal_sekarang();
        String nip = Main_AgenDay.guru.getNIP();
        
        try {
            Statement stmt = Main_AgenDay.con.createStatement();
            String query = "SELECT * FROM data_agenda WHERE NIP = '" + nip + "' AND tanggal = '" + hari + "' AND nama_kelas = '" + kelas + "'";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                if(nip.equals(rs.getString("NIP")) && hari.equals(rs.getString("tanggal")) && kelas.equals(rs.getString("nama_kelas"))){
                    menu.setVisible(false);
                    tAgenda.setVisible(false);
                    agenda.setVisible(true);
                }
            }
            else{
                menu.setVisible(false);
                tAgenda.setVisible(true);
                agenda.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

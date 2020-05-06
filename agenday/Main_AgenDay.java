/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

import agenday.koneksi.koneksi;
import agenday.views.V_Login;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LENOVO
 */
public class Main_AgenDay {
    
    public static void main(String[] args) {
        login.setVisible(true);
    }
    
    public static Connection con;
    public static void koneksi(){
        con = koneksi.getKoneksi("localhost","3306","root","","agenday");
    }
    
    public static void namaKelas(String namakls){
        kelas.setNamaKelas(namakls);
    }
    
    public static V_Login login = new V_Login();
    public static WargaSekolah siswa = new Siswa();
    public static WargaSekolah guru = new Guru();
    public static Kelas kelas = new Kelas();
    public static Absen[] absen = new Absen[40];
    
    public static void tanggal_sekarang(javax.swing.JLabel tgl){
        Calendar cal = new GregorianCalendar();
        
        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH);
        int tahun = cal.get(Calendar.YEAR);
        String nolb = "0";
        String nolh = "0";
        
        if((bulan+1) >= 10) nolb = "";
        if(hari >= 10) nolh = "";
        tgl.setText(tahun + "-" + nolb + (bulan+1) + "-" + nolh + hari);
        
    }
    
    public static String tanggal_sekarang(){
        Calendar cal = new GregorianCalendar();
        
        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH);
        int tahun = cal.get(Calendar.YEAR);
        String nolb = "0";
        String nolh = "0";
        String tanggal;
        
        if((bulan+1) >= 10) nolb = "";
        if(hari >= 10) nolh = "";
        tanggal = tahun + "-" + nolb + (bulan+1) + "-" + nolh + hari;
        
        return tanggal;
    }
    
    public static void jam_sekarang(javax.swing.JLabel waktu){
        Thread p = new Thread (){
            @Override
            public void run(){
                for(;;){
                    Calendar cal = new GregorianCalendar();

                    int jam = cal.get (Calendar.HOUR);
                    int menit = cal.get(Calendar.MINUTE);
                    int detik= cal.get(Calendar.SECOND);
                    int AM_PM = cal.get(Calendar.AM_PM);

                    String day_night = "";
                    if(AM_PM == 1)
                    {
    //                 day_night = "PM";
                     int jamku = jam + 12;
                     jam = jamku;
                    }
                    else
                    {
    //                 day_night = "AM";
                    }
                    String nolm = "0";
                    String nold = "0";
                    if(menit >= 10) nolm = "";
                    if(detik >= 10) nold = "";
                    String waktuku = jam + ":" + nolm + menit + ":" + nold + detik + day_night;
                    waktu.setText(waktuku);

                    try{
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(agenday.views.V_Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        p.start(); 
    }
    
    public static String hari_sekarang(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        String hari = day.format(cal.getTime());
        
        return hari;
    }
}

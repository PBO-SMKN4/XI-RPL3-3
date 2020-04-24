/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

/**
 *
 * @author LENOVO
 */
public class Guru extends WargaSekolah implements Pengajar{
    private String NIP;
    private String mapel;

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public String getNIP() {
        return NIP;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

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
    
    @Override
    public void Pelajaran() {}

    @Override
    public void Tugas() {}
    
}

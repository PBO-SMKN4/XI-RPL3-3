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
public class WargaSekolah {
    private String Nama;
    private String TempatLahir;
    private String Tanggalahir;
    private String JenisKelamin;

    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    public String getTempatLahir() {
        return TempatLahir;
    }

    public void setTempatLahir(String TempatLahir) {
        this.TempatLahir = TempatLahir;
    }

    public String getTanggalahir() {
        return Tanggalahir;
    }

    public void setTanggalahir(String Tanggalahir) {
        this.Tanggalahir = Tanggalahir;
    }

    public String getJenisKelamin() {
        return JenisKelamin;
    }

    public void setJenisKelamin(String JenisKelamin) {
        this.JenisKelamin = JenisKelamin;
    }
    
    public void setNoabsen(int Noabsen) {}

    public void setNIS(String nis) {}
    public String getNIS(){return null;}
    public void setAlpha(Boolean alpha) {}
    public Boolean getAlpha() {return null;}
    public void setIzin(Boolean izin) {}
    public Boolean getIzin() {return null;}
    public void setSakit(Boolean sakit) {}
    public Boolean getSakit() {return null;}
}

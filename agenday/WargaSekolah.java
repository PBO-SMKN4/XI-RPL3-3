/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

/**
 *
 * @author RamdanRohendi
 */
public class WargaSekolah {
    private String Nama;
    private String Tugas;
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
    
    public void setNIP(String NIP) {}
    public String getNIP() {return null;}
    public void setMapel(String mapel) {}
    public String getMapel() {return null;}
    public Boolean cekmengisi(String nip, String kelas, String hari){return null;};
    public void MengisiAgenda(javax.swing.JLabel txtTgl, javax.swing.JTextArea txtCttn){}
    public void setTugas(String Tugas) {
        this.Tugas = Tugas;
    }

    public String getTugas() {
        return Tugas;
    }
}

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
public class Siswa extends WargaSekolah{
    private String NIS;
    private Boolean sakit;
    private Boolean izin;
    private Boolean alpha;

    
    @Override
    public void setAlpha(Boolean alpha) {
        this.alpha = alpha;
    }

    @Override
    public Boolean getAlpha() {
        return alpha;
    }

    @Override
    public void setIzin(Boolean izin) {
        this.izin = izin;
    }

    @Override
    public Boolean getIzin() {
        return izin;
    }

    @Override
    public void setSakit(Boolean sakit) {
        this.sakit = sakit;
    }

    @Override
    public Boolean getSakit() {
        return sakit;
    }
    
    @Override
    public String getNIS() {
        return NIS;
    }

    @Override
    public void setNIS(String NIS) {
        this.NIS = NIS;
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
    public void setJenisKelamin(String JenisKelamin) {
        super.setJenisKelamin(JenisKelamin); 
    }

    @Override
    public String getJenisKelamin() {
        return super.getJenisKelamin(); 
    }
    
}

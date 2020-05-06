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
public class Kelas {
    private String NamaKelas;
    private String Jurusan;

    public void setNamaKelas(String NamaKelas) {
        this.NamaKelas = NamaKelas;
    }

    public String getNamaKelas() {
        return NamaKelas;
    }

    public void setJurusan(String Jurusan) {
        this.Jurusan = Jurusan;
    }

    public String getJurusan() {
        return Jurusan;
    }
    
    public void DaftarMurid(String namaKelas){};
}

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
public interface Pengajar {
    String tugas = "Mengerjakan Latihan";
    
    void Mengabsen();
    void MengisiAgenda(javax.swing.JLabel txtTgl, javax.swing.JTextArea txtCttn);
    void setTugas(String tugas);
}

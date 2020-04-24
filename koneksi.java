/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

import java.sql.DriverManager;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;


/**
 *
 * @author daniar
 */
public class koneksi {
    
    public static Connection getKoneksi(String host, String port, String username, String password, String db) {
			String konString = "jdbc:mysql://" + host + ":" + port + "/" +db;
			Connection koneksi = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				koneksi = (Connection) DriverManager.getConnection(konString, username, password);
				System.out.println("Koneksi Berhasil");
			}catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Koneksi Database Error!");
				koneksi = null;
			}
			return koneksi;
	}
    
    
}

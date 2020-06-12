/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday;

import agenday.views.V_Login;
import agenday.views.V_Menu;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author RamdanRohendi
 */
public class Login {
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
    //Dibuat oleh Zahy Habibi
    //Fungsi untuk mengecek login/validasi data
    public void cekLogin(String User, String Pass, V_Login Vlogin){
        try {
            Statement stmt = Main_AgenDay.con.createStatement();
            String sql = "SELECT * FROM login WHERE username='"+User+"' AND paswword='"+Pass+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                if(User.equals(rs.getString("username")) && Pass.equals(rs.getString("paswword"))){
                    JOptionPane.showMessageDialog(null, "Berhasil Log-in");
                    V_Menu menu = new V_Menu();
                    setUsername(User);
                    menu.setVisible(true);
                    Vlogin.setVisible(false);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Log-in gagal");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Vlogin, e.getMessage());
        }
    }
}

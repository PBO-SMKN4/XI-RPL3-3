/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenday.views;

import agenday.Absen;
import agenday.Main_AgenDay;
import agenday.Siswa;
import static java.lang.String.valueOf;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author M Ervan Nugraha
 */
public class V_Menu extends javax.swing.JFrame {

    /**
     * Creates new form ViewApp
     */
    DefaultListModel mAjar = new DefaultListModel();
    DefaultListModel mJamP = new DefaultListModel();
    V_AbsensiTabelModel model;
    
    public V_Menu() {
        initComponents();
        
        TampilDataJadwal();
        dataKelas();
        Main_AgenDay.koneksi();
        Main_AgenDay.tanggal_sekarang(txtTglJdwl);
        Main_AgenDay.tanggal_sekarang(txtTglTAgenda);
        Main_AgenDay.tanggal_sekarang(txtTgltdy);
    }
    
    public void TampilDataJadwal(){
        Main_AgenDay.guru.setNIP(Main_AgenDay.login.getUser());
        String hari = Main_AgenDay.hari_sekarang();
        String nip = Main_AgenDay.guru.getNIP();
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            
            String query = "SELECT * FROM jadwal_ajar WHERE NIP = " + nip +" AND hari = '" + hari + "'" ;
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()){
                Main_AgenDay.guru.setNama(rs.getString("nama_guru"));
                Main_AgenDay.guru.setMapel(rs.getString("nama_mapel"));
                mAjar.addElement("- " + rs.getString("ngajar_kls"));   
                mJamP.addElement("- " + rs.getString("jam_pelajaran"));
            }
            
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        txtNama.setText(Main_AgenDay.guru.getNama());
        txtMapelJdwl.setText(Main_AgenDay.guru.getMapel());
        listAjar.setModel(mAjar);
        listJamPjdwl.setModel(mJamP);
    }
    
    
    
    
    public void showDataSiswa(){
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String nama_kelas = Main_AgenDay.kelas.getNamaKelas();
            
            String query = "SELECT * FROM data_absen WHERE nama_kelas = '" + nama_kelas + "'";
            ResultSet rs = stmt.executeQuery(query);
            model = new V_AbsensiTabelModel();
            int no = 0;
            while (rs.next()){
                Main_AgenDay.siswa = new Siswa();
                Main_AgenDay.absen[no] = new Absen();
                Absen absen = new Absen();
                
                Main_AgenDay.siswa.setNoabsen(no+1);
                Main_AgenDay.siswa.setNIS(rs.getString("nis"));
                Main_AgenDay.siswa.setNama(rs.getString("nama"));
                Main_AgenDay.siswa.setJenisKelamin(rs.getString("jk"));
                Main_AgenDay.siswa.setSakit(false);
                Main_AgenDay.siswa.setIzin(false);
                Main_AgenDay.siswa.setAlpha(false);
                Main_AgenDay.absen[no].setSakit(rs.getInt("sakit"));
                Main_AgenDay.absen[no].setIzin(rs.getInt("izin"));
                Main_AgenDay.absen[no].setAlfa(rs.getInt("alpha"));
                
                model.add(Main_AgenDay.siswa, absen);
                no++;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        tblSiswa.setModel(model);
        baris = model.getRowCount();
    }
    
    int baris;
    
    public void simpanDataAbsen(){
        String[] query = new String[baris];
        int[] sakit = new int[baris];
        int[] izin = new int[baris];
        int[] alpha = new int[baris];
        String[] nis = new String[baris];
        int berhasil = 0;
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            for(int a=0; a < baris; a++){
                nis[a] = model.list.get(a).getNIS();
                sakit[a] = model.absen.get(a).getSakit() + Main_AgenDay.absen[a].getSakit();
                izin[a] = model.absen.get(a).getIzin() + Main_AgenDay.absen[a].getIzin();
                alpha[a] = model.absen.get(a).getAlfa() + Main_AgenDay.absen[a].getAlfa();
                
                query[a] = "UPDATE absensi SET sakit = '"+sakit[a]+"',"
                    +"izin = '"+izin[a]+"',"
                    +"alpha = '"+alpha[a]+"' WHERE id_absen = substr("+nis[a]+", -3, 3 )";
                
                berhasil = stmt.executeUpdate(query[a]);
//                System.out.println(query[a]);
            }
            
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            }
            else{
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada database");
        }
    }
    
    
    
    
    DefaultTableModel dtm;
    public void showDataRekab(){
        String [] kolom = {"No", "NIS", "Nama", "JK", "Sakit","Izin","Alpha","Jumlah Kehadiran","Persentase Kehadiran"};
        dtm = new DefaultTableModel(null, kolom);
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String nama_kelas = Main_AgenDay.kelas.getNamaKelas();
            
            String query = "SELECT * FROM data_absen WHERE nama_kelas = '" + nama_kelas + "'";
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()){
                String nis = rs.getString("nis");
                String nama = rs.getString("nama");
                String jk = rs.getString("jk");
                String sakit = rs.getString("sakit");
                String izin = rs.getString("izin");
                String alpha = rs.getString("alpha");
                int isakit = rs.getInt("sakit");
                int iizin = rs.getInt("izin");
                int ialpha = rs.getInt("alpha");
                
                dtm.addRow(new String[] {no + ". ", nis, nama, jk, sakit, izin, alpha, valueOf(jmlhkehadiran(isakit, iizin, ialpha)), persentase() });
                no++;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        tblRekap.setModel(dtm);
    }
    
    int jmlharipembljrn = 135;
    int jmlhadir;
    float persen;
    
    public int jmlhkehadiran(int s, int i, int a) {
        
        jmlhadir = (int) (jmlharipembljrn-( s + i + a ));
        
        return jmlhadir;
    }
    
    public String persentase() {
        
        persen = (float) (jmlhadir)/(jmlharipembljrn)*(100);
        
        
        return valueOf(persen) + "%";
    }
    
    
    
    public void dataKelas(){
        Main_AgenDay.guru.setNIP(Main_AgenDay.login.getUser());
        String nip = Main_AgenDay.guru.getNIP();
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String query = "SELECT * FROM jadwal_ajar WHERE NIP = '" + nip + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                if(!rs.getString("ngajar_kls").equals("Tidak Ada")){
                    cmbPilihKelas.addItem(rs.getString("ngajar_kls"));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    
    
    public void showdataTAgenda(){
        String kelas = Main_AgenDay.kelas.getNamaKelas();
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String nip = Main_AgenDay.guru.getNIP();
            String query = "SELECT * FROM data_agenda WHERE NIP = '" + nip + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                Main_AgenDay.guru.setNama(rs.getString("nama_guru"));
                Main_AgenDay.guru.setMapel(rs.getString("nama_mapel"));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        txtMapel.setText(Main_AgenDay.guru.getMapel());
        txtGuru.setText(Main_AgenDay.guru.getNama());
        txtKlsTAgenda.setText(kelas);
    }
    
    
    
    public void showDatahadir(){
        String [] kolom = {"No","Nama", "Keterangan"};
        dtm = new DefaultTableModel(null, kolom);
        
        int no = 1;
        int jumlah = Main_AgenDay.absen.length;
        
//        for (int i = 0; i < jumlah; i++) {
//            Boolean hadir = Main_AgenDay.absen[0].getHadir();
//            if(hadir == true){
                String Nama = Main_AgenDay.siswa.getNama();
//                String Keterangan = Main_AgenDay.absen[0].getKeterangan();
                
//                dtm.addRow(new String[] {no + ". ", Nama, Keterangan });
//                no++;
//            }
//        }
        tbltdkhadir.setModel(dtm);
    }
    
    public void showdataAgenda(){
        String kelas = Main_AgenDay.kelas.getNamaKelas();
        String hari = Main_AgenDay.tanggal_sekarang();
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String nip = Main_AgenDay.guru.getNIP();
            String query = "SELECT * FROM data_agenda WHERE NIP = '" + nip + "' AND tanggal = '" + hari + "' AND nama_kelas = '" + kelas + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                Main_AgenDay.guru.setNama(rs.getString("nama_guru"));
                Main_AgenDay.guru.setMapel(rs.getString("nama_mapel"));
                Main_AgenDay.guru.setTugas(rs.getString("cttn"));
            }
            
            txtMapeltdy.setText(Main_AgenDay.guru.getMapel());
            txtGurutdy.setText(Main_AgenDay.guru.getNama());
            txtKlstdy.setText(kelas);
            txtCttntdy.setText(Main_AgenDay.guru.getTugas());
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }
    
    
    
    
    public Boolean CekPilihanKelas() {
        String nip = Main_AgenDay.login.getUser();
        String kelas = Main_AgenDay.kelas.getNamaKelas();
        String hari = Main_AgenDay.hari_sekarang();
        Boolean cek = false;
        
        try{
            Statement stmt = Main_AgenDay.con.createStatement();
            String query = "SELECT * FROM jadwal_ajar WHERE NIP = " + nip +" AND hari = '" + hari + "'" ;
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                if(kelas.equals(rs.getString("ngajar_kls"))){
                    cek = true;
                }
                else{
                    cek = false;
                }
            }
            else{
                cek = false;
            }
            
        }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Terjadi Kesalahan Pada Database");
        }
        
        return cek;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMenu = new javax.swing.JPanel();
        Top = new javax.swing.JPanel();
        btnLogout = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        ApkName = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        btnAgenda = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        btnJadwal = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        btnPersentasi = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        Tampilan = new javax.swing.JPanel();
        PanelJadwal = new javax.swing.JPanel();
        lblNmguru = new javax.swing.JLabel();
        lblJdwl = new javax.swing.JLabel();
        lblAjar = new javax.swing.JLabel();
        lblMapeljdwl = new javax.swing.JLabel();
        lblTgljdwl = new javax.swing.JLabel();
        lblJamP = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAjar = new javax.swing.JList<String>();
        jScrollPane3 = new javax.swing.JScrollPane();
        listJamPjdwl = new javax.swing.JList<String>();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtNama = new javax.swing.JLabel();
        txtMapelJdwl = new javax.swing.JLabel();
        txtTglJdwl = new javax.swing.JLabel();
        PanelAgenda = new javax.swing.JPanel();
        lblTgl = new javax.swing.JLabel();
        lblNmForm = new javax.swing.JLabel();
        lblMapel = new javax.swing.JLabel();
        lblNmGuru = new javax.swing.JLabel();
        lblKls = new javax.swing.JLabel();
        lblCttn = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCttn = new javax.swing.JTextArea();
        btnSubmitAgenda = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        txtMapel = new javax.swing.JLabel();
        txtGuru = new javax.swing.JLabel();
        txtTglTAgenda = new javax.swing.JLabel();
        txtKlsTAgenda = new javax.swing.JLabel();
        PanelPresentase = new javax.swing.JPanel();
        lblJangPer = new javax.swing.JLabel();
        lblPlih = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        lblKelas = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        cmbJkwn = new javax.swing.JComboBox<String>();
        cmbKelas = new javax.swing.JComboBox<String>();
        btnLihatRkp = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        PanelAbsensi = new javax.swing.JPanel();
        lblDataSiswa = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        btnSimpanabsen = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        DisplayAgenda = new javax.swing.JPanel();
        lblDaribpkibu = new javax.swing.JLabel();
        lblAgendatdy = new javax.swing.JLabel();
        lblmapeltdy = new javax.swing.JLabel();
        lblNkls = new javax.swing.JLabel();
        lbltgltdy = new javax.swing.JLabel();
        lblcttn = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lbltdkhadr = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbltdkhadir = new javax.swing.JTable();
        txtTgltdy = new javax.swing.JLabel();
        txtGurutdy = new javax.swing.JLabel();
        txtMapeltdy = new javax.swing.JLabel();
        txtKlstdy = new javax.swing.JLabel();
        txtCttntdy = new javax.swing.JLabel();
        PanelPilihKelas = new javax.swing.JPanel();
        lblPilihKelas = new javax.swing.JLabel();
        cmbPilihKelas = new javax.swing.JComboBox<String>();
        btnOk = new javax.swing.JButton();
        PanelRekap = new javax.swing.JPanel();
        lblRekap = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblRekap = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Top.setBackground(new java.awt.Color(102, 0, 51));

        btnLogout.setBackground(new java.awt.Color(102, 0, 51));
        btnLogout.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });

        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Logout");

        javax.swing.GroupLayout btnLogoutLayout = new javax.swing.GroupLayout(btnLogout);
        btnLogout.setLayout(btnLogoutLayout);
        btnLogoutLayout.setHorizontalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 99, Short.MAX_VALUE)
            .addGroup(btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLogoutLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel22)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnLogoutLayout.setVerticalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
            .addGroup(btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLogoutLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel22)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        ApkName.setBackground(new java.awt.Color(102, 0, 51));
        ApkName.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        ApkName.setForeground(new java.awt.Color(255, 255, 255));
        ApkName.setText("AGENDAY");

        javax.swing.GroupLayout TopLayout = new javax.swing.GroupLayout(Top);
        Top.setLayout(TopLayout);
        TopLayout.setHorizontalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ApkName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 615, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        TopLayout.setVerticalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ApkName)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelMenu.add(Top, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 60));

        Left.setBackground(new java.awt.Color(102, 0, 51));

        btnAgenda.setBackground(new java.awt.Color(102, 0, 51));
        btnAgenda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAgenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgendaMouseClicked(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agenday/Gambar/icons8-address-book-60.png"))); // NOI18N

        javax.swing.GroupLayout btnAgendaLayout = new javax.swing.GroupLayout(btnAgenda);
        btnAgenda.setLayout(btnAgendaLayout);
        btnAgendaLayout.setHorizontalGroup(
            btnAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnAgendaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel18))
        );
        btnAgendaLayout.setVerticalGroup(
            btnAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAgendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnJadwal.setBackground(new java.awt.Color(102, 0, 51));
        btnJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnJadwalMouseClicked(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agenday/Gambar/icons8-schedule-60.png"))); // NOI18N

        javax.swing.GroupLayout btnJadwalLayout = new javax.swing.GroupLayout(btnJadwal);
        btnJadwal.setLayout(btnJadwalLayout);
        btnJadwalLayout.setHorizontalGroup(
            btnJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnJadwalLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel19))
        );
        btnJadwalLayout.setVerticalGroup(
            btnJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnJadwalLayout.createSequentialGroup()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPersentasi.setBackground(new java.awt.Color(102, 0, 51));
        btnPersentasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPersentasiMouseClicked(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agenday/Gambar/icons8-percentage-60.png"))); // NOI18N

        javax.swing.GroupLayout btnPersentasiLayout = new javax.swing.GroupLayout(btnPersentasi);
        btnPersentasi.setLayout(btnPersentasiLayout);
        btnPersentasiLayout.setHorizontalGroup(
            btnPersentasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPersentasiLayout.createSequentialGroup()
                .addComponent(jLabel20)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btnPersentasiLayout.setVerticalGroup(
            btnPersentasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPersentasiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPersentasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnJadwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(btnJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btnPersentasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        panelMenu.add(Left, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 60, 440));

        Tampilan.setLayout(new java.awt.CardLayout());

        PanelJadwal.setBackground(new java.awt.Color(255, 255, 255));

        lblNmguru.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblNmguru.setText("Nama Guru");

        lblJdwl.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblJdwl.setText("Jadwal hari ini");

        lblAjar.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblAjar.setText("Mengajar Kelas");

        lblMapeljdwl.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblMapeljdwl.setText("Mata Pelajaran");

        lblTgljdwl.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblTgljdwl.setText("Tanggal");

        lblJamP.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblJamP.setText("Jam Pelajaran");

        listAjar.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listAjar);

        jScrollPane3.setViewportView(listJamPjdwl);

        jLabel43.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel43.setText(":");

        jLabel44.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel44.setText(":");

        jLabel45.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel45.setText(":");

        jLabel46.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel46.setText(":");

        jLabel47.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel47.setText(":");

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNama.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtNama.setText("Nama");
        txtNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMapelJdwl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMapelJdwl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtMapelJdwl.setText("Pelajaran");
        txtMapelJdwl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTglJdwl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTglJdwl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTglJdwl.setText("dd/mm/yyyy");
        txtTglJdwl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout PanelJadwalLayout = new javax.swing.GroupLayout(PanelJadwal);
        PanelJadwal.setLayout(PanelJadwalLayout);
        PanelJadwalLayout.setHorizontalGroup(
            PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelJadwalLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblJdwl)
                    .addGroup(PanelJadwalLayout.createSequentialGroup()
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNmguru)
                            .addComponent(lblTgljdwl)
                            .addComponent(lblMapeljdwl)
                            .addComponent(lblAjar)
                            .addComponent(lblJamP))
                        .addGap(55, 55, 55)
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel43))))
                .addGap(56, 56, 56)
                .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMapelJdwl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTglJdwl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        PanelJadwalLayout.setVerticalGroup(
            PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelJadwalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJdwl)
                .addGap(41, 41, 41)
                .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelJadwalLayout.createSequentialGroup()
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNmguru)
                            .addComponent(jLabel43)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAjar)
                                .addComponent(jLabel44))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMapeljdwl)
                            .addComponent(jLabel45)))
                    .addGroup(PanelJadwalLayout.createSequentialGroup()
                        .addComponent(txtMapelJdwl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(31, 31, 31)
                .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTgljdwl)
                    .addComponent(jLabel46)
                    .addComponent(txtTglJdwl, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelJadwalLayout.createSequentialGroup()
                        .addGroup(PanelJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblJamP)
                            .addComponent(jLabel47))
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        Tampilan.add(PanelJadwal, "card3");

        PanelAgenda.setBackground(new java.awt.Color(255, 255, 255));

        lblTgl.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblTgl.setText("Tanggal");

        lblNmForm.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblNmForm.setText("Form Agenda");

        lblMapel.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblMapel.setText("Mata Pelajaran");

        lblNmGuru.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblNmGuru.setText("Nama Guru");

        lblKls.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblKls.setText("Kelas");

        lblCttn.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblCttn.setText("Catatan/Tugas");

        jLabel28.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel28.setText(":");

        jLabel29.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel29.setText(":");

        jLabel30.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel30.setText(":");

        jLabel31.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel31.setText(":");

        jLabel32.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel32.setText(":");

        txtCttn.setColumns(20);
        txtCttn.setRows(5);
        jScrollPane1.setViewportView(txtCttn);

        btnSubmitAgenda.setBackground(new java.awt.Color(255, 255, 255));
        btnSubmitAgenda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSubmitAgenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubmitAgendaMouseClicked(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel33.setText("Submit");

        javax.swing.GroupLayout btnSubmitAgendaLayout = new javax.swing.GroupLayout(btnSubmitAgenda);
        btnSubmitAgenda.setLayout(btnSubmitAgendaLayout);
        btnSubmitAgendaLayout.setHorizontalGroup(
            btnSubmitAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
            .addGroup(btnSubmitAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSubmitAgendaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel33)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnSubmitAgendaLayout.setVerticalGroup(
            btnSubmitAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
            .addGroup(btnSubmitAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSubmitAgendaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel33)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        txtMapel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMapel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtMapel.setText("Mata Pelajaran");
        txtMapel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtGuru.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGuru.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGuru.setText("Nama Guru");
        txtGuru.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTglTAgenda.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTglTAgenda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTglTAgenda.setText("dd/mm/yyyy");
        txtTglTAgenda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtKlsTAgenda.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtKlsTAgenda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtKlsTAgenda.setText("Nama Kelas");
        txtKlsTAgenda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout PanelAgendaLayout = new javax.swing.GroupLayout(PanelAgenda);
        PanelAgenda.setLayout(PanelAgendaLayout);
        PanelAgendaLayout.setHorizontalGroup(
            PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAgendaLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMapel)
                    .addComponent(lblNmGuru)
                    .addComponent(lblTgl)
                    .addComponent(lblKls)
                    .addComponent(lblCttn))
                .addGap(64, 64, 64)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel32))
                .addGap(46, 46, 46)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(txtMapel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGuru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTglTAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtKlsTAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSubmitAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelAgendaLayout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(lblNmForm)
                    .addContainerGap(550, Short.MAX_VALUE)))
        );
        PanelAgendaLayout.setVerticalGroup(
            PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAgendaLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMapel)
                    .addComponent(jLabel28)
                    .addComponent(txtMapel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNmGuru)
                    .addComponent(jLabel29)
                    .addComponent(txtGuru, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTgl)
                    .addComponent(jLabel30)
                    .addComponent(txtTglTAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKls)
                    .addComponent(jLabel31)
                    .addComponent(txtKlsTAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCttn)
                        .addComponent(jLabel32))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAgendaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSubmitAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(PanelAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelAgendaLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(lblNmForm)
                    .addContainerGap(380, Short.MAX_VALUE)))
        );

        Tampilan.add(PanelAgenda, "card2");

        PanelPresentase.setBackground(new java.awt.Color(255, 255, 255));
        PanelPresentase.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJangPer.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblJangPer.setText("Jangkauan Persentase");
        PanelPresentase.add(lblJangPer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        lblPlih.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblPlih.setText("Pilih kategori rekap dan persentase");
        PanelPresentase.add(lblPlih, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel66.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel66.setText(":");
        PanelPresentase.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, -1, -1));

        lblKelas.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblKelas.setText("Kelas");
        PanelPresentase.add(lblKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel68.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel68.setText(":");
        PanelPresentase.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, -1, -1));

        cmbJkwn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semester 1", "Semester 2" }));
        PanelPresentase.add(cmbJkwn, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 260, 40));

        cmbKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XI AVI 1", "XI AVI 2", "XI AVI 3", "XI TITL 1", "XI TITL 2", "XI TOI 1", "XI TOI 2", "XI TKJ 1", "XI TKJ 2", "XI TKJ 3", "XI RPL 1", "XI RPL 2", "XI RPL 3", "XI MM" }));
        cmbKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKelasActionPerformed(evt);
            }
        });
        PanelPresentase.add(cmbKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 260, 40));

        btnLihatRkp.setBackground(new java.awt.Color(255, 255, 255));
        btnLihatRkp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnLihatRkp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLihatRkpMouseClicked(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel69.setText("Lihat rekap dan presentase");

        javax.swing.GroupLayout btnLihatRkpLayout = new javax.swing.GroupLayout(btnLihatRkp);
        btnLihatRkp.setLayout(btnLihatRkpLayout);
        btnLihatRkpLayout.setHorizontalGroup(
            btnLihatRkpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLihatRkpLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel69)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        btnLihatRkpLayout.setVerticalGroup(
            btnLihatRkpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLihatRkpLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel69)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        PanelPresentase.add(btnLihatRkp, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, 410, 80));

        Tampilan.add(PanelPresentase, "card4");

        PanelAbsensi.setBackground(new java.awt.Color(255, 255, 255));
        PanelAbsensi.setPreferredSize(new java.awt.Dimension(853, 440));

        lblDataSiswa.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblDataSiswa.setText("Data Siswa dan Absensi");

        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "NIS", "NAMA", "JK", "SAKIT", "IZIN", "ALPHA"
            }
        ));
        jScrollPane5.setViewportView(tblSiswa);

        btnSimpanabsen.setBackground(new java.awt.Color(255, 255, 255));
        btnSimpanabsen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSimpanabsen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanabsenMouseClicked(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel61.setText("SIMPAN");

        javax.swing.GroupLayout btnSimpanabsenLayout = new javax.swing.GroupLayout(btnSimpanabsen);
        btnSimpanabsen.setLayout(btnSimpanabsenLayout);
        btnSimpanabsenLayout.setHorizontalGroup(
            btnSimpanabsenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
            .addGroup(btnSimpanabsenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSimpanabsenLayout.createSequentialGroup()
                    .addGap(0, 15, Short.MAX_VALUE)
                    .addComponent(jLabel61)
                    .addGap(0, 15, Short.MAX_VALUE)))
        );
        btnSimpanabsenLayout.setVerticalGroup(
            btnSimpanabsenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
            .addGroup(btnSimpanabsenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSimpanabsenLayout.createSequentialGroup()
                    .addGap(0, 5, Short.MAX_VALUE)
                    .addComponent(jLabel61)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout PanelAbsensiLayout = new javax.swing.GroupLayout(PanelAbsensi);
        PanelAbsensi.setLayout(PanelAbsensiLayout);
        PanelAbsensiLayout.setHorizontalGroup(
            PanelAbsensiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAbsensiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelAbsensiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(PanelAbsensiLayout.createSequentialGroup()
                        .addComponent(lblDataSiswa)
                        .addGap(0, 360, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAbsensiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSimpanabsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelAbsensiLayout.setVerticalGroup(
            PanelAbsensiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAbsensiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataSiswa)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanabsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Tampilan.add(PanelAbsensi, "card5");

        DisplayAgenda.setBackground(new java.awt.Color(255, 255, 255));

        lblDaribpkibu.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblDaribpkibu.setText("Dari bpk/ibu");

        lblAgendatdy.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblAgendatdy.setText("Agenda Hari Ini");

        lblmapeltdy.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblmapeltdy.setText("Mata Pelajaran");

        lblNkls.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblNkls.setText("Kelas");

        lbltgltdy.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lbltgltdy.setText("Tanggal");

        lblcttn.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lblcttn.setText("Catatan/tugas");

        jLabel52.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel52.setText(":");

        jLabel53.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel53.setText(":");

        jLabel54.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel54.setText(":");

        jLabel55.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel55.setText(":");

        jLabel56.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel56.setText(":");

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbltdkhadr.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        lbltdkhadr.setText("Yang Tidak Hadir");

        tbltdkhadir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Nama", "Alasan"
            }
        ));
        jScrollPane6.setViewportView(tbltdkhadir);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lbltdkhadr)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltdkhadr)
                .addGap(50, 50, 50)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        txtTgltdy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTgltdy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTgltdy.setText("dd/mm/yyyy");
        txtTgltdy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtGurutdy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGurutdy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtGurutdy.setText("Nama Guru");
        txtGurutdy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMapeltdy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMapeltdy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtMapeltdy.setText("Nama Mapel");
        txtMapeltdy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtKlstdy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtKlstdy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtKlstdy.setText("Nama Kelas");
        txtKlstdy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtCttntdy.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCttntdy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtCttntdy.setText("Catatan dan Tugas");
        txtCttntdy.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        txtCttntdy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout DisplayAgendaLayout = new javax.swing.GroupLayout(DisplayAgenda);
        DisplayAgenda.setLayout(DisplayAgendaLayout);
        DisplayAgendaLayout.setHorizontalGroup(
            DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDaribpkibu)
                    .addComponent(lblmapeltdy)
                    .addComponent(lblNkls)
                    .addComponent(lbltgltdy)
                    .addComponent(lblcttn))
                .addGap(65, 65, 65)
                .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53)
                    .addComponent(jLabel54)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56))
                .addGap(34, 34, 34)
                .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTgltdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGurutdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMapeltdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtKlstdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCttntdy, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DisplayAgendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAgendatdy)
                .addGap(82, 529, Short.MAX_VALUE))
        );
        DisplayAgendaLayout.setVerticalGroup(
            DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblAgendatdy)
                .addGap(29, 29, 29)
                .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DisplayAgendaLayout.createSequentialGroup()
                        .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDaribpkibu)
                            .addComponent(jLabel52)
                            .addComponent(txtGurutdy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblmapeltdy)
                                .addComponent(jLabel53))
                            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                                .addComponent(txtMapeltdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3)))
                        .addGap(18, 18, 18)
                        .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(lblNkls)
                            .addComponent(txtKlstdy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbltgltdy)
                                .addComponent(jLabel55))
                            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                                .addComponent(txtTgltdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3)))
                        .addGap(18, 18, 18)
                        .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                                .addGroup(DisplayAgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblcttn)
                                    .addComponent(jLabel56))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE))
                            .addGroup(DisplayAgendaLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtCttntdy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        Tampilan.add(DisplayAgenda, "card6");

        PanelPilihKelas.setBackground(new java.awt.Color(255, 255, 255));

        lblPilihKelas.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblPilihKelas.setText("Silahkan Pilih Kelas");

        cmbPilihKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPilihKelasActionPerformed(evt);
            }
        });

        btnOk.setBackground(new java.awt.Color(255, 255, 255));
        btnOk.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelPilihKelasLayout = new javax.swing.GroupLayout(PanelPilihKelas);
        PanelPilihKelas.setLayout(PanelPilihKelasLayout);
        PanelPilihKelasLayout.setHorizontalGroup(
            PanelPilihKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPilihKelasLayout.createSequentialGroup()
                .addGroup(PanelPilihKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPilihKelasLayout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addGroup(PanelPilihKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPilihKelas)
                            .addComponent(cmbPilihKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelPilihKelasLayout.createSequentialGroup()
                        .addGap(318, 318, 318)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(245, Short.MAX_VALUE))
        );
        PanelPilihKelasLayout.setVerticalGroup(
            PanelPilihKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPilihKelasLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(lblPilihKelas)
                .addGap(30, 30, 30)
                .addComponent(cmbPilihKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );

        Tampilan.add(PanelPilihKelas, "card7");

        PanelRekap.setBackground(new java.awt.Color(255, 255, 255));

        lblRekap.setFont(new java.awt.Font("Century Gothic", 1, 40)); // NOI18N
        lblRekap.setText("Rekap Absensi");

        tblRekap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "NIS", "NAMA", "JK", "SAKIT", "IZIN", "ALPHA", "JML HADIR", "PRESENTASE KEHADIRAN"
            }
        ));
        jScrollPane7.setViewportView(tblRekap);

        javax.swing.GroupLayout PanelRekapLayout = new javax.swing.GroupLayout(PanelRekap);
        PanelRekap.setLayout(PanelRekapLayout);
        PanelRekapLayout.setHorizontalGroup(
            PanelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelRekapLayout.createSequentialGroup()
                        .addComponent(jScrollPane7)
                        .addContainerGap())
                    .addGroup(PanelRekapLayout.createSequentialGroup()
                        .addComponent(lblRekap)
                        .addGap(92, 543, Short.MAX_VALUE))))
        );
        PanelRekapLayout.setVerticalGroup(
            PanelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRekap)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        Tampilan.add(PanelRekap, "card8");

        panelMenu.add(Tampilan, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 840, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        V_Login login = new V_Login();
        this.setVisible(false);
        login.setVisible(true);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnAgendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgendaMouseClicked
        // TODO add your handling code here:
        showdataAgenda();
        showDatahadir();
        
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(PanelPilihKelas);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnAgendaMouseClicked

    private void btnJadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJadwalMouseClicked
        // TODO add your handling code here:
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(PanelJadwal);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnJadwalMouseClicked

    private void btnPersentasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPersentasiMouseClicked
        // TODO add your handling code here:
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(PanelPresentase);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnPersentasiMouseClicked

    private void btnSubmitAgendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitAgendaMouseClicked
        // TODO add your handling code here:
        Main_AgenDay.guru.MengisiAgenda(txtTglTAgenda, txtCttn);
        showdataAgenda();
        
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(DisplayAgenda);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnSubmitAgendaMouseClicked

    private void btnLihatRkpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLihatRkpMouseClicked
        // TODO add your handling code here:
        showDataRekab();
        
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(PanelRekap);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnLihatRkpMouseClicked

    private void btnSimpanabsenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanabsenMouseClicked
        // TODO add your handling code here:
        simpanDataAbsen();
        showdataTAgenda();
        
        Tampilan.removeAll();
        Tampilan.revalidate();
        Tampilan.add(PanelAgenda);
        Tampilan.repaint();
        Tampilan.revalidate();
    }//GEN-LAST:event_btnSimpanabsenMouseClicked

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        Main_AgenDay.guru.setNIP(Main_AgenDay.login.getUser());
        String nip = Main_AgenDay.guru.getNIP();
        String kelas = Main_AgenDay.kelas.getNamaKelas();
        String hari = Main_AgenDay.tanggal_sekarang();
        
        showDataSiswa();
        
        if(CekPilihanKelas() == true){
            if(Main_AgenDay.guru.cekmengisi(nip, kelas, hari) == true ){
                showdataAgenda();

                Tampilan.removeAll();
                Tampilan.revalidate();
                Tampilan.add(DisplayAgenda);
                Tampilan.repaint();
                Tampilan.revalidate();
            }
            else{
                Tampilan.removeAll();
                Tampilan.revalidate();
                Tampilan.add(PanelAbsensi);
                Tampilan.repaint();
                Tampilan.revalidate();
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Anda Tidak Mengajar Kelas ini di Hari Ini !!!");
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void cmbKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKelasActionPerformed
        // TODO add your handling code here:
        String text;
        
        text = (String)cmbKelas.getSelectedItem();
        Main_AgenDay.kelas.setNamaKelas(text);
    }//GEN-LAST:event_cmbKelasActionPerformed

    private void cmbPilihKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPilihKelasActionPerformed
        // TODO add your handling code here:
        String text;
        
        text = (String)cmbPilihKelas.getSelectedItem();
        Main_AgenDay.kelas.setNamaKelas(text);
    }//GEN-LAST:event_cmbPilihKelasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(V_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ApkName;
    private javax.swing.JPanel DisplayAgenda;
    private javax.swing.JPanel Left;
    private javax.swing.JPanel PanelAbsensi;
    private javax.swing.JPanel PanelAgenda;
    private javax.swing.JPanel PanelJadwal;
    private javax.swing.JPanel PanelPilihKelas;
    private javax.swing.JPanel PanelPresentase;
    private javax.swing.JPanel PanelRekap;
    private javax.swing.JPanel Tampilan;
    private javax.swing.JPanel Top;
    private javax.swing.JPanel btnAgenda;
    private javax.swing.JPanel btnJadwal;
    private javax.swing.JPanel btnLihatRkp;
    private javax.swing.JPanel btnLogout;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel btnPersentasi;
    private javax.swing.JPanel btnSimpanabsen;
    private javax.swing.JPanel btnSubmitAgenda;
    private javax.swing.JComboBox<String> cmbJkwn;
    private javax.swing.JComboBox<String> cmbKelas;
    private javax.swing.JComboBox<String> cmbPilihKelas;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblAgendatdy;
    private javax.swing.JLabel lblAjar;
    private javax.swing.JLabel lblCttn;
    private javax.swing.JLabel lblDaribpkibu;
    private javax.swing.JLabel lblDataSiswa;
    private javax.swing.JLabel lblJamP;
    private javax.swing.JLabel lblJangPer;
    private javax.swing.JLabel lblJdwl;
    private javax.swing.JLabel lblKelas;
    private javax.swing.JLabel lblKls;
    private javax.swing.JLabel lblMapel;
    private javax.swing.JLabel lblMapeljdwl;
    private javax.swing.JLabel lblNkls;
    private javax.swing.JLabel lblNmForm;
    private javax.swing.JLabel lblNmGuru;
    private javax.swing.JLabel lblNmguru;
    private javax.swing.JLabel lblPilihKelas;
    private javax.swing.JLabel lblPlih;
    private javax.swing.JLabel lblRekap;
    private javax.swing.JLabel lblTgl;
    private javax.swing.JLabel lblTgljdwl;
    private javax.swing.JLabel lblcttn;
    private javax.swing.JLabel lblmapeltdy;
    private javax.swing.JLabel lbltdkhadr;
    private javax.swing.JLabel lbltgltdy;
    private javax.swing.JList<String> listAjar;
    private javax.swing.JList<String> listJamPjdwl;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JTable tblRekap;
    private javax.swing.JTable tblSiswa;
    private javax.swing.JTable tbltdkhadir;
    private javax.swing.JTextArea txtCttn;
    private javax.swing.JLabel txtCttntdy;
    private javax.swing.JLabel txtGuru;
    private javax.swing.JLabel txtGurutdy;
    private javax.swing.JLabel txtKlsTAgenda;
    private javax.swing.JLabel txtKlstdy;
    private javax.swing.JLabel txtMapel;
    private javax.swing.JLabel txtMapelJdwl;
    private javax.swing.JLabel txtMapeltdy;
    private javax.swing.JLabel txtNama;
    private javax.swing.JLabel txtTglJdwl;
    private javax.swing.JLabel txtTglTAgenda;
    private javax.swing.JLabel txtTgltdy;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgAdmin.java
 *
 * Created on 04 Des 13, 12:59:34
 */
package khanzacetakantrianloket;

import bridging.ApiPcare;
import bridging.BPJSCekReferensiDokterDPJP1;
import bridging.PCareCekMappingPoli;
import bridging.PCareMapingPoli;
import bridging.PCareMapingDokter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import khanzacetakantrianloket.DlgCariDokter2;
import khanzacetakantrianloket.DlgCariPoli;
import khanzacetakantrianloket.DlgPilihBayar;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.engines.TnepresEngine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author Kode
 */
public class PendaftaranOnsite extends javax.swing.JDialog {

    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps, ps3;
    private ResultSet rs, rs3;
    private ApiPcare api=new ApiPcare();
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    private String umur = "0", sttsumur = "Th", hari = "", kode_dokter = "", kode_poli = "", nama_instansi, alamat_instansi, kabupaten, propinsi, kontak, email,otorisasi, utc="", kunjungansakit="", requestJson,URL="",link="", perawatan="", kdmediscari="";
    private String status = "Baru", BASENOREG = "", URUTNOREG = "", aktifjadwal = "";
    private Properties prop = new Properties();
    private File file;
//    private DlgCariPoli poli = new DlgCariPoli(null, true);
//    private DlgCariPoliBPJS poli = new DlgCariPoliBPJS(null, true);
//    private PCareMapingPoli poli=new PCareMapingPoli(null,false); 
     private PCareCekMappingPoli poli=new PCareCekMappingPoli(null,false); 
//    private PCareCekMappingPoli poli=new PCareCekMappingPoli(null,false); 
//    private DlgCariDokter2 dokter = new DlgCariDokter2(null, true);
    private PCareMapingDokter dokter=new PCareMapingDokter(null,true);
//    private BPJSCekReferensiDokterDPJP1 dokter = new BPJSCekReferensiDokterDPJP1(null, true);
    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    private Calendar cal = Calendar.getInstance();
    private int day = cal.get(Calendar.DAY_OF_WEEK), pilih=0;
    private DlgPilihBayar pilihbayar=new DlgPilihBayar(null,true);
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private JsonNode nameNode;
    /**
     * Creates new form DlgAdmin
     *
     * @param parent
     * @param id
     */
    public PendaftaranOnsite(java.awt.Frame parent, boolean id) {
        super(parent, id);
        initComponents();

        try {
            ps = koneksi.prepareStatement(
                    "select nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"
                    + "namakeluarga,keluarga,pasien.kd_pj,penjab.png_jawab,if(tgl_daftar=?,'Baru','Lama') as daftar, "
                    + "TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) as tahun, "
                    + "(TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12)) as bulan, "
                    + "TIMESTAMPDIFF(DAY, DATE_ADD(DATE_ADD(tgl_lahir,INTERVAL TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) YEAR), INTERVAL TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12) MONTH), CURDATE()) as hari from pasien "
                    + "inner join kelurahan inner join kecamatan inner join kabupaten inner join penjab "
                    + "on pasien.kd_kel=kelurahan.kd_kel and pasien.kd_pj=penjab.kd_pj "
                    + "and pasien.kd_kec=kecamatan.kd_kec and pasien.kd_kab=kabupaten.kd_kab "
                    + "where pasien.no_rkm_medis=?");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            ps = koneksi.prepareStatement("select nama_instansi, alamat_instansi, kabupaten, propinsi, aktifkan, wallpaper,kontak,email,logo from setting");
            rs = ps.executeQuery();
            while (rs.next()) {
                nama_instansi = rs.getString("nama_instansi");
                alamat_instansi = rs.getString("alamat_instansi");
                kabupaten = rs.getString("kabupaten");
                propinsi = rs.getString("propinsi");
                kontak = rs.getString("kontak");
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

      poli.addWindowListener(new WindowListener() {
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {
        // When window is closed, update the main form with the selected poli details.
        int selectedRow = poli.getTable().getSelectedRow();
        if(selectedRow != -1){   
            KdPoliTujuan.setText(poli.getTable().getValueAt(selectedRow,2).toString());
            NamaPoli.setText(poli.getTable().getValueAt(selectedRow,3).toString());
            kdpoli.setText(poli.getTable().getValueAt(selectedRow,0).toString());
            KdPoliTujuan.requestFocus(); // Focus the next logical component.
        }                  
    }
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
});

        
        poli.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    poli.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){   
                    KdTenagaMedis.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NamaDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    kdmediscari=dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),2).toString();
                    KdTenagaMedis.requestFocus();                      
                }                  
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dokter.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    dokter.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        pilihbayar.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {                
                    KdBayar.setText(pilihbayar.getTable().getValueAt(pilihbayar.getTable().getSelectedRow(),0).toString());
                    NmBayar.setText(pilihbayar.getTable().getValueAt(pilihbayar.getTable().getSelectedRow(),1).toString());
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });        
        
        

        try {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            aktifjadwal = prop.getProperty("JADWALDOKTERDIREGISTRASI");
            URUTNOREG = prop.getProperty("URUTNOREG");
            BASENOREG = prop.getProperty("BASENOREG");
        } catch (Exception ex) {
            aktifjadwal = "";
            URUTNOREG = "";
            BASENOREG = "";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LblKdPoli = new component.Label();
        LblKdDokter = new component.Label();
        NoReg = new component.TextBox();
        NoRawat = new component.TextBox();
        Biaya = new component.TextBox();
        TAlmt = new component.Label();
        TPngJwb = new component.Label();
        THbngn = new component.Label();
        NoTelpPasien = new component.Label();
        kdpoli = new widget.TextBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new component.Label();
        jLabel11 = new component.Label();
        lblNamaPasien = new component.Label();
        jLabel28 = new component.Label();
        jLabel29 = new component.Label();
        jLabel30 = new component.Label();
        jLabel31 = new component.Label();
        TanggalPeriksa = new widget.Tanggal();
        lblNoRM = new component.Label();
        jLabel32 = new component.Label();
        jLabel33 = new component.Label();
        jLabel35 = new component.Label();
        btnSimpan1 = new component.Button();
        jLabel36 = new component.Label();
        btnSimpan2 = new component.Button();
        BtnCetak = new widget.Button();
        BtnCetak1 = new widget.Button();
        PanelWall1 = new usu.widget.glass.PanelGlass();
        btnSimpan3 = new component.Button();
        NamaDokter = new component.Label();
        NamaPoli = new component.Label();
        NmBayar = new component.Label();
        KdBayar = new component.Label();
        TNoRw = new component.Label();
        KdPPK = new component.Label();
        KdTenagaMedis = new component.Label();
        KdPoliTujuan = new component.Label();
        lblNokartu = new component.Label();
        jLabel37 = new component.Label();
        jLabel38 = new component.Label();
        NmPPK = new component.Label();
        jLabel34 = new widget.Label();
        JenisKunjungan = new widget.ComboBox();
        Perawatan = new widget.ComboBox();
        jLabel40 = new component.Label();
        jLabel41 = new component.Label();
        keluahan = new component.Label();
        jLabel42 = new component.Label();

        LblKdPoli.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblKdPoli.setText("Norm");
        LblKdPoli.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LblKdPoli.setPreferredSize(new java.awt.Dimension(20, 14));

        LblKdDokter.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblKdDokter.setText("Norm");
        LblKdDokter.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LblKdDokter.setPreferredSize(new java.awt.Dimension(20, 14));

        NoReg.setPreferredSize(new java.awt.Dimension(320, 30));
        NoReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRegActionPerformed(evt);
            }
        });
        NoReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRegKeyPressed(evt);
            }
        });

        NoRawat.setPreferredSize(new java.awt.Dimension(320, 30));
        NoRawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRawatActionPerformed(evt);
            }
        });
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });

        Biaya.setPreferredSize(new java.awt.Dimension(320, 30));
        Biaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BiayaActionPerformed(evt);
            }
        });
        Biaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BiayaKeyPressed(evt);
            }
        });

        TAlmt.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TAlmt.setText("Norm");
        TAlmt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TAlmt.setPreferredSize(new java.awt.Dimension(20, 14));

        TPngJwb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TPngJwb.setText("Norm");
        TPngJwb.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TPngJwb.setPreferredSize(new java.awt.Dimension(20, 14));

        THbngn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        THbngn.setText("Norm");
        THbngn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        THbngn.setPreferredSize(new java.awt.Dimension(20, 14));

        NoTelpPasien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NoTelpPasien.setText("Norm");
        NoTelpPasien.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        NoTelpPasien.setPreferredSize(new java.awt.Dimension(20, 14));

        kdpoli.setHighlighter(null);
        kdpoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdpoliKeyPressed(evt);
            }
        });

        setTitle("SIMKES Khanza Cetak Antrian Loket");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(810, 530));
        jPanel1.setPreferredSize(new java.awt.Dimension(810, 530));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setForeground(new java.awt.Color(15, 81, 137));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("No. RM / Nama");
        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 200, 50));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setForeground(new java.awt.Color(15, 81, 137));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(":");
        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 30, 50));

        lblNamaPasien.setForeground(new java.awt.Color(0, 51, 102));
        lblNamaPasien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNamaPasien.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        lblNamaPasien.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(lblNamaPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 380, 50));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setForeground(new java.awt.Color(15, 81, 137));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText(":");
        jLabel28.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel28.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 30, 50));

        jLabel29.setBackground(new java.awt.Color(255, 255, 255));
        jLabel29.setForeground(new java.awt.Color(15, 81, 137));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("Tanggal Periksa");
        jLabel29.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel29.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 200, 50));

        jLabel30.setBackground(new java.awt.Color(255, 255, 255));
        jLabel30.setForeground(new java.awt.Color(15, 81, 137));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText(":");
        jLabel30.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel30.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 30, 50));

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setForeground(new java.awt.Color(15, 81, 137));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("Poliklinik");
        jLabel31.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel31.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 120, 50));

        TanggalPeriksa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TanggalPeriksa.setForeground(new java.awt.Color(0, 51, 102));
        TanggalPeriksa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-01-2025" }));
        TanggalPeriksa.setDisplayFormat("dd-MM-yyyy");
        TanggalPeriksa.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        TanggalPeriksa.setPreferredSize(new java.awt.Dimension(95, 23));
        TanggalPeriksa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TanggalPeriksaItemStateChanged(evt);
            }
        });
        TanggalPeriksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalPeriksaActionPerformed(evt);
            }
        });
        TanggalPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalPeriksaKeyPressed(evt);
            }
        });
        jPanel2.add(TanggalPeriksa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 180, 50));

        lblNoRM.setForeground(new java.awt.Color(0, 51, 102));
        lblNoRM.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoRM.setText("123456");
        lblNoRM.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        lblNoRM.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(lblNoRM, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 100, 50));

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setForeground(new java.awt.Color(15, 81, 137));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Dokter ");
        jLabel32.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel32.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 140, 50));

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setForeground(new java.awt.Color(15, 81, 137));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText(":");
        jLabel33.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel33.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 30, 50));

        jLabel35.setBackground(new java.awt.Color(255, 255, 255));
        jLabel35.setForeground(new java.awt.Color(15, 81, 137));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText(":");
        jLabel35.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel35.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 30, 50));

        btnSimpan1.setBorder(null);
        btnSimpan1.setForeground(new java.awt.Color(15, 81, 137));
        btnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnSimpan1.setMnemonic('S');
        btnSimpan1.setText("Pilih Poli");
        btnSimpan1.setToolTipText("Alt+S");
        btnSimpan1.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        btnSimpan1.setGlassColor(new java.awt.Color(255, 255, 255));
        btnSimpan1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpan1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan1.setIconTextGap(10);
        btnSimpan1.setPreferredSize(new java.awt.Dimension(300, 45));
        btnSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan1ActionPerformed(evt);
            }
        });
        btnSimpan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpan1KeyPressed(evt);
            }
        });
        jPanel2.add(btnSimpan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, 170, 50));

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setForeground(new java.awt.Color(15, 81, 137));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setText("Keluhan");
        jLabel36.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel36.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, 50));

        btnSimpan2.setBorder(null);
        btnSimpan2.setForeground(new java.awt.Color(15, 81, 137));
        btnSimpan2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnSimpan2.setMnemonic('S');
        btnSimpan2.setText("Pilih Dokter");
        btnSimpan2.setToolTipText("Alt+S");
        btnSimpan2.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        btnSimpan2.setGlassColor(new java.awt.Color(255, 255, 255));
        btnSimpan2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpan2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan2.setIconTextGap(10);
        btnSimpan2.setPreferredSize(new java.awt.Dimension(300, 45));
        btnSimpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan2ActionPerformed(evt);
            }
        });
        btnSimpan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpan2KeyPressed(evt);
            }
        });
        jPanel2.add(btnSimpan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 180, 170, 50));

        BtnCetak.setForeground(new java.awt.Color(15, 81, 137));
        BtnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/tombolatm.png"))); // NOI18N
        BtnCetak.setText("Simpan");
        BtnCetak.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        BtnCetak.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetak.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetak.setIconTextGap(25);
        BtnCetak.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakActionPerformed(evt);
            }
        });
        BtnCetak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCetakKeyPressed(evt);
            }
        });
        jPanel2.add(BtnCetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 260, 80));

        BtnCetak1.setForeground(new java.awt.Color(15, 81, 137));
        BtnCetak1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/tombolatm - kanan.png"))); // NOI18N
        BtnCetak1.setText("Kembali");
        BtnCetak1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        BtnCetak1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BtnCetak1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BtnCetak1.setIconTextGap(30);
        BtnCetak1.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetak1ActionPerformed(evt);
            }
        });
        jPanel2.add(BtnCetak1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 420, 260, 80));

        PanelWall1.setBackground(new java.awt.Color(255, 255, 255));
        PanelWall1.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/Untuk Informasi lLebih Lanjut,.png"))); // NOI18N
        PanelWall1.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_CENTER_BOTTOM);
        PanelWall1.setForeground(new java.awt.Color(255, 255, 255));
        PanelWall1.setOpaque(true);
        PanelWall1.setOpaqueGradient(true);
        PanelWall1.setPreferredSize(new java.awt.Dimension(1200, 200));
        PanelWall1.setRound(false);
        PanelWall1.setToolTipText("tes");

        javax.swing.GroupLayout PanelWall1Layout = new javax.swing.GroupLayout(PanelWall1);
        PanelWall1.setLayout(PanelWall1Layout);
        PanelWall1Layout.setHorizontalGroup(
            PanelWall1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1270, Short.MAX_VALUE)
        );
        PanelWall1Layout.setVerticalGroup(
            PanelWall1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel2.add(PanelWall1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 1270, 70));

        btnSimpan3.setBorder(null);
        btnSimpan3.setForeground(new java.awt.Color(15, 81, 137));
        btnSimpan3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnSimpan3.setMnemonic('S');
        btnSimpan3.setText("Pilih Bayar");
        btnSimpan3.setToolTipText("Alt+S");
        btnSimpan3.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        btnSimpan3.setGlassColor(new java.awt.Color(255, 255, 255));
        btnSimpan3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpan3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan3.setIconTextGap(10);
        btnSimpan3.setPreferredSize(new java.awt.Dimension(300, 45));
        btnSimpan3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan3ActionPerformed(evt);
            }
        });
        btnSimpan3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpan3KeyPressed(evt);
            }
        });
        jPanel2.add(btnSimpan3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 240, 170, 50));

        NamaDokter.setForeground(new java.awt.Color(0, 51, 102));
        NamaDokter.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NamaDokter.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        NamaDokter.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(NamaDokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 290, 50));

        NamaPoli.setForeground(new java.awt.Color(0, 51, 102));
        NamaPoli.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NamaPoli.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        NamaPoli.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(NamaPoli, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 310, 50));

        NmBayar.setForeground(new java.awt.Color(0, 51, 102));
        NmBayar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NmBayar.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        NmBayar.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(NmBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 300, 50));

        KdBayar.setForeground(new java.awt.Color(0, 51, 102));
        KdBayar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        KdBayar.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        KdBayar.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(KdBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 90, 50));

        TNoRw.setForeground(new java.awt.Color(0, 51, 102));
        TNoRw.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TNoRw.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        TNoRw.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(TNoRw, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 300, 200, 30));

        KdPPK.setForeground(new java.awt.Color(0, 131, 62));
        KdPPK.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        KdPPK.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        KdPPK.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(KdPPK, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 340, 230, 40));

        KdTenagaMedis.setForeground(new java.awt.Color(0, 51, 102));
        KdTenagaMedis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        KdTenagaMedis.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        KdTenagaMedis.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(KdTenagaMedis, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 130, 50));

        KdPoliTujuan.setForeground(new java.awt.Color(0, 51, 102));
        KdPoliTujuan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        KdPoliTujuan.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        KdPoliTujuan.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(KdPoliTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 90, 50));

        lblNokartu.setForeground(new java.awt.Color(15, 81, 137));
        lblNokartu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNokartu.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblNokartu.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(lblNokartu, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, 280, 30));

        jLabel37.setBackground(new java.awt.Color(255, 255, 255));
        jLabel37.setForeground(new java.awt.Color(15, 81, 137));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText("Cara Bayar");
        jLabel37.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel37.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 130, 50));

        jLabel38.setBackground(new java.awt.Color(255, 255, 255));
        jLabel38.setForeground(new java.awt.Color(15, 81, 137));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText(":");
        jLabel38.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel38.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, 30, 50));

        NmPPK.setForeground(new java.awt.Color(15, 81, 137));
        NmPPK.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NmPPK.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        NmPPK.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(NmPPK, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 310, 40));

        jLabel34.setForeground(new java.awt.Color(15, 81, 137));
        jLabel34.setText("Jenis Kunjungan");
        jLabel34.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 160, -1));

        JenisKunjungan.setForeground(new java.awt.Color(15, 81, 137));
        JenisKunjungan.setMaximumRowCount(23);
        JenisKunjungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kunjungan Sakit", "Kunjungan Sehat" }));
        JenisKunjungan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        JenisKunjungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JenisKunjunganKeyPressed(evt);
            }
        });
        jPanel2.add(JenisKunjungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 150, 40));

        Perawatan.setForeground(new java.awt.Color(15, 81, 137));
        Perawatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10 Rawat Jalan", "20 Rawat Inap", "50 Promotif Preventif" }));
        Perawatan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Perawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanKeyPressed(evt);
            }
        });
        jPanel2.add(Perawatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 370, 170, 40));

        jLabel40.setBackground(new java.awt.Color(255, 255, 255));
        jLabel40.setForeground(new java.awt.Color(15, 81, 137));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setText("Faskes");
        jLabel40.setFont(new java.awt.Font("Trebuchet MS", 2, 20)); // NOI18N
        jLabel40.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 130, 50));

        jLabel41.setBackground(new java.awt.Color(255, 255, 255));
        jLabel41.setForeground(new java.awt.Color(15, 81, 137));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText(":");
        jLabel41.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel41.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 30, 50));

        keluahan.setForeground(new java.awt.Color(0, 51, 102));
        keluahan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        keluahan.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        keluahan.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(keluahan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 360, 30));

        jLabel42.setBackground(new java.awt.Color(255, 255, 255));
        jLabel42.setForeground(new java.awt.Color(15, 81, 137));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText(":");
        jLabel42.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel42.setPreferredSize(new java.awt.Dimension(20, 14));
        jPanel2.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 30, 50));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 530));

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        getAccessibleContext().setAccessibleName("ANJUNGAN PASIEN MANDIRI");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void NoRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRegActionPerformed

    private void NoRegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRegKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRegKeyPressed

    private void NoRawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRawatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatKeyPressed

    private void BiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BiayaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BiayaActionPerformed

    private void BiayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BiayaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BiayaKeyPressed

    private void btnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan1ActionPerformed
    try {
        if (poli != null) {
            poli.setSize(jPanel2.getWidth() - 50, jPanel2.getHeight() - 50);
            poli.setLocationRelativeTo(jPanel2);
            poli.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Dialog 'poli' is not initialized!");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error showing poli dialog: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSimpan1ActionPerformed

    private void btnSimpan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpan1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpan1KeyPressed

    private void btnSimpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan2ActionPerformed
//        dokter.carinamadokter(Kddokter.getText(), NamaDokter.getText());
        dokter.setSize(jPanel1.getWidth() - 50, jPanel1.getHeight() - 50);
        dokter.setLocationRelativeTo(jPanel2);
        dokter.setVisible(true);
        
//        dokter.setSize(jPanel1.getWidth()-20,jPanel1.getHeight()-20);
//        dokter.setLocationRelativeTo(jPanel2);
//        dokter.setVisible(true);
    }//GEN-LAST:event_btnSimpan2ActionPerformed

    private void btnSimpan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpan2KeyPressed

    private void BtnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakActionPerformed
        if (lblNoRM.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "No RM Kosong");
        } else if (kode_poli == "") {
            JOptionPane.showMessageDialog(rootPane, "Pilih poli terlebih dahulu");
        } else if (kode_dokter == "") {
            JOptionPane.showMessageDialog(rootPane, "Pilih Dokter terlebih dahulu");
        } else  if(Sequel.cariInteger("select count(no_rkm_medis) from reg_periksa where no_rkm_medis=? and kd_poli=?",lblNoRM.getText(),LblKdPoli.getText())>0){
            status="Lama";
            JOptionPane.showMessageDialog(rootPane, "Maaf, anda sudah terdaftar pada hari ini dengan dokter yang sama ");
        } else {
            isNumber();
            String biayareg = Sequel.cariIsi("SELECT registrasilama FROM poliklinik WHERE kd_poli='" + kode_poli + "'");
            UpdateUmur();
            SimpanPendaftaranPCare();
            isCekPasien();
            if (Sequel.menyimpantf2("reg_periksa", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 19,
                new String[]{NoReg.getText(), NoRawat.getText(), Valid.SetTgl(TanggalPeriksa.getSelectedItem() + ""), Sequel.cariIsi("select current_time()"),
                    kode_dokter, lblNoRM.getText(), kode_poli, TPngJwb.getText(), TAlmt.getText(), THbngn.getText(), biayareg, "Belum",
                    "Lama", "Ralan", KdBayar.getText(), umur, sttsumur, "Belum Bayar", status}) == true) {
            MnCetakRegisterActionPerformed(NoRawat.getText());
            NoReg.setText("");
            TNoRw.setText("");
            NoRawat.setText("");
            lblNoRM.setText("");
            TPngJwb.setText("");
            TAlmt.setText("");
            THbngn.setText("");
            umur = "";
            sttsumur = "";
            kode_poli = "";
            NamaPoli.setText("");
            NamaDokter.setText("");
            kode_dokter = "";
            JOptionPane.showMessageDialog(rootPane, "Berhasil");
            this.dispose();
        }

        }
    }//GEN-LAST:event_BtnCetakActionPerformed

    private void BtnCetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetak1ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCetak1ActionPerformed

    private void btnSimpan3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan3ActionPerformed
        pilihbayar.setSize(this.getWidth()-20,this.getHeight()-20);
        pilihbayar.setLocationRelativeTo(this);
        pilihbayar.tampil();
        pilihbayar.setVisible(true);
    }//GEN-LAST:event_btnSimpan3ActionPerformed

    private void btnSimpan3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpan3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpan3KeyPressed

    private void BtnCetakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCetakKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCetakActionPerformed(null);
        }
    }//GEN-LAST:event_BtnCetakKeyPressed

    private void TanggalPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalPeriksaKeyPressed

    }//GEN-LAST:event_TanggalPeriksaKeyPressed

    private void TanggalPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TanggalPeriksaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalPeriksaActionPerformed

    private void TanggalPeriksaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TanggalPeriksaItemStateChanged
        tentukanHari();
        kode_poli = "";
        NamaPoli.setText("");
        NamaDokter.setText("");
        kode_dokter = "";
        //        poli.tampil(hari);
        //        poli.setSize(jPanel2.getWidth() - 50, jPanel2.getHeight() - 50);
        //        poli.setLocationRelativeTo(jPanel2);
        //        poli.setVisible(true);
    }//GEN-LAST:event_TanggalPeriksaItemStateChanged

    private void kdpoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdpoliKeyPressed

    }//GEN-LAST:event_kdpoliKeyPressed

    private void JenisKunjunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JenisKunjunganKeyPressed

    }//GEN-LAST:event_JenisKunjunganKeyPressed

    private void PerawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanKeyPressed

    }//GEN-LAST:event_PerawatanKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
           PendaftaranOnsite dialog = new PendaftaranOnsite(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.TextBox Biaya;
    private widget.Button BtnCetak;
    private widget.Button BtnCetak1;
    private widget.ComboBox JenisKunjungan;
    private component.Label KdBayar;
    private component.Label KdPPK;
    private component.Label KdPoliTujuan;
    private component.Label KdTenagaMedis;
    private component.Label LblKdDokter;
    private component.Label LblKdPoli;
    private component.Label NamaDokter;
    private component.Label NamaPoli;
    private component.Label NmBayar;
    private component.Label NmPPK;
    private component.TextBox NoRawat;
    private component.TextBox NoReg;
    private component.Label NoTelpPasien;
    private usu.widget.glass.PanelGlass PanelWall1;
    private widget.ComboBox Perawatan;
    private component.Label TAlmt;
    private component.Label THbngn;
    private component.Label TNoRw;
    private component.Label TPngJwb;
    private widget.Tanggal TanggalPeriksa;
    private component.Button btnSimpan1;
    private component.Button btnSimpan2;
    private component.Button btnSimpan3;
    private component.Label jLabel10;
    private component.Label jLabel11;
    private component.Label jLabel28;
    private component.Label jLabel29;
    private component.Label jLabel30;
    private component.Label jLabel31;
    private component.Label jLabel32;
    private component.Label jLabel33;
    private widget.Label jLabel34;
    private component.Label jLabel35;
    private component.Label jLabel36;
    private component.Label jLabel37;
    private component.Label jLabel38;
    private component.Label jLabel40;
    private component.Label jLabel41;
    private component.Label jLabel42;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private widget.TextBox kdpoli;
    private component.Label keluahan;
    private component.Label lblNamaPasien;
    private component.Label lblNoRM;
    private component.Label lblNokartu;
    // End of variables declaration//GEN-END:variables
  public void setPasien(String norm) { 
                    lblNoRM.setText(norm);
                    lblNamaPasien.setText(Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='" + norm + "'"));
                    lblNokartu.setText(Sequel.cariIsi("select pasien.no_peserta from pasien where pasien.no_rkm_medis='" + norm + "'"));
                   if (!lblNoRM.getText().equals("") && !lblNokartu.getText().equals("")) {
                       tentukanHari();
                  }       
}
    
    public void setPasien(String noPeserta, String noRmPasien) {
        
        try {
    otorisasi = koneksiDB.USERPCARE() + ":" + koneksiDB.PASSPCARE() + ":095";
    link = koneksiDB.URLAPIPCARE();  
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("X-cons-id", koneksiDB.CONSIDAPIPCARE());
    utc = String.valueOf(api.GetUTCdatetimeAsString());
    headers.add("X-timestamp", utc);
    headers.add("X-signature", api.getHmac());
    headers.add("X-authorization", "Basic " + Base64.encodeBase64String(otorisasi.getBytes()));
    headers.add("user_key", koneksiDB.USERKEYAPIPCARE());

    // Membuat request ke API
    requestEntity = new HttpEntity(headers);
    System.out.println("URL: " + link + "/peserta/" + noPeserta);
    root = mapper.readTree(api.getRest().exchange(link + "/peserta/" + noPeserta, HttpMethod.GET, requestEntity, String.class).getBody());
    nameNode = root.path("metaData");

    if (nameNode.path("message").asText().equals("OK")) {
        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));

        // Validasi kepesertaan
        if (!response.path("kdProviderPst").path("kdProvider").asText().equals(KdPPK.getText())) {
            int pilih = JOptionPane.showConfirmDialog(null, "Bukan peserta Anda, apa mau dibatalkan...?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (pilih == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            // Lanjutkan dengan menampilkan data pada field GUI
            String namaPasien = response.path("nama").asText();
            String noKartu = response.path("noKartu").asText();
            String nmProvider = response.path("kdProviderPst").path("nmProvider").asText();

            lblNoRM.setText(noRmPasien);
            lblNamaPasien.setText(namaPasien);
            lblNokartu.setText(noKartu);
            NmPPK.setText(nmProvider);

            System.out.println("Nama Pasien: " + namaPasien);
            System.out.println("No RM: " + noRmPasien);
            System.out.println("No Kartu: " + noKartu);
            System.out.println("NmPPK: " + nmProvider);
        }
    }
} catch (Exception ex) {
    System.out.println("Notifikasi: " + ex);
//    handleExceptions(ex);
}

        
        
//    try {
//         otorisasi=koneksiDB.USERPCARE()+":"+koneksiDB.PASSPCARE()+":095";
//            link=koneksiDB.URLAPIPCARE();  
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("X-cons-id", koneksiDB.CONSIDAPIPCARE());
//        utc = String.valueOf(api.GetUTCdatetimeAsString());
//        headers.add("X-timestamp", utc);
//        headers.add("X-signature", api.getHmac());
//        headers.add("X-authorization", "Basic " + Base64.encodeBase64String(otorisasi.getBytes()));
//        headers.add("user_key", koneksiDB.USERKEYAPIPCARE());
//
//        // Membuat request ke API
//        requestEntity = new HttpEntity(headers);
//        System.out.println("URL: " + link + "/peserta/" + noPeserta);
//        root = mapper.readTree(api.getRest().exchange(link + "/peserta/" + noPeserta, HttpMethod.GET, requestEntity, String.class).getBody());
//        nameNode = root.path("metaData");
//        requestEntity = new HttpEntity(headers);
//
//        if (nameNode.path("message").asText().equals("OK")) {
//            response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
//
//            // Ambil data dari JSON response
//            String namaPasien = response.path("nama").asText();
//            String noKartu = response.path("noKartu").asText();
//            String nmProvider = response.path("kdProviderPst").path("nmProvider").asText();
//
//            // Tampilkan data pada field GUI
//            lblNoRM.setText(noRmPasien);
//            lblNamaPasien.setText(namaPasien);
//            lblNokartu.setText(noKartu);
//            NmPPK.setText(nmProvider);
//
//            System.out.println("Nama Pasien: " + namaPasien);
//            System.out.println("No RM: " + noRmPasien);
//            System.out.println("No Kartu: " + noKartu);
//            System.out.println("NmPPK: " + nmProvider);
//
//            
//        }
//        
//    } catch (Exception ex) {
//        System.out.println("Notifikasi: " + ex);
//        if (ex.toString().contains("UnknownHostException")) {
//            JOptionPane.showMessageDialog(null, "Koneksi ke server PCare terputus...!");
//        } else if (ex.toString().contains("500")) {
//            JOptionPane.showMessageDialog(null, "Server PCare baru ngambek...!");
//        } else if (ex.toString().contains("401")) {
//            JOptionPane.showMessageDialog(null, "Username/Password salah.");
//        } else if (ex.toString().contains("408")) {
//            JOptionPane.showMessageDialog(null, "Time out.");
//        } else if (ex.toString().contains("204")) {
//            JOptionPane.showMessageDialog(null, "Data tidak ditemukan.");
//        } else if (ex.toString().contains("refused")) {
//            JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS ditolak.");
//        }
//    }

 }
    

    public void isCek(String norm) {

    }

    private void UpdateUmur() {
        Sequel.mengedit("pasien", "no_rkm_medis=?", "umur=CONCAT(CONCAT(CONCAT(TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()), ' Th '),CONCAT(TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12), ' Bl ')),CONCAT(TIMESTAMPDIFF(DAY, DATE_ADD(DATE_ADD(tgl_lahir,INTERVAL TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) YEAR), INTERVAL TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12) MONTH), CURDATE()), ' Hr'))", 1, new String[]{lblNoRM.getText()});
    }

    private void isNumber() {
        if (BASENOREG.equals("booking")) {
            switch (URUTNOREG) {
                case "poli":
                    if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")
                            >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")) {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    } else {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    }
                    break;
                case "dokter":
                    if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kode_dokter + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")
                            >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")) {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kode_dokter + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    } else {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    }
                    break;
                case "dokter + poli":
                    if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kode_dokter + "' and kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")
                            >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")) {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kode_dokter + "' and kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    } else {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    }
                    break;
                default:
                    if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")
                            >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'")) {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kode_poli + "' and tanggal_periksa='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    } else {
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    }
                    break;
            }
        } else {
            switch (URUTNOREG) {
                case "poli":
                    Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    break;
                case "dokter":
                    Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    break;
                case "dokter + poli":
                    Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and kd_poli='" + kode_poli + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    break;
                default:
                    Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kode_dokter + "' and tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "'", "", 3, NoReg);
                    break;
            }
        }

        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_rawat,6),signed)),0) from reg_periksa where tgl_registrasi='" + Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()) + "' ", Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()).replaceAll("-", "/") + "/", 6, NoRawat);
    }

    private void tampilPenjab() {
        try {
            file = new File("./cache/anjunganpenjamin.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem = "";
            ps = koneksi.prepareStatement("select * from penjab where status='1' AND kd_pj='001' order by kd_pj");
           // NmBayar.removeAllItems();
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
              //      NmBayar.addItem(rs.getString(2).replaceAll("\"", ""));
                    iyem = iyem + "{\"NamaPenjab\":\"" + rs.getString(2).replaceAll("\"", "") + "\",\"KodePenjab\":\"" + rs.getString(1) + "\"},";
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            fileWriter.write("{\"anjunganpenjamin\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
            fileWriter.flush();
            fileWriter.close();
            iyem = null;

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

//    private void tampilDokter() {
//        if (!kode_poli.equals("")) {
//
//            try {
//                file = new File("./cache/anjungandokter.iyem");
//                file.createNewFile();
//                fileWriter = new FileWriter(file);
//                iyem = "";
//                ps = koneksi.prepareStatement("SELECT\n"
//                        + "	dokter.nm_dokter, \n"
//                        + "	jadwal.kd_dokter, jadwal.jam_mulai,jadwal.jam_selesai\n"
//                        + "FROM\n"
//                        + "	dokter\n"
//                        + "	INNER JOIN\n"
//                        + "	jadwal\n"
//                        + "	ON \n"
//                        + "		dokter.kd_dokter = jadwal.kd_dokter\n"
//                        + "		where jadwal.hari_kerja='" + hari + "' and jadwal.kd_poli='" + kode_poli + "'");
//                cmbDokterTujuan.removeAllItems();
//                try {
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        cmbDokterTujuan.addItem(rs.getString(1) + " " + rs.getString(3) + " s/d " + rs.getString(4));
//
//                        iyem = iyem + "{\"NamaDokter\":\"" + rs.getString(1).replaceAll("\"", "") + "\",\"KodeDokter\":\"" + rs.getString(2) + "\"},";
//                    }
//                } catch (Exception e) {
//                    System.out.println("Notifikasi resultset dokter : " + e);
//                } finally {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                    if (ps != null) {
//                        ps.close();
//                    }
//                }
//                fileWriter.write("{\"anjungandokter\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
//                fileWriter.flush();
//                fileWriter.close();
//                iyem = null;
//
//            } catch (Exception e) {
//                System.out.println("Notifikasi dokter : " + e);
//            }
//        } else {
//            JOptionPane.showMessageDialog(rootPane, "Pilih poli terlebih dahulu");
//        }
//
//        tampilPenjab();
//    }
//    private void tampilPoli() {
//
//        try {
//            file = new File("./cache/anjunganpoli.iyem");
//            file.createNewFile();
//            fileWriter = new FileWriter(file);
//            iyem = "";
//            ps = koneksi.prepareStatement("SELECT\n"
//                    + "	poliklinik.nm_poli, \n"
//                    + "	poliklinik.kd_poli, \n"
//                    + "	jadwal.hari_kerja\n"
//                    + "FROM\n"
//                    + "	jadwal\n"
//                    + "	INNER JOIN\n"
//                    + "	poliklinik\n"
//                    + "	ON \n"
//                    + "		jadwal.kd_poli = poliklinik.kd_poli\n"
//                    + "		where poliklinik.status='1' and jadwal.hari_kerja='" + hari + "'\n"
//                    + "		group by jadwal.kd_poli");
//            cmbPoliTujuan.removeAllItems();
//            try {
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    System.out.println(rs.getString(1));
//                    cmbPoliTujuan.addItem(rs.getString(1).replaceAll("\"", ""));
//                    iyem = iyem + "{\"NamaPoli\":\"" + rs.getString(1).replaceAll("\"", "") + "\",\"KodePoli\":\"" + rs.getString(2) + "\"},";
//                }
//            } catch (Exception e) {
//                System.out.println("Notifikasi resultset : " + e);
//            } finally {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (ps != null) {
//                    ps.close();
//                }
//            }
//            System.out.println("disini");
//            fileWriter.write("{\"anjunganpoli\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
//            fileWriter.flush();
//            fileWriter.close();
//            iyem = null;
//
//        } catch (Exception e) {
//            System.out.println("Notifikasi poli : " + e);
//        }
//    }
    private void tentukanHari() {
        try {
            java.sql.Date hariperiksa = java.sql.Date.valueOf(Valid.SetTgl(TanggalPeriksa.getSelectedItem().toString()));
            cal.setTime(hariperiksa);
            day = cal.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case 1:
                    hari = "AKHAD";
                    break;
                case 2:
                    hari = "SENIN";
                    break;
                case 3:
                    hari = "SELASA";
                    break;
                case 4:
                    hari = "RABU";
                    break;
                case 5:
                    hari = "KAMIS";
                    break;
                case 6:
                    hari = "JUMAT";
                    break;
                case 7:
                    hari = "SABTU";
                    break;
                default:
                    break;
            }
            System.out.println(hari);

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    private void isCekPasien() {
        try {
            ps3 = koneksi.prepareStatement("select nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"
                    + "namakeluarga,keluarga,pasien.kd_pj,penjab.png_jawab,if(tgl_daftar=?,'Baru','Lama') as daftar, "
                    + "TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) as tahun,pasien.no_peserta, "
                    + "(TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12)) as bulan, "
                    + "TIMESTAMPDIFF(DAY, DATE_ADD(DATE_ADD(tgl_lahir,INTERVAL TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) YEAR), INTERVAL TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12) MONTH), CURDATE()) as hari,pasien.no_ktp,pasien.no_tlp "
                    + "from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "
                    + "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "
                    + "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "
                    + "inner join penjab on pasien.kd_pj=penjab.kd_pj "
                    + "where pasien.no_rkm_medis=?");
            try {
                ps3.setString(1, Valid.SetTgl(TanggalPeriksa.getSelectedItem() + ""));
                ps3.setString(2, lblNoRM.getText());
                rs = ps3.executeQuery();
                while (rs.next()) {
                    TAlmt.setText(rs.getString("asal"));
                    TPngJwb.setText(rs.getString("namakeluarga"));
                    THbngn.setText(rs.getString("keluarga"));
                    NoTelpPasien.setText(rs.getString("no_tlp"));
                    umur = "0";
                    sttsumur = "Th";
                    if (rs.getInt("tahun") > 0) {
                        umur = rs.getString("tahun");
                        sttsumur = "Th";
                    } else if (rs.getInt("tahun") == 0) {
                        if (rs.getInt("bulan") > 0) {
                            umur = rs.getString("bulan");
                            sttsumur = "Bl";
                        } else if (rs.getInt("bulan") == 0) {
                            umur = rs.getString("hari");
                            sttsumur = "Hr";
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps3 != null) {
                    ps3.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        status = "Baru";
        if (Sequel.cariInteger("select count(no_rkm_medis) from reg_periksa where no_rkm_medis=? and kd_poli=?", lblNoRM.getText(), kode_poli) > 0) {
            status = "Lama";
        }

    }

    private void MnCetakRegisterActionPerformed(String norawat) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Map<String, Object> param = new HashMap<>();
        param.put("namars", Sequel.cariIsi("select nama_instansi from setting"));
        param.put("alamatrs", Sequel.cariIsi("select alamat_instansi from setting"));
        param.put("kotars", Sequel.cariIsi("select kabupaten from setting"));
        param.put("propinsirs", Sequel.cariIsi("select propinsi from setting"));
        param.put("kontakrs", Sequel.cariIsi("select kontak from setting"));
        param.put("emailrs", Sequel.cariIsi("select email from setting"));
        param.put("logo", Sequel.cariGambar("select logo from setting"));
        Valid.MyReportqryabdul("rptBuktiRegister.jasper", "report", "::[ Bukti Register ]::",
                "select IF ((SELECT count( booking_registrasi.no_rkm_medis ) FROM booking_registrasi WHERE booking_registrasi.STATUS = 'Terdaftar'  AND booking_registrasi.no_rkm_medis = reg_periksa.no_rkm_medis AND booking_registrasi.tanggal_periksa = reg_periksa .tgl_registrasi AND kd_dokter = reg_periksa.kd_dokter )= 1,CONCAT( 'A', reg_periksa.no_reg ),CONCAT( 'W', reg_periksa.no_reg ) ) AS no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,pasien.no_tlp,"
                + "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.umur as umur,poliklinik.nm_poli,"
                + "reg_periksa.p_jawab,reg_periksa.almt_pj,reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.stts_daftar,penjab.png_jawab "
                + "from reg_periksa inner join dokter inner join pasien inner join poliklinik inner join penjab "
                + "on reg_periksa.kd_dokter=dokter.kd_dokter and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "and reg_periksa.kd_pj=penjab.kd_pj and reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat='" + norawat + "' ", param);
        System.out.println(norawat);
        this.setCursor(Cursor.getDefaultCursor());

    }

    private void MnCetakBarcodeRawatJalan(String norawat) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("emailrs", akses.getemailrs());
        param.put("no_rawat", norawat);
        param.put("logo", Sequel.cariGambar("select logo from setting"));
        Valid.MyReportSilentPrint("rptBarcodeRawat3.jasper", param, "::[ Barcode No.RM ]::");
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void SimpanPendaftaranPCare() {
        try {
            
            otorisasi=koneksiDB.USERPCARE()+":"+koneksiDB.PASSPCARE()+":095";
            link=koneksiDB.URLAPIPCARE();  
            
            headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add("X-cons-id",koneksiDB.CONSIDAPIPCARE());
            utc=String.valueOf(api.GetUTCdatetimeAsString());
	    headers.add("X-timestamp",utc);            
	    headers.add("X-signature",api.getHmac());
            headers.add("X-authorization","Basic "+Base64.encodeBase64String(otorisasi.getBytes()));
            headers.add("user_key",koneksiDB.USERKEYAPIPCARE());
            
            kunjungansakit="true";
            if(JenisKunjungan.getSelectedItem().toString().equals("Kunjungan Sehat")){
                kunjungansakit="false";
            }
            
            
            requestJson ="{" +
                            "\"kdProviderPeserta\": \""+NmPPK.getText()+"\"," +
                            "\"tglDaftar\": \""+TanggalPeriksa.getSelectedItem().toString().substring(0,10)+"\"," +
                            "\"noKartu\": \""+lblNokartu.getText()+"\"," +
                            "\"kdPoli\": \""+"001"+"\"," +
                            "\"keluhan\": \""+keluahan.getText()+"\"," +
                            "\"kunjSakit\": "+kunjungansakit+"," +
                            "\"sistole\": 0," +
                            "\"diastole\": 0," +
                            "\"beratBadan\": 0," +
                            "\"tinggiBadan\": 0," +
                            "\"respRate\": 0," +
                            "\"lingkarPerut\": 0," +
                            "\"heartRate\": 0," +
                            "\"rujukBalik\": 0," +
                            "\"kdTkp\": \""+Perawatan.getSelectedItem().toString().substring(0,2)+"\"" +
                         "}";
            
            System.out.println(requestJson);
            requestEntity = new HttpEntity(requestJson,headers);
            System.out.println("URL : "+URL);
            requestJson=api.getRest().exchange(link+"/pendaftaran", HttpMethod.POST, requestEntity, String.class).getBody();
            root = mapper.readTree(requestJson);
            nameNode = root.path("metaData");
            System.out.println("code : "+nameNode.path("code").asText());
            System.out.println("message : "+nameNode.path("message").asText());
            if(nameNode.path("code").asText().equals("201")){
                response = mapper.readTree(api.Decrypt(root.path("response").asText(),utc)).path("message");
                System.out.println("noUrut : "+response.asText());
                if(Sequel.menyimpantf("pcare_pendaftaran","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Terkirim'","No.Urut",20,new String[]{
                     TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),lblNoRM.getText(),lblNokartu.getText(),NmPPK.getText(),
                    lblNokartu.getText(),kode_poli,NamaPoli.getText(),keluahan.getText(),kunjungansakit,
                    "0","0","0","0","0","0",
                    "0","0",perawatan,response.asText()
                })==true){  
                    if((!keluahan.getText().trim().equals("")))
                            Sequel.menyimpan2("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",21,new String[]{
                                TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),Sequel.cariIsi("select current_time()"),
                                "0","0"+"/"+"0","0","0","0", 
                                "0","","","Compos Mentis", keluahan.getText(),"","","0","","","","",kode_dokter
                            });    
                }                     
            }
        }catch (Exception ex) {
            System.out.println("Notifikasi Bridging : "+ex);
            if(ex.toString().contains("UnknownHostException")||ex.toString().contains("unreachable")){
                if(Sequel.menyimpantf("pcare_pendaftaran","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Gagal'","No.Urut",20,new String[]{
                    TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),lblNoRM.getText(),lblNamaPasien.getText(),NmPPK.getText(),
                    lblNokartu.getText(),kode_poli,NamaPoli.getText(),keluahan.getText(),kunjungansakit,
                    "0","0","0","0","0","0",
                    "0","0",perawatan,response.asText()
                })==true){  
                    if((!keluahan.getText().trim().equals("")))
                        Sequel.menyimpan2("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",21,new String[]{
                                TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),Sequel.cariIsi("select current_time()"),
                                "0","0"+"/"+"0","0","0","0", 
                                "0","","","Compos Mentis", keluahan.getText(),"","","0","","","","",kode_dokter
                            });    
                }  
                JOptionPane.showMessageDialog(null,"Koneksi ke server PCare terputus. Data disimpan secara lokal, dan dapat dikirimkan kembali ke server PCare..!!");
            }else if(ex.toString().contains("500")){
                if(Sequel.menyimpantf("pcare_pendaftaran","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Gagal'","No.Urut",20,new String[]{
                    TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),lblNoRM.getText(),lblNamaPasien.getText(),NmPPK.getText(),
                    lblNokartu.getText(),kode_poli,NamaPoli.getText(),keluahan.getText(),kunjungansakit,
                    "0","0","0","0","0","0",
                    "0","0",perawatan,response.asText()
                })==true){  
                    if((!keluahan.getText().trim().equals("")))
                        Sequel.menyimpan2("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",21,new String[]{
                                TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),Sequel.cariIsi("select current_time()"),
                                "0","0"+"/"+"0","0","0","0", 
                                "0","","","Compos Mentis", keluahan.getText(),"","","0","","","","",kode_dokter
                            });    
                    }          
                JOptionPane.showMessageDialog(null,"Server PCare baru ngambek broooh. Data disimpan secara lokal, dan dapat dikirimkan kembali ke server PCare..!!");
            }else if(ex.toString().contains("401")){
                JOptionPane.showMessageDialog(null,"Username/Password salah. Lupa password? Wani piro...!");
            }else if(ex.toString().contains("408")){
                if(Sequel.menyimpantf("pcare_pendaftaran","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Gagal'","No.Urut",20,new String[]{
                     TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),lblNoRM.getText(),lblNamaPasien.getText(),NmPPK.getText(),
                    lblNokartu.getText(),kode_poli,NamaPoli.getText(),keluahan.getText(),kunjungansakit,
                    "0","0","0","0","0","0",
                    "0","0",perawatan,response.asText()
                })==true){  
                    if((!keluahan.getText().trim().equals("")))
                       Sequel.menyimpan2("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",21,new String[]{
                                TNoRw.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),Sequel.cariIsi("select current_time()"),
                                "0","0"+"/"+"0","0","0","0", 
                                "0","","","Compos Mentis", keluahan.getText(),"","","0","","","","",kode_dokter
                            });    
                } 
                JOptionPane.showMessageDialog(null,"Time out, hayati lelah baaaang. Data disimpan secara lokal, dan dapat dikirimkan kembali ke server PCare..!!");
            }else if(ex.toString().contains("424")){
                JOptionPane.showMessageDialog(null,"Ambil data masternya yang bener dong coy...!");
            }else if(ex.toString().contains("412")){
                JOptionPane.showMessageDialog(null,"Tidak sesuai kondisi. Aku, kamu end...!");
            }else if(ex.toString().contains("204")){
                JOptionPane.showMessageDialog(null,"Data tidak ditemukan...!");
            }else if(ex.toString().contains("refused")){
                JOptionPane.showMessageDialog(null,"BPJSe ngelu...!");
            }
        }
    }
}

 


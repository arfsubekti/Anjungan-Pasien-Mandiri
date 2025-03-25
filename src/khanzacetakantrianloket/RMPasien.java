/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khanzacetakantrianloket;

import bridging.BPJSCekRujukanKartuPCare;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author windiartonugroho
 */
public class RMPasien extends javax.swing.JFrame {
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    private String umur = "0", sttsumur = "Th";
    private String status = "Baru", BASENOREG = "", URUTNOREG = "", aktifjadwal = "";
    private Properties prop = new Properties();
    private int lebar = 0, tinggi = 0;
    private String validasiregistrasi=Sequel.cariIsi("select set_validasi_registrasi.wajib_closing_kasir from set_validasi_registrasi");
    /**
     * Creates new form frmUtama
     */
    public RMPasien(java.awt.Frame parent, boolean id) {
       // super(parent, id);
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

        internalFrame1 = new widget.InternalFrame();
        BtnCetak2 = new widget.Button();
        BtnCetak3 = new widget.Button();
        jLabel29 = new component.Label();
        jLabel30 = new component.Label();
        NoRMPasien = new component.TextBox();
        jSeparator1 = new javax.swing.JSeparator();
        btnAngka3 = new javax.swing.JButton();
        btnAngka6 = new javax.swing.JButton();
        btnAngka2 = new javax.swing.JButton();
        btnAngka7 = new javax.swing.JButton();
        btnAngka8 = new javax.swing.JButton();
        btnAngka9 = new javax.swing.JButton();
        btnAngka11 = new javax.swing.JButton();
        btnAngka10 = new javax.swing.JButton();
        btnAngka12 = new javax.swing.JButton();
        btnAngka17 = new javax.swing.JButton();
        btnAngka16 = new javax.swing.JButton();
        btnAngka18 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIMKES Khanza Cetak Antrian Loket");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BtnCetak2.setForeground(new java.awt.Color(15, 81, 137));
        BtnCetak2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/tombolatm.png"))); // NOI18N
        BtnCetak2.setText("BENAR");
        BtnCetak2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        BtnCetak2.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnCetak2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetak2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetak2.setIconTextGap(25);
        BtnCetak2.setMaximumSize(new java.awt.Dimension(429, 81));
        BtnCetak2.setMinimumSize(new java.awt.Dimension(429, 81));
        BtnCetak2.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetak2ActionPerformed(evt);
            }
        });
        internalFrame1.add(BtnCetak2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 330, 60));

        BtnCetak3.setForeground(new java.awt.Color(15, 81, 137));
        BtnCetak3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/tombolatm - kanan.png"))); // NOI18N
        BtnCetak3.setText("KEMBALI");
        BtnCetak3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        BtnCetak3.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnCetak3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BtnCetak3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BtnCetak3.setIconTextGap(50);
        BtnCetak3.setMaximumSize(new java.awt.Dimension(429, 81));
        BtnCetak3.setMinimumSize(new java.awt.Dimension(429, 81));
        BtnCetak3.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetak3ActionPerformed(evt);
            }
        });
        internalFrame1.add(BtnCetak3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 470, 300, 60));

        jLabel29.setForeground(new java.awt.Color(15, 81, 137));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Silahkan scan barcode / masukkan");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel29.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        jLabel29.setPreferredSize(new java.awt.Dimension(450, 75));
        internalFrame1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 470, 30));

        jLabel30.setForeground(new java.awt.Color(15, 81, 137));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("No.RM/KTP/Asuransi/JKN Pasien :");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel30.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        jLabel30.setPreferredSize(new java.awt.Dimension(450, 75));
        internalFrame1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 460, 30));

        NoRMPasien.setBorder(null);
        NoRMPasien.setForeground(new java.awt.Color(15, 81, 137));
        NoRMPasien.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        NoRMPasien.setFont(new java.awt.Font("Trebuchet MS", 3, 48)); // NOI18N
        NoRMPasien.setPreferredSize(new java.awt.Dimension(350, 75));
        NoRMPasien.setSelectedTextColor(new java.awt.Color(15, 81, 137));
        NoRMPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRMPasienActionPerformed(evt);
            }
        });
        NoRMPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMPasienKeyPressed(evt);
            }
        });
        internalFrame1.add(NoRMPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 430, 50));

        jSeparator1.setBackground(new java.awt.Color(15, 81, 137));
        jSeparator1.setForeground(new java.awt.Color(15, 81, 137));
        internalFrame1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 500, 30));

        btnAngka3.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka3.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka3.setText("1");
        btnAngka3.setBorder(null);
        btnAngka3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka3ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 60, 60));

        btnAngka6.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka6.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka6.setText("2");
        btnAngka6.setBorder(null);
        btnAngka6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka6ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 60, 60));

        btnAngka2.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka2.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka2.setText("3");
        btnAngka2.setBorder(null);
        btnAngka2.setMaximumSize(new java.awt.Dimension(44, 44));
        btnAngka2.setMinimumSize(new java.awt.Dimension(44, 44));
        btnAngka2.setPreferredSize(new java.awt.Dimension(44, 44));
        btnAngka2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka2ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 60, 60));

        btnAngka7.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka7.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka7.setText("4");
        btnAngka7.setBorder(null);
        btnAngka7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka7ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 60, 60));

        btnAngka8.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka8.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka8.setText("5");
        btnAngka8.setBorder(null);
        btnAngka8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka8ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 250, 60, 60));

        btnAngka9.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka9.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka9.setText("6");
        btnAngka9.setBorder(null);
        btnAngka9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka9ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 60, 60));

        btnAngka11.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka11.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka11.setText("7");
        btnAngka11.setBorder(null);
        btnAngka11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka11ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka11, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, 60, 60));

        btnAngka10.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka10.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka10.setText("8");
        btnAngka10.setBorder(null);
        btnAngka10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka10ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 320, 60, 60));

        btnAngka12.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka12.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka12.setText("9");
        btnAngka12.setBorder(null);
        btnAngka12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka12ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 320, 60, 60));

        btnAngka17.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        btnAngka17.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka17.setText("CLEAR");
        btnAngka17.setBorder(null);
        btnAngka17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka17ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka17, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, 60, 60));

        btnAngka16.setFont(new java.awt.Font("Trebuchet MS", 2, 24)); // NOI18N
        btnAngka16.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka16.setText("0");
        btnAngka16.setBorder(null);
        btnAngka16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka16ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka16, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 60, 60));

        btnAngka18.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        btnAngka18.setForeground(new java.awt.Color(15, 81, 137));
        btnAngka18.setText("DEL");
        btnAngka18.setBorder(null);
        btnAngka18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAngka18ActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAngka18, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, 60, 60));

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        getAccessibleContext().setAccessibleName("ANJUNGAN PASIEN MANDIRI");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void BtnCetak2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetak2ActionPerformed
this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
try {
    String noPeserta = NoRMPasien.getText();
    int count = Sequel.cariInteger("select count(pasien.no_peserta) from pasien where pasien.no_peserta='" + noPeserta + "'");
    if (count == 1) {
        PendaftaranOnsite form = new PendaftaranOnsite(null, true);
        String noRmMedis = Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_peserta='" + noPeserta + "'");
        form.setPasien(noPeserta, noRmMedis);
        form.setSize(this.getWidth(), this.getHeight());
        form.setLocationRelativeTo(jPanel1);
        this.dispose();
        form.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(rootPane, "No Rekam Medis tidak terdaftar");
    }
} finally {
    this.setCursor(Cursor.getDefaultCursor());
    
}
    }//GEN-LAST:event_BtnCetak2ActionPerformed

    private void BtnCetak3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetak3ActionPerformed
     dispose();  
    }//GEN-LAST:event_BtnCetak3ActionPerformed

    private void NoRMPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRMPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRMPasienActionPerformed

    private void NoRMPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMPasienKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           if (Sequel.cariInteger("select count(pasien.no_rkm_medis) from pasien where pasien.no_rkm_medis='" + NoRMPasien.getText() + "'") == 1) {
                PendaftaranOnsite form = new PendaftaranOnsite(null, true);
               form.setPasien(NoRMPasien.getText());
               form.setSize(this.getWidth(), this.getHeight());
               form.setLocationRelativeTo(jPanel1);
                this.dispose();
                form.setVisible(true);
            } else if (Sequel.cariInteger("select count(pasien.no_ktp) from pasien where pasien.no_ktp='" + NoRMPasien.getText() + "'") == 1) {
                PendaftaranOnsite form = new PendaftaranOnsite(null, true);
               form.setPasien(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_ktp='" + NoRMPasien.getText() + "'"));
                form.setSize(this.getWidth(), this.getHeight());
               form.setLocationRelativeTo(jPanel1);
                this.dispose();
                form.setVisible(true);
            } else if (Sequel.cariInteger("select count(pasien.no_peserta) from pasien where pasien.no_ktp='" + NoRMPasien.getText() + "'") == 1) {
                PendaftaranOnsite form = new PendaftaranOnsite(null, true);
               form.setPasien(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_ktp='" + NoRMPasien.getText() + "'"));
                form.setSize(this.getWidth(), this.getHeight());
               form.setLocationRelativeTo(jPanel1);
                this.dispose();
                form.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "No Rekam Medis tidak terdaftar ");
               NoRMPasien.setText("");
            }
         this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_NoRMPasienKeyPressed

    private void btnAngka3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka3ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "1");
    }//GEN-LAST:event_btnAngka3ActionPerformed

    private void btnAngka6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka6ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "2");
    }//GEN-LAST:event_btnAngka6ActionPerformed

    private void btnAngka2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka2ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "3");
    }//GEN-LAST:event_btnAngka2ActionPerformed

    private void btnAngka7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka7ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "4");
    }//GEN-LAST:event_btnAngka7ActionPerformed

    private void btnAngka8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka8ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "5");
    }//GEN-LAST:event_btnAngka8ActionPerformed

    private void btnAngka9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka9ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "6");
    }//GEN-LAST:event_btnAngka9ActionPerformed

    private void btnAngka11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka11ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "7");
    }//GEN-LAST:event_btnAngka11ActionPerformed

    private void btnAngka10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka10ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "8");
    }//GEN-LAST:event_btnAngka10ActionPerformed

    private void btnAngka12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka12ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "9");
    }//GEN-LAST:event_btnAngka12ActionPerformed

    private void btnAngka17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka17ActionPerformed
        int length = NoRMPasien.getText().length();
        if (length > 0) {
            NoRMPasien.setText("");
        }
    }//GEN-LAST:event_btnAngka17ActionPerformed

    private void btnAngka16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka16ActionPerformed
        NoRMPasien.setText(NoRMPasien.getText() + "0");
    }//GEN-LAST:event_btnAngka16ActionPerformed

    private void btnAngka18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAngka18ActionPerformed
        int length = NoRMPasien.getText().length();
        int number = NoRMPasien.getText().length() - 1;
        String store;
        if (length > 0) {
            StringBuilder back = new StringBuilder(NoRMPasien.getText());
            back.deleteCharAt(number);
            store = back.toString();
            NoRMPasien.setText(store);
        }
    }//GEN-LAST:event_btnAngka18ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPasien dialog = new RMPasien(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCetak2;
    private widget.Button BtnCetak3;
    private component.TextBox NoRMPasien;
    private javax.swing.JButton btnAngka10;
    private javax.swing.JButton btnAngka11;
    private javax.swing.JButton btnAngka12;
    private javax.swing.JButton btnAngka16;
    private javax.swing.JButton btnAngka17;
    private javax.swing.JButton btnAngka18;
    private javax.swing.JButton btnAngka2;
    private javax.swing.JButton btnAngka3;
    private javax.swing.JButton btnAngka6;
    private javax.swing.JButton btnAngka7;
    private javax.swing.JButton btnAngka8;
    private javax.swing.JButton btnAngka9;
    private widget.InternalFrame internalFrame1;
    private component.Label jLabel29;
    private component.Label jLabel30;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    public void setPasien(String norm, String kodepoli, String kddokter) {
    }

    private void UpdateUmur() {

    }

    private void isNumber() {
    }

}

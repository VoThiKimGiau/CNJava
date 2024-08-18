/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.KhachHangDAO;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pojo.KhachHang;

/**
 *
 * @author Admin
 */
public class frmKhachHang extends javax.swing.JFrame {

    DefaultTableModel dtm = new DefaultTableModel();
    ArrayList<KhachHang> dsKH = KhachHangDAO.layDSKH();

    /**
     * Creates new form QLCHBS
     */
    public frmKhachHang() {
        initComponents();

        this.setLocationRelativeTo(null);
        this.setTitle("Khách hàng");
        String[] tieuDe = {"Mã khách hàng", "Tên khách hàng", "Địa chỉ", "SDT", "Email"};
        dtm.setColumnIdentifiers(tieuDe);

        hienThi(dsKH);
        txtMaKH.enable(false);
        txtTenKH.enable(false);
        txtDiaChi.enable(false);
        txtSDT.enable(false);
        txtEmail.enable(false);
        tbKhachHang.setModel(dtm);
    }

    public void hienThi(ArrayList<KhachHang> dsKH) {
        dtm.setRowCount(0);
        for (KhachHang kh : dsKH) {
            Vector<Object> vec = new Vector<Object>();
            vec.add(kh.getMaKH());
            vec.add(kh.getTenKH());
            vec.add(kh.getDiaChi());
            vec.add(kh.getSdt());
            vec.add(kh.getEmail());
            dtm.addRow(vec);
        }
    }

    public boolean kiemTraNhap(int loai) {
        boolean kq = true;
        String sdt = txtSDT.getText().toString();
        String email = txtEmail.getText().toString();
        KhachHang kh = KhachHangDAO.searchSDT(sdt);
        KhachHang khE = KhachHangDAO.searchEmail(email);

        if (sdt.isEmpty()) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại không được bỏ trống. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (sdt.matches(".*\\D.*")) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại không được có chữ. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (!sdt.substring(0, 1).equals("0")) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại không bắt đầu bằng 0. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (sdt.length() < 10) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại không đủ 10 số. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (sdt.length() > 10) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại hơn 10 số. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (kh.getMaKH() != null && txtMaKH.getText().toString().isEmpty() && loai == 0) {// loại = 0 là không phải tìm kiếm
            kq = false;
            JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại. Vui lòng nhập lại!");
            txtSDT.requestFocus();
        } else if (txtTenKH.getText().isEmpty() && loai == 0) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được bỏ trống. Vui lòng nhập lại!");
            txtTenKH.requestFocus();
        } else if (txtDiaChi.getText().toString().isEmpty() && loai == 0) {
            kq = false;
            txtDiaChi.setText("0");
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống. Nếu mua tại chỗ không cần giao vui lòng nhập 0.\nVui lòng nhập lại!");
            return kq;
        } else if (khE.getMaKH() == null || khE.getEmail().equals("")) {
            kq = true;
            return kq;
        } else if (!email.contains("@")) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Emal phải có @. Vui lòng nhập lại!");
            txtEmail.requestFocus();
        } else if (email.length() == 1) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Email phải có kí tự khác ngoài @. Vui lòng nhập lại!");
            txtEmail.requestFocus();
        } else if (khE.getMaKH() != null && txtMaKH.getText().toString().isEmpty()) {
            kq = false;
            JOptionPane.showMessageDialog(this, "Email đã tồn tại. Vui lòng nhập lại!");
            txtEmail.requestFocus();
        }

        return kq;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKhachHang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("KHÁCH HÀNG");

        tbKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbKhachHang);

        jLabel2.setText("Mã khách hàng");

        jLabel3.setText("Tên khách hàng");

        jLabel4.setText("Địa chỉ");

        jLabel5.setText("Số điện thoại");

        jLabel6.setText("Email");

        btnInsert.setText("Thêm");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnSave.setText("Lưu");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnMenu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(314, 314, 314))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtSDT, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(txtEmail)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnSave)
                                        .addGap(68, 68, 68)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnMenu))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInsert))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate)
                    .addComponent(jLabel3)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete)
                    .addComponent(btnSave)
                    .addComponent(btnSearch))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        hienThi(dsKH);
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtTenKH.enable(true);
        txtDiaChi.enable(true);
        txtSDT.enable(true);
        txtEmail.enable(true);
        txtSDT.requestFocus();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        hienThi(dsKH);
        txtTenKH.enable(true);
        txtDiaChi.enable(true);
        txtSDT.enable(true);
        txtEmail.enable(true);
        txtSDT.requestFocus();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        int row = tbKhachHang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa");
        } else {
            int n = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.NO_OPTION) {
                return;
            } else {
                if (KhachHangDAO.deleteKH(txtMaKH.getText().toString())) {
                    dsKH = KhachHangDAO.layDSKH();
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại\nNếu muốn xóa khách hàng phải xóa các đơn hàng của khách hàng đó. Vui lòng thử lại");
                }
                hienThi(dsKH);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");

        txtTenKH.enable(false);
        txtDiaChi.enable(false);
        txtEmail.enable(false);
        txtSDT.enable(true);
        txtSDT.requestFocus();

        if (txtSDT.getText().isEmpty())
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại");
        else {
            if (kiemTraNhap(1)) {
                ArrayList<KhachHang> lst = new ArrayList<>();
                KhachHang kh = KhachHangDAO.searchSDT(txtSDT.getText().toString());
                if (kh.getMaKH() == null) {
                    JOptionPane.showMessageDialog(this, "Không có khách hàng có số điện thoại này!");
                } else {
                    lst.add(kh);
                    hienThi(lst);
                }
            }
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tbKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKhachHangMouseClicked
        // TODO add your handling code here:

        int row = tbKhachHang.getSelectedRow();
        txtMaKH.setText(String.valueOf(tbKhachHang.getValueAt(row, 0)));
        txtTenKH.setText(String.valueOf(tbKhachHang.getValueAt(row, 1)));
        txtDiaChi.setText(String.valueOf(tbKhachHang.getValueAt(row, 2)));
        txtSDT.setText(String.valueOf(tbKhachHang.getValueAt(row, 3)));
        txtEmail.setText(String.valueOf(tbKhachHang.getValueAt(row, 4)));

        txtTenKH.enable(false);
        txtDiaChi.enable(false);
        txtSDT.enable(false);
        txtEmail.enable(false);
    }//GEN-LAST:event_tbKhachHangMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        hienThi(dsKH);
        KhachHang kh = new KhachHang();
        kh.setTenKH(txtTenKH.getText().toString());
        kh.setDiaChi(txtDiaChi.getText().toString());
        kh.setSdt(txtSDT.getText().toString());
        kh.setEmail(txtEmail.getText().toString());

        if (txtMaKH.getText().isEmpty()) {
            if (kiemTraNhap(0)) {
                if (KhachHangDAO.themKH(kh)) {
                    dsKH = KhachHangDAO.layDSKH();
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thàng công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại. Vui lòng thử lại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            if (kiemTraNhap(0)) {
                kh.setMaKH(txtMaKH.getText().toString());
                if (KhachHangDAO.updateKH(kh)) {
                    dsKH = KhachHangDAO.layDSKH();
                    JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thàng công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại. Vui lòng thử lại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        hienThi(dsKH);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        new frmMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMenuActionPerformed

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
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmKhachHang().setVisible(false);
                new frmDangNhap(new JFrame(), true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbKhachHang;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenKH;
    // End of variables declaration//GEN-END:variables
}

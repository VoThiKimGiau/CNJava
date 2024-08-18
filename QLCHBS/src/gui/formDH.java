/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import pojo.KhachHang;
import dao.DonHangDAO;
import dao.SachDAO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import pojo.DonHang;
import pojo.Sach;

/**
 *
 * @author TPUS
 */
public class formDH extends javax.swing.JFrame {

    /**
     * Creates new form formDH
     */
    public formDH() {
        initComponents();
        this.setLocationRelativeTo(null);
        DonHangDAO dh = new DonHangDAO();
        loadTableSP();
        loadTableHoadon();
        loadComboBoxData();
        txtSDTNV.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String sdtNV = txtSDTNV.getText().trim();
                // Gọi phương thức để lấy mã nhân viên từ CSDL dựa trên số điện thoại
                String maNV = dh.layMaNhanVienTuSDT(sdtNV);
                txtMaNV.setText(maNV);
            }
        });

        txtSDT.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String sdtKH = txtSDT.getText().trim();
                // Gọi phương thức để lấy thông tin khách hàng từ CSDL dựa trên số điện thoại
                KhachHang khachHang = dh.layThongTinKhachHangTuSDT(sdtKH);
                if (khachHang != null) {
                    txtTenkh.setText(khachHang.getTenKH());
                    txtDiachi.setText(khachHang.getDiaChi());
                }
            }
        });
        TableSPCT.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        // Xóa hàng được chọn
                        ((DefaultTableModel) TableSPCT.getModel()).removeRow(row);
                        // Cập nhật lại SLKho nếu cần
                        capNhatSLKhoTuTableSPCT();
                    }
                }
            }
        });

        TableSPCT.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("tableCellEditor".equals(evt.getPropertyName())) {
                    if (!TableSPCT.isEditing()) {
                        int row = TableSPCT.getSelectedRow();
                        int column = TableSPCT.getSelectedColumn();
                        // Kiểm tra xem cột được sửa có phải là cột giá không
                        if (column == 2) {
                            Object giaMoi = TableSPCT.getValueAt(row, column);
                            // Cập nhật giá mới vào cơ sở dữ liệu
                            // ...
                        }
                    }
                }
            }
        });
    }

    private void loadComboBoxData() {
        ArrayList<String> dsDM = DonHangDAO.layDanhSachTenDM();
        cboLoai.removeAllItems();
        for (String tenDM : dsDM) {
            cboLoai.addItem(tenDM);
        }

    }

    private void loadTableSP() {
        DonHangDAO dh = new DonHangDAO();
        SachDAO sach = new SachDAO();
        List<Sach> listsach = sach.getAllBooks();
        DefaultTableModel model = (DefaultTableModel) TableSP.getModel();
        model.setRowCount(0); // Clear table

        for (Sach s : listsach) {
            Object[] row = {
                s.getMaSach(),
                s.getTenSach(),
                dh.layTenDanhMucBangMaDanhMuc(s.getMaDanhMuc()),
                s.getSlKho(),
                s.getGiaBan()
            };
            model.addRow(row);
        }
    }

    private void loadTableHoadon() {
        DonHangDAO dh = new DonHangDAO();
        ArrayList<DonHang> listDH = dh.layDanhSachDonHang();

        DefaultTableModel model = (DefaultTableModel) TableHoadon.getModel();
        model.setRowCount(0); // Clear table

        for (DonHang s : listDH) {
            String trangThaiStr = s.isTrangThai() ? "đã thanh toán" : "chưa thanh toán";

            Object[] row = {
                s.getMaDH(),
                s.getNgayDat(),
                s.getTongTien(),
                trangThaiStr
            };
            model.addRow(row);
        }
    }

    private void themSanPhamVaoTableSPCT(String maSach, int soluongNhap, float giaban) {
        DefaultTableModel model = (DefaultTableModel) TableSPCT.getModel();
        int rowCount = model.getRowCount();
        boolean found = false;

        for (int i = 0; i < rowCount; i++) {
            String existingMaSach = (String) model.getValueAt(i, 0);
            if (existingMaSach.equals(maSach)) {
                int existingSoluongNhap = (int) model.getValueAt(i, 1);
                float existingTong = (float) model.getValueAt(i, 2);

                int updatedSoluongNhap = existingSoluongNhap + soluongNhap;
                float updatedTong = existingTong + (giaban * soluongNhap);

                model.setValueAt(updatedSoluongNhap, i, 1);
                model.setValueAt(updatedTong, i, 2);

                found = true;
                break;
            }
        }

        if (!found) {
            float tong = giaban * soluongNhap;
            model.addRow(new Object[]{maSach, soluongNhap, tong});
        }
    }

    // Hàm cập nhật số lượng tồn kho dựa trên dữ liệu trong TableSPCT
    public void capNhatSLKhoTuTableSPCT() {
        DonHangDAO dh = new DonHangDAO();
        String maSach = txtMasp.getText().trim();
        int soLuongNhap = Integer.parseInt(txtSoluong.getText().trim());
        dh.capNhatSLKho(maSach, soLuongNhap);
    }

    private float tinhTongTien() {
        DefaultTableModel model = (DefaultTableModel) TableSPCT.getModel();
        int rowCount = model.getRowCount();
        float tongTien = 0;

        for (int i = 0; i < rowCount; i++) {
            float gia = (float) model.getValueAt(i, 2);
            tongTien += gia;
        }
        return tongTien;
    }

    private void luuHoaDon(String maDH) throws Exception {
        DonHangDAO dhDao = new DonHangDAO();
        DonHang dh = dhDao.layDonHang(maDH);
        float tongTien = 0;
        DefaultTableModel model = (DefaultTableModel) TableSPCT.getModel();
        int rowCount = model.getRowCount();

        StringBuilder hoaDonContent = new StringBuilder();
        hoaDonContent.append("Hóa Đơn\n");
        hoaDonContent.append("Mã đơn hàng: ").append(dh.getMaDH()).append("\n");
        hoaDonContent.append("Tên khách hàng: ").append(txtTenkh.getText()).append("\n");
        hoaDonContent.append("Số điện thoại: ").append(txtSDT.getText()).append("\n");
        hoaDonContent.append("Địa chỉ: ").append(txtDiachi.getText()).append("\n");
        hoaDonContent.append("Danh sách sản phẩm\n");
        for (int i = 0; i < rowCount; i++) {
            String ma = (String) model.getValueAt(i, 0);
            hoaDonContent.append("Sách: ").append(dhDao.layTenSPBangMaSP(ma)).append(" ");
            int soluong = (int) model.getValueAt(i, 1);
            hoaDonContent.append("số lượng: ").append(soluong).append(" ");
            float gia = (float) model.getValueAt(i, 2);
            hoaDonContent.append("Giá: ").append(gia).append("\n");
            tongTien += gia;
        }
        hoaDonContent.append("Thành tiền: ").append(tongTien).append("\n");
        String fileName = "hoa_don_" + maDH + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(hoaDonContent.toString());
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

        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMasp = new javax.swing.JTextField();
        txtTensp = new javax.swing.JTextField();
        txtSoluong = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cboLoai = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableSP = new javax.swing.JTable();
        txtSDTNV = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableSPCT = new javax.swing.JTable();
        txtMaNV = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableHoadon = new javax.swing.JTable();
        btnThemHD = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTenkh = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtDiachi = new javax.swing.JTextField();
        btnThemSP = new javax.swing.JButton();
        btnXoaHD = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel13.setText("Nhân viên thực hiện");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Mã sản phẩm");

        jLabel7.setText("Tên sản phẩm");

        jLabel9.setText("Số lượng");

        txtMasp.setEditable(false);

        txtTensp.setEditable(false);
        txtTensp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenspActionPerformed(evt);
            }
        });

        txtSoluong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoluongActionPerformed(evt);
            }
        });

        jLabel10.setText("Loại");

        cboLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel14.setText("Giá");

        txtGia.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtMasp, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel10)
                        .addGap(27, 27, 27)
                        .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(28, 28, 28)
                        .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtTensp)
                        .addGap(97, 97, 97)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtMasp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtTensp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)), "Bảng sản phẩm"));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        TableSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại", "Số lượng", "Giá"
            }
        ));
        TableSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableSPMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableSP);

        jLabel15.setText("Mã nhân viên");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)), "Chi tiết sản phẩm"));

        TableSPCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Số lượng", "Giá"
            }
        ));
        jScrollPane2.setViewportView(TableSPCT);

        txtMaNV.setEditable(false);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)), "Bảng hóa đơn"));

        TableHoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Ngày đặt", "Thành tiền", "Trạng thái"
            }
        ));
        jScrollPane3.setViewportView(TableHoadon);

        btnThemHD.setText("Thêm hóa đơn");
        btnThemHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemHDActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("QUẢN LÝ ĐƠN HÀNG");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("Tên khách hàng");

        jLabel3.setText("Số điện thoại");

        jLabel4.setText("Địa chỉ");

        txtTenkh.setEditable(false);

        txtDiachi.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtTenkh, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(40, 40, 40)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtDiachi)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenkh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiachi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnThemSP.setText("Thêm sản phẩm");
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        btnXoaHD.setText("Xóa hóa đơn");
        btnXoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHDActionPerformed(evt);
            }
        });

        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(255, 102, 102));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(txtSDTNV, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(btnThemHD)
                        .addGap(53, 53, 53)
                        .addComponent(btnThemSP)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnXoaHD)
                                .addGap(57, 57, 57)
                                .addComponent(btnLuu)
                                .addGap(269, 269, 269))
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnMenu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(423, 423, 423)))
                .addGap(30, 30, 30))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(667, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnMenu))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDTNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(16, 16, 16)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSP)
                    .addComponent(btnThemHD)
                    .addComponent(btnXoaHD)
                    .addComponent(btnLuu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(96, 96, 96)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(431, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTenspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenspActionPerformed

    private void txtSoluongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoluongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoluongActionPerformed

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed

        DonHangDAO dh = new DonHangDAO();
        String maSach = txtMasp.getText().trim();
        String sdtNV = txtSDTNV.getText().trim();
        String sdtKH = txtSDT.getText().trim();
       
        float giaban = Float.parseFloat(txtGia.getText().trim());
        int soLuong = Integer.parseInt(txtSoluong.getText().trim());

        if (!dh.kiemtraSoDienThoai(sdtNV)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại nhân viên không hợp lệ");
            return;
        }
        if (!dh.kiemtraSoDienThoai(sdtKH)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại khách hàng không hợp lệ");
            return;
        }

        

        if (!dh.kiemTraSoLuong(soLuong)) {
            JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ");
            return;
        }

        if (!dh.kiemTraSoLuongNhap(soLuong, maSach)) {
            JOptionPane.showMessageDialog(null, "Số lượng nhập không vượt quá số lượng trong kho");
            return;
        }

        themSanPhamVaoTableSPCT(maSach, soLuong, giaban);
        capNhatSLKhoTuTableSPCT();
        loadTableSP();
    }//GEN-LAST:event_btnThemSPActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        DonHangDAO dh = new DonHangDAO();
        float tongTien = tinhTongTien();
        String maKH = dh.layMaKHTuSDT(txtSDT.getText());
        String maNV = txtMaNV.getText();
        String maDH = dh.taoMaDHTuDong();

        if (dh.themDonHang(maDH, maKH, maNV, tongTien)) {
            JOptionPane.showMessageDialog(this, "Thêm đơn hàng thành công");
            loadTableHoadon();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm đơn hàng thất bại");
        }

    }//GEN-LAST:event_btnLuuActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void TableSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableSPMouseClicked
        // TODO add your handling code here:

        // Lấy thông tin sản phẩm được chọn
        int selectedRow = TableSP.getSelectedRow();
        String maSach = TableSP.getValueAt(selectedRow, 0).toString();
        String tenSach = TableSP.getValueAt(selectedRow, 1).toString();
        String loai = TableSP.getValueAt(selectedRow, 2).toString();
        int slKho = Integer.parseInt(TableSP.getValueAt(selectedRow, 3).toString());
        Float gia = Float.parseFloat(TableSP.getValueAt(selectedRow, 4).toString());

        // Cập nhật thông tin sản phẩm được chọn vào các trường nhập liệu
        txtMasp.setText(maSach);
        txtTensp.setText(tenSach);
        txtGia.setText(String.valueOf(gia));
        cboLoai.setSelectedItem(loai);

    }//GEN-LAST:event_TableSPMouseClicked

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        int selectedRow = TableHoadon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 hóa đơn để thanh toán");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) TableHoadon.getModel();
        String maDH = (String) model.getValueAt(selectedRow, 0);

        DonHangDAO dh = new DonHangDAO();
        dh.capNhatTrangThaiDonHang(maDH, true);

        loadTableHoadon();

        try{
            luuHoaDon(maDH);
            JOptionPane.showMessageDialog(null, "Thanh toán thành công");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thanh toán: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnXoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHDActionPerformed
        // TODO add your handling code here:
          int row = TableHoadon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "hãy chọn đơn hàng cần xóa!");
            return;
        }
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đơn hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            String maDH = TableHoadon.getValueAt(row, 0).toString();

            if (DonHangDAO.xoaDonHang(maDH)) {
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng thành công!");
                loadTableHoadon();
                
            } else {
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng thất bại!");
            }
        }
    }//GEN-LAST:event_btnXoaHDActionPerformed

    private void btnThemHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemHDActionPerformed
        // TODO add your handling code here:
        this.txtDiachi.setText("");
        this.txtSDT.setText("");
        this.txtTenkh.setText("");
        this.txtMasp.setText("");
        this.txtTensp.setText("");
        this.txtSoluong.setText("");
        this.txtGia.setText("");
        this.cboLoai.setSelectedIndex(0);
    }//GEN-LAST:event_btnThemHDActionPerformed

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
            java.util.logging.Logger.getLogger(formDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formDH().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableHoadon;
    private javax.swing.JTable TableSP;
    private javax.swing.JTable TableSPCT;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThemHD;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnXoaHD;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtDiachi;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMasp;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDTNV;
    private javax.swing.JTextField txtSoluong;
    private javax.swing.JTextField txtTenkh;
    private javax.swing.JTextField txtTensp;
    // End of variables declaration//GEN-END:variables
}

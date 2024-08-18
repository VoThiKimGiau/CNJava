/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmDangNhap;
import gui.frmPhieuNhap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.PhieuNhap;

/**
 *
 * @author Admin
 */
public class PhieuNhapDAO {

    public static ArrayList<PhieuNhap> layDSPN() {
        ArrayList<PhieuNhap> ds = new ArrayList<PhieuNhap>();
        try {
            String sql = "Select * from PhieuNhap";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPhieu(rs.getString("MaPhieu"));
                pn.setMaNCC(rs.getString("MaNCC"));
                pn.setMaNV(rs.getString("MaNV"));
                pn.setNgayNhap(rs.getDate("NgayNhap"));
                pn.setTongTien(rs.getFloat("TongTien"));

                ds.add(pn);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmPhieuNhap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ds;
    }

    public static boolean themPN(PhieuNhap pn) {
        boolean kq = false;

        String maPN = layMaPNCuoi();
        int stt = Integer.parseInt(maPN.substring(maPN.length() - 3));
        String newMaPN = String.format("PN%03d", stt + 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ngayNhapStr = dateFormat.format(pn.getNgayNhap());

        String sql = String.format(Locale.US, "insert into PhieuNhap(MaPhieu, MaNCC, MaNV, NgayNhap, TongTien) values ('%s', '%s','%s', '%s', %f)", newMaPN, pn.getMaNCC(), frmDangNhap.account.getMaNV(), ngayNhapStr, pn.getTongTien());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean updatePN(PhieuNhap pn) {
        boolean kq = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ngayNhapStr = dateFormat.format(pn.getNgayNhap());

        String sql = String.format(Locale.US, "update PhieuNhap set MaNCC = '%s', NgayNhap = '%s', TongTien = %f where MaPhieu = '%s'", pn.getMaNCC(), ngayNhapStr, pn.getTongTien(), pn.getMaPhieu());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean deletePN(String maPN) {
        boolean kq = false;

        String sql = String.format("delete from PhieuNhap where MaPhieu = '%s'", maPN);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static String layTenNCC(String maNCC) {
        String tenNCC = "";

        String sql = "Select TenNCC from PhieuNhap pn join NhaCungCap ncc on pn.MaNCC = ncc.MaNCC where pn.MaNCC = '" + maNCC + "'";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                tenNCC = rs.getString("TenNCC");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return tenNCC;
    }

    public static String layMaPNCuoi() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "SELECT TOP 1 MaPhieu FROM PhieuNhap ORDER BY MaPhieu DESC";
        String kq = "";

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = rs.getString("MaPhieu");
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return kq;
    }

    public static ArrayList<String> getDataForCombobox() {
        ArrayList<String> kq = new ArrayList<>();

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "Select distinct TenNCC from NhaCungCap";

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                kq.add(rs.getString("TenNCC"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return kq;
    }

    public static String layMaNCCTuCbo(String tenNCC) {
        String maNCC = "";

        String sql = "Select ncc.MaNCC from PhieuNhap pn join NhaCungCap ncc on pn.MaNCC = ncc.MaNCC where TenNCC = N'" + tenNCC + "'";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                maNCC = rs.getString("MaNCC");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return maNCC;
    }

    public static ArrayList<PhieuNhap> searchNgayNhap(String ngayNhap) throws SQLException {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ArrayList<PhieuNhap> dsPN = new ArrayList<>();

        String sql = "Select * from PhieuNhap where NgayNhap = '" + ngayNhap + "'";
        ResultSet rs = provider.executeQuery(sql);

        while (rs.next()) {
            PhieuNhap pn = new PhieuNhap();
            pn.setMaPhieu(rs.getString("MaPhieu"));
            pn.setMaNCC(rs.getString("MaNCC"));
            pn.setNgayNhap(rs.getDate("NgayNhap"));
            pn.setTongTien(rs.getFloat("TongTien"));

            dsPN.add(pn);
        }

        provider.Close();
        return dsPN;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmNhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.NhanVien;

/**
 *
 * @author Admin
 */
public class NhanVienDAO {
    public static ArrayList<NhanVien> layDSNV() {
        ArrayList<NhanVien> dsNV = new ArrayList<NhanVien>();
        try {
            String sql = "Select * from NhanVien";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSdt(rs.getString("SDT"));
                nv.setChucVu(rs.getString("ChucVu"));

                dsNV.add(nv);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmNhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsNV;
    }

    public static boolean themNV(NhanVien nv) {
        boolean kq = false;

        String maNV = layMaNVCuoi();
        int stt = Integer.parseInt(maNV.substring(maNV.length() - 3));
        String newMaNV = String.format("NV%03d", stt + 1);

        String sql = String.format("insert into NhanVien(MaNV, TenNV, DiaChi, SDT, ChucVu) values ('%s', N'%s', N'%s', '%s', N'%s')", newMaNV, nv.getTenNV(), nv.getDiaChi(), nv.getSdt(), nv.getChucVu());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean updateNV(NhanVien nv) {
        boolean kq = false;

        String sql = String.format("update NhanVien set TenNV = N'%s', DiaChi = N'%s', SDT = '%s', ChucVu = N'%s' where MaNV = '%s'", nv.getTenNV(), nv.getDiaChi(), nv.getSdt(), nv.getChucVu(), nv.getMaNV());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean deleteNV(String maNV) {
        boolean kq = false;

        String sql = String.format("delete from NhanVien where MaNV = '%s'", maNV);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean kiemTraMaNV(String maNV) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "select MaNV from NhanVien where MaNV = '" + maNV + "'";
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                provider.Close();
                return true;// Có rồi
            } else {
                provider.Close();
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return false;
    }

    public static String layMaNVCuoi() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "SELECT TOP 1 MaNV FROM NhanVien ORDER BY MaNV DESC";
        String kq = "";

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = rs.getString("MaNV");
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }

    public static NhanVien searchSDT(String sdt) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        NhanVien nv = new NhanVien();
        String sql = String.format("select * from NhanVien where SDT = '%s'", sdt);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSdt(rs.getString("SDT"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        provider.Close();
        return nv;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmKhachHang;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.KhachHang;

/**
 *
 * @author Admin
 */
public class KhachHangDAO {

    public static ArrayList<KhachHang> layDSKH() {
        ArrayList<KhachHang> dsKH = new ArrayList<KhachHang>();
        try {
            String sql = "Select * from KhachHang";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));

                dsKH.add(kh);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsKH;
    }

    public static boolean themKH(KhachHang kh) {
        boolean kq = false;

        String maKH = layMaKHCuoi();
        int stt = Integer.parseInt(maKH.substring(maKH.length() - 3));
        String newMaKH = String.format("KH%03d", stt + 1);

        String sql = String.format("insert into KhachHang(MaKH, TenKH, DiaChi, SDT, Email) values ('%s', N'%s', N'%s', '%s', '%s')", newMaKH, kh.getTenKH(), kh.getDiaChi(), kh.getSdt(), kh.getEmail());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean updateKH(KhachHang kh) {
        boolean kq = false;

        String sql = String.format("update KhachHang set TenKH = N'%s', DiaChi = N'%s', SDT = '%s', Email = '%s' where MaKH = '%s'", kh.getTenKH(), kh.getDiaChi(), kh.getSdt(), kh.getEmail(), kh.getMaKH());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean deleteKH(String maKH) {
        boolean kq = false;

        String sql = String.format("delete from KhachHang where MaKH = '%s'", maKH);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean kiemTraMaKH(String maKH) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "select MaKH from KhachHang where MaKH = '" + maKH + "'";
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
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return false;
    }

    public static String layMaKHCuoi() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY MaKH DESC";
        String kq = "";

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = rs.getString("MaKH");
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }

    public static KhachHang searchSDT(String sdt) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        KhachHang kh = new KhachHang();
        String sql = String.format("select * from KhachHang where SDT = '%s'", sdt);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        provider.Close();
        return kh;
    }
    
    public static KhachHang searchEmail(String email) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        KhachHang kh = new KhachHang();
        String sql = String.format("select * from KhachHang where Email = '%s'", email);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        provider.Close();
        return kh;
    }
}

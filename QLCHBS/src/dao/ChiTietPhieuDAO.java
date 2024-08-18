/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmCTPN;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.ChiTietPhieuNhap;

/**
 *
 * @author Admin
 */
public class ChiTietPhieuDAO {

    public static ArrayList<ChiTietPhieuNhap> layDSCTPN() {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<ChiTietPhieuNhap>();
        try {
            String sql = "Select * from ChiTietPhieuNhap";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                ct.setMaPhieu(rs.getString("MaPhieu"));
                ct.setMaSach(rs.getString("MaSach"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setGiaNhap(rs.getFloat("GiaNhap"));

                ds.add(ct);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmCTPN.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ds;
    }

    public static boolean themCTPN(ChiTietPhieuNhap ct) {
        boolean kq = false;

        String sql = String.format(Locale.US, "insert into ChiTietPhieuNhap(MaPhieu, MaSach, SoLuong, GiaNhap) values ('%s', '%s', %d, %f)", ct.getMaPhieu(), ct.getMaSach(), ct.getSoLuong(), ct.getGiaNhap());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean updateCTPN(ChiTietPhieuNhap ct) {
        boolean kq = false;

        String sql = String.format(Locale.US, "update ChiTietPhieuNhap set SoLuong = %d, GiaNhap = %f where MaPhieu = '%s' and MaSach = '%s'", ct.getSoLuong(), ct.getGiaNhap(), ct.getMaPhieu(), ct.getMaSach());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean deleteCTPN(String maCT, String maSach) {
        boolean kq = false;

        String sql = String.format("delete from ChiTietPhieuNhap where MaPhieu = '%s' and MaSach = '%s'", maCT, maSach);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static String layTenSach(String maSach) {
        String tenSach = "";

        String sql = "Select TenSach from ChiTietPhieuNhap ct join Sach s on ct.MaSach = s.MaSach where ct.MaSach = '" + maSach + "'";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                tenSach = rs.getString("TenSach");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return tenSach;
    }

    public static ArrayList<String> getDataForCBOSach() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ArrayList<String> ds = new ArrayList<>();
        String sql = "Select distinct TenSach from Sach";

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                ds.add(rs.getString("TenSach"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ds;
    }

    public static ArrayList<String> getDataForCBOMaPhieu() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ArrayList<String> ds = new ArrayList<>();
        String sql = "Select distinct MaPhieu from PhieuNhap";

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                ds.add(rs.getString("MaPhieu"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ds;
    }

    public static String layMaSachTuCBO(String tenSach) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String maSach = "";
        String sql = "Select s.MaSach from ChiTietPhieuNhap ct join Sach s on ct.MaSach = s.MaSach where TenSach = N'" + tenSach + "'";

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                maSach = rs.getString("MaSach");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return maSach;
    }

    public static ArrayList<ChiTietPhieuNhap> searchMaPN(String maPN) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();
        String sql = "Select * from ChiTietPhieuNhap where MaPhieu = '" + maPN + "'";

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                ct.setMaPhieu(rs.getString("MaPhieu"));
                ct.setMaSach(rs.getString("MaSach"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setGiaNhap(rs.getFloat("GiaNhap"));

                ds.add(ct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return ds;
    }

    public static boolean ktrCTPN(String maPhieu, String maSach) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        boolean kq = false;
        String sql = "Select * from ChiTietPhieuNhap where MaPhieu = '" + maPhieu + "' and MaSach = '" + maSach + "'";
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            while(rs.next())
                kq = true; // Đã có
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kq;
    }
}

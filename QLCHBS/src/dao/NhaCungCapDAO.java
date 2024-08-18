/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmNhaCungCap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.NhaCungCap;

/**
 *
 * @author Admin
 */
public class NhaCungCapDAO {
    public static ArrayList<NhaCungCap> layDSNCC() {
        ArrayList<NhaCungCap> dsNCC = new ArrayList<NhaCungCap>();
        try {
            String sql = "Select * from NhaCungCap";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));

                dsNCC.add(ncc);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmNhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsNCC;
    }

    public static boolean themNCC(NhaCungCap ncc) {
        boolean kq = false;

        String maNCC = layMaNCCCuoi();
        int stt = Integer.parseInt(maNCC.substring(maNCC.length() - 2));
        String newMaNCC = String.format("NCC%02d", stt + 1);

        String sql = String.format("insert into NhaCungCap(MaNCC, TenNCC, DiaChi, SDT, Email) values ('%s', N'%s', N'%s', '%s', '%s')", newMaNCC, ncc.getTenNCC(), ncc.getDiaChi(), ncc.getSdt(), ncc.getEmail());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean updateNCC(NhaCungCap ncc) {
        boolean kq = false;

        String sql = String.format("update NhaCungCap set TenNCC = N'%s', DiaChi = N'%s', SDT = '%s', Email = '%s' where MaNCC = '%s'", ncc.getTenNCC(), ncc.getDiaChi(), ncc.getSdt(), ncc.getEmail(), ncc.getMaNCC());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean deleteNCC(String maNCC) {
        boolean kq = false;

        String sql = String.format("delete from NhaCungCap where MaNCC = '%s'", maNCC);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }

    public static boolean kiemTraMaNCC(String maNCC) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "select MaNCC from NhaCungCap where MaNCC = '" + maNCC + "'";
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
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return false;
    }

    public static String layMaNCCCuoi() {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "SELECT TOP 1 MaNCC FROM NhaCungCap ORDER BY MaNCC DESC";
        String kq = "";

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = rs.getString("MaNCC");
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }

    public static NhaCungCap searchSDT(String sdt) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        NhaCungCap ncc = new NhaCungCap();
        String sql = String.format("select * from NhaCungCap where SDT = '%s'", sdt);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        provider.Close();
        return ncc;
    }
    
    public static NhaCungCap searchEmail(String email) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        NhaCungCap ncc = new NhaCungCap();
        String sql = String.format("select * from NhaCungCap where Email = '%s'", email);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        provider.Close();
        return ncc;
    }
}

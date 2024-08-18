/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmDangNhap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.DangNhap;

/**
 *
 * @author ASUS
 */
public class DangNhapDao {

   
    public static ArrayList<DangNhap> layDSDN() {
        ArrayList<DangNhap> dsDN = new ArrayList<DangNhap>();
        try {
            String sql = "Select * from PQ";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                DangNhap dn = new DangNhap();
                dn.setMaNV(rs.getString("MaNV"));
                dn.setTenDN(rs.getString("username"));
                dn.setMK(rs.getString("pass"));
                dn.setTenND(rs.getString("typeofuser"));

                dsDN.add(dn);
            }
            provider.Close();
        } catch (SQLException ex) {
            Logger.getLogger(frmDangNhap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsDN;
    }

    public static boolean KtraDN(String TK, String MK) {

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = String.format("SELECT * FROM PQ WHERE username = '%s' AND pass = '%s'", TK, MK);
        boolean kq = false;

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                DangNhap dn = new DangNhap();
                dn.setMaNV(rs.getString("MaNV"));
                frmDangNhap.account = dn;
                kq = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }
   
    public static boolean KtraND(String TK) {
        String TenND = "";
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = String.format("SELECT * FROM PQ WHERE username = '%s'", TK);
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                TenND = rs.getString("typeofuser").trim();
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        
        if(TenND.equals("Admin")) {
            
            return true;
        } else {
            
            return false;
        }

    }
    public static boolean deleteNV(String manv) {
        boolean kq = false;
        String sql = String.format("delete from PQ where MaNV = '%s'", manv);

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }
    public static boolean KtraID(String id)
    {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = String.format("SELECT * FROM PQ WHERE MaNV = '%s'", id);
        
        boolean kq = false;

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }
    public static boolean KtraNVTuNhanVien(String maNV)
    {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = String.format("SELECT * FROM NhanVien WHERE MaNV = '%s'", maNV);
        
        boolean kq = false;

        try {
            ResultSet rs = provider.executeQuery(sql);
            if (rs.next()) {
                kq = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        provider.Close();
        return kq;
    }
    
    public static boolean themNV(DangNhap dn) {
        boolean kq = false;
        
        String sql = String.format("insert into PQ(MaNV, username, pass, typeofuser) values ( '%s', '%s', '%s', '%s')", dn.getMaNV(), dn.getTenDN(), dn.getMK(), dn.getTenND());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }
    
    public static boolean updateNV(DangNhap dn) {
        boolean kq = false;

        String sql = String.format("update PQ set username = '%s', pass = '%s', typeofuser = '%s' where MaNV = '%s'", dn.getTenDN(), dn.getMK(),dn.getTenND(), dn.getMaNV());

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        int n = provider.executeUpdate(sql);
        if (n == 1) {
            kq = true;
        }

        provider.Close();
        return kq;
    }
}

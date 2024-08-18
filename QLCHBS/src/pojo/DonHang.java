package pojo;

import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author TPUS
 */
public class DonHang {
    String MaDH;
    String NgayDat;
    int TongTien;
    String MaKH;
    String MaNV;
    boolean TrangThai;
    

    public DonHang() {
    }

    public DonHang(String MaDH, String NgayDat, int TongTien, String MaKH, String MaNV, boolean TrangThai) {
        this.MaDH = MaDH;
        this.NgayDat = NgayDat;
        this.TongTien = TongTien;
        this.MaKH = MaKH;
        this.MaNV = MaNV;
        this.TrangThai = TrangThai;
    }

   

    public String getMaDH() {
        return MaDH;
    }

    public void setMaDH(String MaDH) {
        this.MaDH = MaDH;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String NgayDat) {
        this.NgayDat = NgayDat;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int TongTien) {
        this.TongTien = TongTien;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String MaKH) {
        this.MaKH = MaKH;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    
    
    
}

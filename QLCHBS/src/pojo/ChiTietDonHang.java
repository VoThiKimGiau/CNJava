/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author TPUS
 */
public class ChiTietDonHang {
    String MaCTDH;
    String MaDH;
    String MaSach;
    int SoLuong;
    float GiaBan;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(String MaCTDH, String MaDH, String MaSach, int SoLuong, float GiaBan) {
        this.MaCTDH = MaCTDH;
        this.MaDH = MaDH;
        this.MaSach = MaSach;
        this.SoLuong = SoLuong;
        this.GiaBan = GiaBan;
    }

    public String getMaCTDH() {
        return MaCTDH;
    }

    public void setMaCTDH(String MaCTDH) {
        this.MaCTDH = MaCTDH;
    }

    public String getMaDH() {
        return MaDH;
    }

    public void setMaDH(String MaDH) {
        this.MaDH = MaDH;
    }

    public String getMaSach() {
        return MaSach;
    }

    public void setMaSach(String MaSach) {
        this.MaSach = MaSach;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public float getGiaBan() {
        return GiaBan;
    }

    public void setGiaBan(float GiaBan) {
        this.GiaBan = GiaBan;
    }
    
    
}

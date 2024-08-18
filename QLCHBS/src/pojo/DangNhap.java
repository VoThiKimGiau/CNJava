/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author ASUS
 */
public class DangNhap {
    private String MaNV;
    private String TenDN;
    private String MK;
    private String TenND;
    private boolean KQDN;

    public DangNhap() {
    }

    public DangNhap(String MaNV, String TenDN, String MK, String TenND, boolean KQDN) {
        this.MaNV = MaNV;
        this.TenDN = TenDN;
        this.MK = MK;
        this.TenND = TenND;
        this.KQDN = KQDN;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getTenDN() {
        return TenDN;
    }

    public void setTenDN(String TenDN) {
        this.TenDN = TenDN;
    }

    public String getMK() {
        return MK;
    }

    public void setMK(String MK) {
        this.MK = MK;
    }

    public String getTenND() {
        return TenND;
    }

    public void setTenND(String TenND) {
        this.TenND = TenND;
    }

    public boolean isKQDN() {
        return KQDN;
    }

    public void setKQDN(boolean KQDN) {
        this.KQDN = KQDN;
    }
}
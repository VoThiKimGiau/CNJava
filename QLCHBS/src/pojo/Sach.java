package pojo;

public class Sach {
    private String maSach;
    private String maTG;
    private String maDanhMuc;
    private double giaBan;
    private int slKho;
    private String tenSach;

    public Sach(String maSach, String maTG, String maDanhMuc, double giaBan, int slKho, String tenSach) {
        this.maSach = maSach;
        this.maTG = maTG;
        this.maDanhMuc = maDanhMuc;
        this.giaBan = giaBan;
        this.slKho = slKho;
        this.tenSach = tenSach;
    }

    // Getters and setters
    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSlKho() {
        return slKho;
    }

    public void setSlKho(int slKho) {
        this.slKho = slKho;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }
}

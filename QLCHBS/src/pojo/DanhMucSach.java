/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

/**
 *
 * @author HAI
 */
public class DanhMucSach {
    private String maDanhMuc;
    private String tenDanhMuc;

    public DanhMucSach() {
        
    }

    public DanhMucSach(String maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    // Getters và setters
    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    // Phương thức toString để in ra thông tin danh mục sách
    @Override
    public String toString() {
        return tenDanhMuc;
    }
}


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
public class TacGia {
    
private String maTG;
    private String tenTG;

    public TacGia() {
        // Constructor mặc định
    }

    public TacGia(String maTG, String tenTG) {
        this.maTG = maTG;
        this.tenTG = tenTG;
    }

    // Getters và setters
    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getTenTG() {
        return tenTG;
    }

    public void setTenTG(String tenTG) {
        this.tenTG = tenTG;
    }

    // Phương thức toString để in ra thông tin của tác giả
    @Override
    public String toString() {
        return tenTG;
    }
}

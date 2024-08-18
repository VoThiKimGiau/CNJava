package dao;

import pojo.DanhMucSach;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DanhMucSachDAO {

    // Get all categories
    public static List<DanhMucSach> getAllCategories() {
        List<DanhMucSach> categories = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String query = "SELECT * FROM DanhMucSach";

        try (ResultSet rs = provider.executeQuery(query)) {
            while (rs.next()) {
                String maDanhMuc = rs.getString("MaDanhMuc");
                String tenDanhMuc = rs.getString("TenDanhMuc");
                categories.add(new DanhMucSach(maDanhMuc, tenDanhMuc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Add a new category
    public static boolean addCategory(DanhMucSach category) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "INSERT INTO DanhMucSach (MaDanhMuc, TenDanhMuc) VALUES (?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, category.getMaDanhMuc());
            pstmt.setString(2, category.getTenDanhMuc());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update an existing category
    public static boolean updateCategory(DanhMucSach category) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "UPDATE DanhMucSach SET TenDanhMuc = ? WHERE MaDanhMuc = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, category.getTenDanhMuc());
            pstmt.setString(2, category.getMaDanhMuc());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete a category
    public static boolean deleteCategory(String maDanhMuc) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "DELETE FROM DanhMucSach WHERE MaDanhMuc = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, maDanhMuc);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Find a category by name
    public static List<DanhMucSach> findCategoriesByName(String name) {
        List<DanhMucSach> categories = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "SELECT * FROM DanhMucSach WHERE TenDanhMuc LIKE ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maDanhMuc = rs.getString("MaDanhMuc");
                    String tenDanhMuc = rs.getString("TenDanhMuc");
                    categories.add(new DanhMucSach(maDanhMuc, tenDanhMuc));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    
     public static boolean isCategoryExists(String maDM) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "Select MaDanhMuc from DanhMucSach where MaDanhMuc = '" + maDM + "'";
        
        try {
            ResultSet rs = provider.executeQuery(sql);
            while(rs.next()){
                provider.Close();
                return true; // có trùng
            }
        } catch (SQLException ex) {
            Logger.getLogger(SachDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        provider.Close();
        return false;
    }
}

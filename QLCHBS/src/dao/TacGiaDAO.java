package dao;

import pojo.TacGia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TacGiaDAO {

    // Get all authors
    public static List<TacGia> getAllAuthors() {
        List<TacGia> authors = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String query = "SELECT * FROM TacGia";

        try (ResultSet rs = provider.executeQuery(query)) {
            while (rs.next()) {
                String maTG = rs.getString("MaTG");
                String tenTG = rs.getString("TenTG");
                authors.add(new TacGia(maTG, tenTG));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    // Add a new author
    public static boolean addAuthor(TacGia author) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "INSERT INTO TacGia (MaTG, TenTG) VALUES (?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, author.getMaTG());
            pstmt.setString(2, author.getTenTG());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update an existing author
    public static boolean updateAuthor(TacGia author) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "UPDATE TacGia SET TenTG = ? WHERE MaTG = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, author.getTenTG());
            pstmt.setString(2, author.getMaTG());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete an author
    public static boolean deleteAuthor(String maTG) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();
        
        String query = "DELETE FROM TacGia WHERE MaTG = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, maTG);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Find authors by name
    public static List<TacGia> findAuthorsByName(String name) {
        List<TacGia> authors = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "SELECT * FROM TacGia WHERE TenTG LIKE ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maTG = rs.getString("MaTG");
                    String tenTG = rs.getString("TenTG");
                    authors.add(new TacGia(maTG, tenTG));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
    
     public static boolean isAuthorExists(String maTG) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "Select MaTG from TacGia where MaTG = '" + maTG + "'";
        
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

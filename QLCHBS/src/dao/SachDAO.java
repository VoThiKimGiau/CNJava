package dao;

import pojo.Sach;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.*;

public class SachDAO {

    public static List<Sach> getAllBooks() {
        List<Sach> books = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String query = "SELECT * FROM Sach";

        try (ResultSet rs = provider.executeQuery(query)) {
            while (rs.next()) {
                String maSach = rs.getString("MaSach");
                String maTG = rs.getString("MaTG");
                String maDanhMuc = rs.getString("MaDanhMuc");
                double giaBan = rs.getDouble("GiaBan");
                int slKho = rs.getInt("SLKho");
                String tenSach = rs.getString("TenSach");
                books.add(new Sach(maSach, maTG, maDanhMuc, giaBan, slKho, tenSach));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static List<TacGia> getAllTacGia() {
        List<TacGia> tgs = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String query = "SELECT * FROM TacGia";

        try (ResultSet rs = provider.executeQuery(query)) {
            while (rs.next()) {
                String matg = rs.getString("MaTG");
                String tentg = rs.getString("TenTG");
                tgs.add(new TacGia(matg, tentg));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tgs;
    }

    // Add a new book
    public static boolean addBook(Sach book) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "INSERT INTO Sach (MaSach, MaTG, MaDanhMuc, GiaBan, SLKho, TenSach) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, book.getMaSach());
            pstmt.setString(2, book.getMaTG());
            pstmt.setString(3, book.getMaDanhMuc());
            pstmt.setDouble(4, book.getGiaBan());
            pstmt.setInt(5, book.getSlKho());
            pstmt.setString(6, book.getTenSach());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update an existing book
    public static boolean updateBook(Sach book) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "UPDATE Sach SET MaTG = ?, MaDanhMuc = ?, GiaBan = ?, SLKho = ?, TenSach = ? WHERE MaSach = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, book.getMaTG());
            pstmt.setString(2, book.getMaDanhMuc());
            pstmt.setDouble(3, book.getGiaBan());
            pstmt.setInt(4, book.getSlKho());
            pstmt.setString(5, book.getTenSach());
            pstmt.setString(6, book.getMaSach());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete a book
    public static boolean deleteBook(String maSach) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "DELETE FROM Sach WHERE MaSach = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, maSach);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Find books by name
    public static List<Sach> findBooksByName(String name) {
        List<Sach> books = new ArrayList<>();
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();
        Connection con = provider.getConnection();

        String query = "SELECT * FROM Sach WHERE TenSach LIKE ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maSach = rs.getString("MaSach");
                    String maTG = rs.getString("MaTG");
                    String maDanhMuc = rs.getString("MaDanhMuc");
                    double giaBan = rs.getDouble("GiaBan");
                    int slKho = rs.getInt("SLKho");
                    String tenSach = rs.getString("TenSach");
                    books.add(new Sach(maSach, maTG, maDanhMuc, giaBan, slKho, tenSach));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static boolean isBookExists(String maSach) {
        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        String sql = "Select MaSach from Sach where MaSach = '" + maSach + "'";
        
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

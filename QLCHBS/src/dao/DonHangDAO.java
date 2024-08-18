package dao;

import gui.frmDangNhap;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.DangNhap;
import pojo.DonHang;
import pojo.KhachHang;

/**
 *
 * @author TPUS
 */
public class DonHangDAO {

    // Method to fetch all DonHang records
    public static ArrayList<DonHang> layDanhSachDonHang() {
        ArrayList<DonHang> dsDH = new ArrayList<>();
        try {
            String sql = "SELECT * FROM DonHang";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getString("MaDH"));
                dh.setMaKH(rs.getString("MaKH"));
                dh.setMaNV(rs.getString("MaNV"));
                dh.setNgayDat(rs.getString("NgayDat"));
                dh.setTongTien(rs.getInt("TongTien"));
                dh.setTrangThai(rs.getBoolean("TrangThai"));
                dsDH.add(dh);
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dsDH;
    }

    // Method to fetch a single DonHang record by ID
    public static DonHang layDonHang(String maDH) {
        DonHang dh = null;
        try {
            String sql = "SELECT * FROM DonHang WHERE MaDH = ?";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, maDH);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                dh = new DonHang();
                dh.setMaDH(rs.getString("MaDH"));
                dh.setMaKH(rs.getString("MaKH"));
                dh.setMaNV(frmDangNhap.account.getMaNV());
                dh.setNgayDat(rs.getString("NgayDat"));
                dh.setTongTien(rs.getInt("TongTien"));
                dh.setTrangThai(rs.getBoolean("TrangThai"));
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dh;
    }

    // Method to add a new DonHang record using a stored procedure
    public static boolean themDonHang(String maDH, String maKH, String maNV,float tongTien) {
        boolean kq = false;
        String sql = "{CALL themDonHang(?, ?, ?, ?,?)}"; // Thêm một tham số cho trạng thái
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();

            CallableStatement callstatement = provider.getConnection().prepareCall(sql);
            callstatement.setString(1, maDH);
            callstatement.setString(2, maKH);
            callstatement.setString(3, maNV);
            callstatement.setFloat(4, tongTien);
            callstatement.setBoolean(5, false);
            int n = callstatement.executeUpdate();
            if (n == 1) {
                kq = true;
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    // Method to update an existing DonHang record
    public static boolean capNhatDonHang(String maDH, String maKH, Date ngayDat) {
        boolean kq = false;
        String sql = "UPDATE DonHang SET MaKH = ?, NgayDat = ? WHERE MaDH = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);

            statement.setString(1, maKH);
            statement.setDate(2, new java.sql.Date(ngayDat.getTime()));
            statement.setString(3, maDH);
            int n = statement.executeUpdate();
            if (n == 1) {
                kq = true;
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    // Method to delete a DonHang record
    public static boolean xoaDonHang(String maDH) {
        boolean kq = false;
        String sql = "DELETE FROM DonHang WHERE MaDH = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, maDH);
            int n = statement.executeUpdate();
            if (n == 1) {
                kq = true;
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    public static ArrayList<String> layDanhSachMaKH() {
        ArrayList<String> dsMaKH = new ArrayList<>();
        try {
            String sql = "SELECT MaKH FROM KhachHang";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                dsMaKH.add(rs.getString("MaKH"));
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dsMaKH;
    }

    public boolean isMaDHTonTai(String maDH) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM DonHang WHERE MaDH = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);

            statement.setString(1, maDH);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exists;

    }

    public String layMaNhanVienTuSDT(String sdt) {
        String sql = "SELECT MaNV FROM NhanVien WHERE SDT = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, sdt);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("MaNV");
            }
            provider.Close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String layMaKHTuSDT(String sdt) {
        String sql = "SELECT MaKH FROM KhachHang WHERE SDT = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, sdt);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("MaKH");
            }
            provider.Close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public KhachHang layThongTinKhachHangTuSDT(String sdt) {
        // Giả sử bạn có bảng KhachHang với cột SDT, TenKH, DiaChi
        String sql = "SELECT TenKH, DiaChi FROM KhachHang WHERE SDT = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, sdt);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setTenKH(rs.getString("TenKH"));
                kh.setDiaChi(rs.getString("DiaChi"));
                return kh;
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean kiemtraSoDienThoai(String sdt) {
        return sdt.matches("\\d{10,11}");
    }

    public boolean kiemtraNgay(String ngay) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        try {
            df.parse(ngay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean kiemTraSoLuong(int soLuong) {
        try {

            return soLuong > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int laySoLuongKho(String maSach) {
        int slKho = 0;
        String sql = "SELECT SLKho FROM Sach WHERE MaSach = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, maSach);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                slKho = rs.getInt("SLKho");
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return slKho;
    }

    public boolean kiemTraSoLuongNhap(int soLuongNhap, String maSach) {
        int slKho = laySoLuongKho(maSach);
        return soLuongNhap <= slKho;
    }

    public String layTenDanhMucBangMaDanhMuc(String ma) {
        String sql = "SELECT TenDanhMuc FROM DanhMucSach WHERE MaDanhMuc = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, ma);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("TenDanhMuc");
            }
            provider.Close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> layDanhSachTenDM() {
        ArrayList<String> dsDM = new ArrayList<>();
        try {
            String sql = "SELECT TenDanhMuc FROM DanhMucSach";
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                dsDM.add(rs.getString("TenDanhMuc"));
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dsDM;
    }

    public void capNhatSLKho(String maSach, int soLuongBan) {
        String sql = "UPDATE Sach SET SLKho = SLKho - ? WHERE MaSach = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setInt(1, soLuongBan);
            statement.setString(2, maSach);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật SLKho thành công cho mã sách: " + maSach);
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
  public void capNhatTrangThaiDonHang(String maDH, boolean trangthai) {
        String sql = "UPDATE DonHang SET TrangThai = ? WHERE MaDH = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setBoolean(1, trangthai);
            statement.setString(2, maDH);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật trạng thái thành công cho đơn hàng: " + maDH);
            }
            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public String taoMaDHTuDong() {
        // Logic tạo mã đơn hàng tự động. Ví dụ:
        // Lấy mã đơn hàng cuối cùng từ cơ sở dữ liệu và tăng lên 1
        String maDH = "";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();

            String sql = "SELECT TOP 1 MaDH FROM DonHang ORDER BY MaDH DESC";
            Statement statement = provider.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                String lastMaDH = rs.getString("MaDH");
                int number = Integer.parseInt(lastMaDH.substring(2)) + 1;
                maDH = String.format("DH%03d", number);
            } else {
                maDH = "DH001"; // nếu chưa có đơn hàng nào
            }

            provider.Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return maDH;
    }
    
    public String layTenSPBangMaSP(String ma) {
        String sql = "SELECT TenSach FROM Sach WHERE MaSach = ?";
        try {
            SQLServerDataProvider provider = new SQLServerDataProvider();
            provider.Open();
            PreparedStatement statement = provider.getConnection().prepareStatement(sql);
            statement.setString(1, ma);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("TenSach");
            }
            provider.Close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

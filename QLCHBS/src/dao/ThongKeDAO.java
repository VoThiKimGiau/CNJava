/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import microsoft.sql.Types;

/**
 *
 * @author Admin
 */
public class ThongKeDAO {

    public static ArrayList<String> layThang() {
        ArrayList<String> kq = new ArrayList<>();
        String sql = "SELECT DISTINCT MONTH(NgayDat) AS Thang FROM DonHang";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                String mon = new String();
                mon = rs.getString("Thang");

                kq.add(mon);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kq;
    }

    public static ArrayList<String> layNam() {
        ArrayList<String> kq = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(NgayDat) AS Nam FROM DonHang";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        try {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                String y = new String();
                y = rs.getString("Nam");

                kq.add(y);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kq;
    }

    public static Double thongKeTheoThang(int Thang) throws SQLException {
        Double tt;
        String sql = "{? = call thongKeTheoThang(?)}";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        Connection connection = provider.getConnection();
        CallableStatement statement = connection.prepareCall(sql);
        statement.registerOutParameter(1, Types.MONEY);
        statement.setInt(2, Thang);

        statement.execute();

        tt = statement.getDouble(1);

        statement.close();
        provider.Close();
        return tt;
    }

    public static Double thongKeTheoNam(int Nam) throws SQLException {
        Double tt;
        String sql = "{? = call thongKeTheoNam(?)}";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        Connection connection = provider.getConnection();
        CallableStatement statement = connection.prepareCall(sql);
        statement.registerOutParameter(1, Types.MONEY);
        statement.setInt(2, Nam);

        statement.execute();

        tt = statement.getDouble(1);

        statement.close();
        provider.Close();
        return tt;
    }

    public static ArrayList<String> thongKeKhachHangMuaNhieu() throws SQLException {
        ArrayList<String> ds = new ArrayList<>();

        String sql = "select * from thongKeKhachMuaNhieu()";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ResultSet resultSet = provider.executeQuery(sql);
        while (resultSet.next()) {
            String maKH = resultSet.getString("MaKH");
            String tenKH = resultSet.getString("TenKH");
            String SDT = resultSet.getString("SDT");
            Double tt = resultSet.getDouble("TongTien");

            String info = maKH + " - " + tenKH + " - " + SDT + " - " + String.valueOf(tt);
            ds.add(info);
        }

        resultSet.close();
        provider.Close();

        return ds;
    }

    public static ArrayList<String> thongKeSachBanChay() throws SQLException {
        ArrayList<String> ds = new ArrayList<>();

        String sql = "select * from thongKeSachBanChay()";

        SQLServerDataProvider provider = new SQLServerDataProvider();
        provider.Open();

        ResultSet resultSet = provider.executeQuery(sql);
        while (resultSet.next()) {
            String maSach = resultSet.getString("MaSach");
            String tenSach = resultSet.getString("TenSach");
            String tenTG = resultSet.getString("TenTG");
            Double gb = resultSet.getDouble("GiaBan");
            int slk = resultSet.getInt("SLKho");

            String info = maSach + " - " + tenSach + " - " + tenTG + " - " + String.valueOf(gb) + " - " + String.valueOf(slk);
            ds.add(info);
        }

        resultSet.close();
        provider.Close();

        return ds;
    }
}

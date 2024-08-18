/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import gui.frmKhachHang;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class SQLServerDataProvider {

    private Connection connection;
    private Statement statement;
    String strServer = "DESKTOP-MIJ3F8K";
    String strDatabase = "QLCHBS";
    String userName = "sa";
    String password = "123";

    public Connection getConnection() {
        return connection;
    }

    public void Open() {
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(frmKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String connectionURL = "jdbc:sqlserver://" + strServer
                + ":1433;databaseName=" + strDatabase
                + ";user = " + userName
                + ";password= " + password
                + ";integratedSecurity=true;"
                + "encrypt=true;trustServerCertificate=true";
        try {
            connection = DriverManager.getConnection(connectionURL);
            if (connection != null) {
                System.out.println("Kết nối thành công");
            } else {
                System.out.println("Kết nối thất bại");
            }

        } catch (SQLException ex) {
            Logger.getLogger(frmKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLServerDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(SQLServerDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public int executeUpdate(String sql) {
        int n = -1;
        try {
            statement = connection.createStatement();
            n = statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(SQLServerDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
}

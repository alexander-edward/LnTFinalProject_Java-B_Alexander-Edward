package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Menu;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/pt_pudding";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void insertMenu(Menu menu) throws SQLException {
        String sql = "INSERT INTO datastok (MenuID, NamaMenu, HargaMenu, StokMenu) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, menu.getKodeMenu());
        statement.setString(2, menu.getNamaMenu());
        statement.setDouble(3, menu.getHargaMenu());
        statement.setInt(4, menu.getStokMenu());
        statement.executeUpdate();
    }

    public static List<Menu> getAllMenus() throws SQLException {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM datastok";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String kodeMenu = resultSet.getString("MenuID");
            String namaMenu = resultSet.getString("NamaMenu");
            int hargaMenu = resultSet.getInt("HargaMenu");
            int stokMenu = resultSet.getInt("StokMenu");
            Menu menu = new Menu(kodeMenu, namaMenu, hargaMenu, stokMenu);
            menus.add(menu);
        }
        
        return menus;
    }

    public static void updateMenu(Menu menu) throws SQLException {
        String sql = "UPDATE datastok SET HargaMenu = ?, StokMenu = ? WHERE MenuID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, menu.getHargaMenu());
        statement.setInt(2, menu.getStokMenu());
        statement.setString(3, menu.getKodeMenu());
        statement.executeUpdate();
    }

    public static void deleteMenu(String kodeMenu) throws SQLException {
        String sql = "DELETE FROM datastok WHERE MenuID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, kodeMenu);
        statement.executeUpdate();
    }
}
package main;

import database.Database;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Menu;
import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    private TextField kodeMenuField;
    private TextField namaMenuField;
    private TextField hargaMenuField;
    private TextField stokMenuField;
    private TableView<Menu> menuTableView;
    private TextField updateKodeMenuField;
    private TextField updateHargaMenuField;
    private TextField updateStokMenuField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Koneksi ke database
        Database.connect();

        // Komponen untuk insert menu
        Label kodeMenuLabel = new Label("Kode Menu:");
        kodeMenuField = new TextField();

        Label namaMenuLabel = new Label("Nama Menu:");
        namaMenuField = new TextField();

        Label hargaMenuLabel = new Label("Harga Menu:");
        hargaMenuField = new TextField();

        Label stokMenuLabel = new Label("Stok Menu:");
        stokMenuField = new TextField();

        Button insertButton = new Button("Insert");
        insertButton.setOnAction(event -> insertMenu());
        

        GridPane insertGridPane = new GridPane();
        insertGridPane.setHgap(10);
        insertGridPane.setVgap(10);
        insertGridPane.addRow(0, kodeMenuLabel, kodeMenuField);
        insertGridPane.addRow(1, namaMenuLabel, namaMenuField);
        insertGridPane.addRow(2, hargaMenuLabel, hargaMenuField);
        insertGridPane.addRow(3, stokMenuLabel, stokMenuField);
        insertGridPane.addRow(4, insertButton);

        // Komponen untuk view menu
        menuTableView = new TableView<>();
        TableColumn<Menu, String> kodeMenuColumn = new TableColumn<>("Kode Menu");
        kodeMenuColumn.setCellValueFactory(cellData -> cellData.getValue().kodeMenuProperty());
        menuTableView.getColumns().add(kodeMenuColumn);
        
        // Kolom Nama Menu
        TableColumn<Menu, String> namaMenuColumn = new TableColumn<>("Nama Menu");
        namaMenuColumn.setCellValueFactory(cellData -> cellData.getValue().namaMenuProperty());
        menuTableView.getColumns().add(namaMenuColumn);

        // Kolom Harga
        TableColumn<Menu, Double> hargaMenuColumn = new TableColumn<>("Harga Menu");
        hargaMenuColumn.setCellValueFactory(cellData -> cellData.getValue().hargaMenuProperty().asObject());
        menuTableView.getColumns().add(hargaMenuColumn);
        
        //Kolom Stok
        TableColumn<Menu, Integer> stokMenuColumn = new TableColumn<>("Stok Menu");
        stokMenuColumn.setCellValueFactory(cellData -> cellData.getValue().stokMenuProperty().asObject());
        menuTableView.getColumns().add(stokMenuColumn);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshMenuTable());

        VBox viewMenuBox = new VBox(10, menuTableView, refreshButton);
        viewMenuBox.setPadding(new Insets(10));

        // Komponen untuk update menu
        updateKodeMenuField = new TextField();

        updateHargaMenuField = new TextField();

        updateStokMenuField = new TextField();

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> updateMenu(updateKodeMenuField.getText(), 
        Double.parseDouble(updateHargaMenuField.getText()),
        Integer.parseInt(updateStokMenuField.getText())));

        GridPane updateGridPane = new GridPane();
        updateGridPane.setHgap(10);
        updateGridPane.setVgap(10);
        updateGridPane.addRow(0, new Label("Kode Menu:"), updateKodeMenuField);
        updateGridPane.addRow(1, new Label("Harga Menu:"), updateHargaMenuField);
        updateGridPane.addRow(2, new Label("Stok Menu:"), updateStokMenuField);
        updateGridPane.addRow(3, updateButton);

        // Komponen untuk delete menu
        TextField deleteKodeMenuField = new TextField();

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteMenu(deleteKodeMenuField.getText()));

        GridPane deleteGridPane = new GridPane();
        deleteGridPane.setHgap(10);
        deleteGridPane.setVgap(10);
        deleteGridPane.addRow(0, new Label("Kode Menu:"), deleteKodeMenuField);
        deleteGridPane.addRow(1, deleteButton);

        // Layout utama
        VBox root = new VBox(10, insertGridPane, viewMenuBox, updateGridPane, deleteGridPane);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("PT Pudding Menu App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    private void insertMenu() {
        String kodeMenu = kodeMenuField.getText();
        String namaMenu = namaMenuField.getText();
        double hargaMenu;
        int stokMenu;

        // Validasi kodeMenu dan namaMenu
        if (kodeMenu.isEmpty() || namaMenu.isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Data menu tidak boleh kosong.");
        	return;
        }
        
        if (!kodeMenu.startsWith("PD-")) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Kode menu harus diawali dengan 'PD-'.");
        	return;
        }
        
        // Mengecek kode menu pada tabel
        try {
            List<Menu> menus = Database.getAllMenus();
            for (Menu menu : menus) {
                if (menu.getKodeMenu().equals(kodeMenu)) {
                    showAlert(Alert.AlertType.ERROR, "Kode Menu Sudah Ada", null, "Kode menu sudah ada di dalam tabel. Silakan masukkan kode menu yang berbeda.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Memuat Data Menu", null, "Terjadi kesalahan saat memuat data menu dari database.");
            return;
        }

        //Exception jika typo
        try {
            hargaMenu = Double.parseDouble(hargaMenuField.getText());
            stokMenu = Integer.parseInt(stokMenuField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", null, "Harap masukkan angka yang valid untuk harga dan stok menu.");
            return;
        }

        Menu menu = new Menu(kodeMenu, namaMenu, hargaMenu, stokMenu);

        try {
            Database.insertMenu(menu);
            clearFields();
            refreshMenuTable();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Insert Menu", null, "Terjadi kesalahan saat menyisipkan data menu ke database.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


	private void refreshMenuTable() {
        try {
            List<Menu> menus = Database.getAllMenus();
            menuTableView.getItems().clear();
            menuTableView.getItems().addAll(menus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMenu(String kodeMenu, double hargaMenu, int stokMenu) {
        Menu menu = new Menu(kodeMenu, null, hargaMenu, stokMenu);
        try {
            Database.updateMenu(menu);
            clearUpdateFields();
            refreshMenuTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMenu(String kodeMenu) {
        try {
            Database.deleteMenu(kodeMenu);
            refreshMenuTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        kodeMenuField.clear();
        namaMenuField.clear();
        hargaMenuField.clear();
        stokMenuField.clear();
    }

    private void clearUpdateFields() {
        updateKodeMenuField.clear();
        updateHargaMenuField.clear();
        updateStokMenuField.clear();
    }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}

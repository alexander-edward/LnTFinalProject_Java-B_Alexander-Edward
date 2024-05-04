package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Menu {
    private final SimpleStringProperty kodeMenu;
    private final SimpleStringProperty namaMenu;
    private final SimpleDoubleProperty hargaMenu;
    private final SimpleIntegerProperty stokMenu;

    public Menu(String kodeMenu, String namaMenu, double hargaMenu, int stokMenu) {
        this.kodeMenu = new SimpleStringProperty(kodeMenu);
        this.namaMenu = new SimpleStringProperty(namaMenu);
        this.hargaMenu = new SimpleDoubleProperty(hargaMenu);
        this.stokMenu = new SimpleIntegerProperty(stokMenu);
    }

    // Getter dan Setter
    public String getKodeMenu() { 
    	return kodeMenu.get(); 
    }
    
    public void setKodeMenu(String kodeMenu) { 
    	this.kodeMenu.set(kodeMenu); 
    }
    
    public SimpleStringProperty kodeMenuProperty() { 
    	return kodeMenu; 
    }

    public String getNamaMenu() { 
    	return namaMenu.get(); 
    }
    
    public void setNamaMenu(String namaMenu) { 
    	this.namaMenu.set(namaMenu); 
    }
    
    public SimpleStringProperty namaMenuProperty() { 
    	return namaMenu; 
    }

    public double getHargaMenu() { 
    	return hargaMenu.get(); 
    }
    
    public void setHargaMenu(double hargaMenu) { 
    	this.hargaMenu.set(hargaMenu); 
    }
    
    public SimpleDoubleProperty hargaMenuProperty() { 
    	return hargaMenu; 
    }

    public int getStokMenu() { 
    	return stokMenu.get(); 
    }
    
    public void setStokMenu(int stokMenu) { 
    	this.stokMenu.set(stokMenu); 
    }
    
    public SimpleIntegerProperty stokMenuProperty() { 
    	return stokMenu; 
    }
}
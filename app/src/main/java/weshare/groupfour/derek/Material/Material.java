package weshare.groupfour.derek.Material;

import java.io.Serializable;

public class Material implements Serializable {
    private int icon;
    private String name,price;

    public Material() {
    }

    public Material(int icon, String name, String price) {
        this.icon = icon;
        this.name = name;
        this.price = price;


        
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

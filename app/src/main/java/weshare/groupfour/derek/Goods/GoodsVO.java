package weshare.groupfour.derek.Goods;

import java.io.Serializable;

public class GoodsVO implements Serializable {
    private int icon;
    private String name,price;

    public GoodsVO() {
    }

    public GoodsVO(int icon, String name, String price) {
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
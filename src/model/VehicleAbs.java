package model;

/**
 * Created by Marcin on 2015-10-28.
 */
public abstract class VehicleAbs {
    int id;
    String marka, name, price, type;
    boolean avibility;

    public VehicleAbs(String marka, String name, String price, boolean avibility, String type) {
        this.marka = marka;
        this.name = name;
        this.price = price;
        this.avibility = avibility;
        this.type = type;
    }

    public VehicleAbs(int id, String marka, String name, String price, boolean avibility, String type) {
        this.id = id;
        this.marka = marka;
        this.name = name;
        this.avibility = avibility;
        this.price = price;
        this.type = type;
    }

    //GETTERs:
    public int getId() {
        return id;
    }
    public String getMarka() {
        return marka;
    }
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public boolean getAvibility() {
        return avibility;
    }
    public String getType() {
        return type;
    }

    //SETTERs:
    public void setId(int id) {
        this.id = id;
    }
    public void setMarka(String marka) {
        this.marka = marka;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAvibility(boolean avibility) {
        this.avibility = avibility;
    }
    public void setType(String type) {
        this.type = type;
    }

    //METODs:
    public void changeAvibilityToFalse() {
        this.avibility = false;
    }
    public void changeAvibilityToTrue() {
        this.avibility = true;
    }

    @Override
    public String toString() {
        return "VehicleAbs{" +
                "id=" + id +
                ", marka='" + marka + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", avibility=" + avibility +
                ", type='" + type + '\'' +
                '}';
    }
}

package model.vehicles;

import model.VehicleAbs;

/**
 * Created by Marcin on 2015-10-28.
 */
public class Quad extends VehicleAbs {
    public Quad(int id, String marka, String name, String price, boolean avibility, String type) {
        super(id, marka, name, price, avibility, type);
    }

    public Quad(String marka, String name, String price, boolean avibility, String type) {
        super(marka, name, price, avibility, type);
    }
}

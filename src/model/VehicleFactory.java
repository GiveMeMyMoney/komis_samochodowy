package model;

import model.vehicles.Car;
import model.vehicles.Motor;
import model.vehicles.Quad;

import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-10-29.
 */
public class VehicleFactory {
    private static Logger logger = Logger.getLogger(VehicleFactory.class.getName());
    public enum Vehicles {
        CAR,
        QUAD,
        MOTOR
    }

    public static VehicleAbs getVehicle(int ID, String marka, String name, String price, boolean avibility, String type)
    {
        Vehicles currentVehicle = Vehicles.valueOf(type.toUpperCase());
        logger.info("Typ: " + currentVehicle);
        if(currentVehicle != null) {
            switch (currentVehicle) {
                case CAR:
                    Car car = new Car(ID, marka, name, price, avibility, type);
                    return car;
                case QUAD:
                    Quad quad = new Quad(ID, marka, name, price, avibility, type);
                    return quad;
                case MOTOR:
                    Motor motor = new Motor(ID, marka, name, price, avibility, type);
                    return motor;
            }
        }
        return null;
    }

    public static VehicleAbs getVehicle(String marka, String name, String price, boolean avibility, String type)
    {
        Vehicles currentVehicle = Vehicles.valueOf(type.toUpperCase());
        logger.info("Typ: " + currentVehicle);
        if(currentVehicle != null) {
            switch (currentVehicle) {
                case CAR:
                    Car car = new Car(marka, name, price, avibility, type);
                    return car;
                case QUAD:
                    Quad quad = new Quad(marka, name, price, avibility, type);
                    return quad;
                case MOTOR:
                    Motor motor = new Motor(marka, name, price, avibility, type);
                    return motor;
            }
        }
        return null;
    }

}

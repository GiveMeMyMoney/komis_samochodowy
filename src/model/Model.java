package model;

import dataBase.DBrepo;
import dataBase.IDBrepo;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-10-30.
 */
public class Model implements IDBrepo {
    private static Logger logger = Logger.getLogger(Model.class.getName());
    //ArrayList<Contact> contacts = new ArrayList<>();

    private static volatile Model instance = null;
    //private static DBrepo dBrepo = null;

    public static Model getInstance() {
        if (instance == null) {
            synchronized (Model.class) {
                if (instance == null) {
                    logger.info("Tworze instancje KsiazkiTelefonicznej");
                    instance = new Model();
                    DBrepo.getInstance();
                }
            }
        }
        return instance;
    }

    /**
     * Konstruktor.
     */
    private Model() {
    }

    @Override
    public void putVehicleToDB(VehicleAbs vehicle) {
        if (vehicle != null) {
            DBrepo.getInstance().putVehicleToDB(vehicle);
        } else {
            logger.info("putVehicleToDB: " + "vehicle = null");
        }

    }

    @Override
    public ArrayList<VehicleAbs> getAllVehicleByTypeFromDB(String type) {
        ArrayList<VehicleAbs> vehicles = new ArrayList<>();
        vehicles = DBrepo.getInstance().getAllVehicleByTypeFromDB(type);
        if (vehicles != null) {
            return vehicles;
        } else {
            logger.info("getAllVehicleByTypeFromDB: " + "vehicles = null");
            return null;
        }
    }

    @Override
    public void deleteVehicleFromDB(VehicleAbs vehicle) {
        if (vehicle != null) {
            DBrepo.getInstance().deleteVehicleFromDB(vehicle);
        } else {
            logger.info("deleteVehicleFromDB: " + "vehicle = null");
        }
    }

    @Override
    public void lendVehicle(VehicleAbs vehicle) {
        if (vehicle != null) {
            DBrepo.getInstance().lendVehicle(vehicle);
        } else {
            logger.info("lendVehicle: " + "vehicle = null");
        }
    }

    @Override
    public void returnVehicle(VehicleAbs vehicle) {
        if (vehicle != null) {
            DBrepo.getInstance().returnVehicle(vehicle);
        } else {
            logger.info("returnVehicle: " + "vehicle = null");
        }
    }

}

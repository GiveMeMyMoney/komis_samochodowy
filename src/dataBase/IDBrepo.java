package dataBase;

import model.VehicleAbs;

import java.util.ArrayList;

/**
 * Created by Marcin on 2015-10-22.
 */
public interface IDBrepo {
    /**
     * sklad metod z DBrepo sluzacych do operacji na BD:
     */
    void putVehicleToDB(VehicleAbs vehicle);
    ArrayList<VehicleAbs> getAllVehicleByTypeFromDB(String type);
    void deleteVehicleFromDB(VehicleAbs vehicle);
    void lendVehicle(VehicleAbs vehicle);
    void returnVehicle(VehicleAbs vehicle);
}

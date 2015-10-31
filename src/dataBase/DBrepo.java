package dataBase;


import model.VehicleAbs;
import model.VehicleFactory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-10-21.
 */
public class DBrepo {
    private static Logger logger = Logger.getLogger(DBrepo.class.getName());
    private Connection conn = null;
    private PreparedStatement prepStmt;

    private static volatile DBrepo instance = null;

    /**
     * Singleton ktory tworzy tylko 1 instancje klasy na wszystkich watkach(synchronized).
     */
    public static DBrepo getInstance() {
        if (instance == null) {
            synchronized (DBrepo.class) {
                if (instance == null) {
                    logger.info("Tworze instancje DBrepo");
                    instance = new DBrepo();
                }
            }
        }
        return instance;
    }

    public DBrepo() {
        try
        {
            logger.info("DBrepo construct");
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:projekt2.sqlite");
            logger.info("Success, connection with DB!");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }

    /**
     * metoda sluzaca do ladowania VehicleAbs do BD.
     * @param vehicle
     */
    public void putVehicleToDB(VehicleAbs vehicle) {
        logger.info("putVehicleToDB: " + vehicle.toString());
        try {
            prepStmt = conn.prepareStatement("insert into Vehicle values (null, ?, ?, ?, ?, ?);");
            //----//
            int i=0;
            prepStmt.setString(++i, vehicle.getMarka());
            prepStmt.setString(++i, vehicle.getName());
            prepStmt.setString(++i, vehicle.getPrice());
            prepStmt.setBoolean(++i, vehicle.getAvibility());
            prepStmt.setString(++i, vehicle.getType().toLowerCase());
            //----//
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }


    /**
     * metoda pobierajaca wszystkie rekordy z BD o danm TYPE od Vehicle(Pojazdu).
     * @param type
     * @return ArrayList<VehicleAbs>
     */
    public ArrayList<VehicleAbs> getAllVehicleByTypeFromDB(String type) {
        logger.info("Type: " + type);
        ResultSet result = null;
        ArrayList<VehicleAbs> vehiclesByType = new ArrayList<>();
        try {
            //najpierw pobieram do result wszystkie rekordy o zadanym type TYPE:
            prepStmt = conn.prepareStatement("SELECT * FROM Vehicle WHERE veh_type = ? order by veh_marka");
            prepStmt.setString(1, type.toLowerCase());
            result = prepStmt.executeQuery();
            VehicleAbs vehicle;
            //pozniej za pomoca Factory tworze konkretny typ(nie musze wiedziec jaki) pojazdu i dopisuje do ArrayListy
            while (result.next()) {
                vehicle = VehicleFactory.getVehicle(result.getInt("veh_id"), result.getString("veh_marka"), result.getString("veh_name"),
                        result.getString("veh_price"), result.getBoolean("veh_avibility"), result.getString("veh_type"));
                if(vehicle != null)
                    logger.info("Vehicle: " + vehicle.toString());
                else
                    logger.info("Err Vehicle = null");
                vehiclesByType.add(vehicle);
            }
            return vehiclesByType;
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * metoda usuwajaca rekord z BD
     * @param vehicle
     */
    public void deleteVehicleFromDB(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            return;
        }
        ///nie moge wziac ID poniewaz przy tworzeniu kontaktu nie tworze ID - ktore jest AutoIncrementowane w BD.
        String marka = vehicle.getMarka(), name = vehicle.getName(), price = vehicle.getPrice();
        try {
            prepStmt = conn.prepareStatement("DELETE from Vehicle " +
                    "where veh_marka = '" + marka + "' AND veh_name = '" + name + "' AND veh_price = '" + price + "'");
                    //dodawanie "'" zeby dla SQLa wygladalo to jako String.
            prepStmt.execute();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
        }
    }


    /**
     * metoda sluzaca blokowaniu wypozyczonego Vehicle.
     * @param vehicle
     */
    public void lendVehicle(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            JOptionPane.showMessageDialog(null, "Dany pojazd jest juz wypozyczony!");
            return;
        } else {
            vehicle.changeAvibilityToFalse();
        }

        ResultSet result = null;
        try {
            Integer id = vehicle.getId();
            try {
                if(id == null) {    //bezsensu?
                    logger.info("B³ad: id==null");
                    throw new NullPointerException();
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }

            prepStmt = conn.prepareStatement("UPDATE Vehicle SET veh_avibility = '0' WHERE veh_id = '" + id + "'");   //false
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * metoda sluzaca odblokowywaniu oddanego Vehicle.
     * @param vehicle
     */
    public void returnVehicle(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        ResultSet result = null;
        try {
            Integer id = vehicle.getId();
            try {
                if(id == null) {    //bezsensu?
                    logger.info("B³ad: id==null");
                    throw new NullPointerException();
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
            vehicle.changeAvibilityToTrue();

            prepStmt = conn.prepareStatement("UPDATE Vehicle SET veh_avibility = 1 WHERE veh_id = '" + id + "'");   //true
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}



/**
 * metoda sluzaca do ladowania Quad do BD.
 *//*
    public void putQuadToDB(Quad quad) {
        logger.info("putContactToDB");
        try {
            prepStmt = conn.prepareStatement("insert into contact values (null, ?, ?, ?, ?, ?, ?);");
            /*//****//*/
            int i=0;
            prepStmt.setString(++i, contact.getName());
            prepStmt.setString(++i, contact.getSurname());
            prepStmt.setString(++i, contact.getProvince());
            prepStmt.setString(++i, contact.getAddress());
            prepStmt.setString(++i, contact.getPostalCode());
            prepStmt.setString(++i, contact.getCity());
            /*//****//*/
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        //TODO zle autoincrementuje ID gdy sie usunie inkrementuje wyzej...
    }

    *//**
 * metoda sluzaca do ladowania Motor do BD.
 *//*
    public void putMotorToDB(Motor motor) {
        logger.info("putContactToDB");
        try {
            prepStmt = conn.prepareStatement("insert into contact values (null, ?, ?, ?, ?, ?, ?);");
            /*//****//*/
            int i=0;
            prepStmt.setString(++i, contact.getName());
            prepStmt.setString(++i, contact.getSurname());
            prepStmt.setString(++i, contact.getProvince());
            prepStmt.setString(++i, contact.getAddress());
            prepStmt.setString(++i, contact.getPostalCode());
            prepStmt.setString(++i, contact.getCity());
            /*//****//*/
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }

        //TODO zle autoincrementuje ID gdy sie usunie inkrementuje wyzej...

    }*/
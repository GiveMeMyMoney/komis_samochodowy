package fxmlController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.Model;
import model.VehicleAbs;
import model.VehicleFactory;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Controller implements Initializable {
    private static Logger logger = Logger.getLogger(Controller.class.getName());
    private static final String car = "CAR";
    private static final String quad = "QUAD";
    private static final String motor = "MOTOR";
    private String flag = car;
    ArrayList<VehicleAbs> vehicles = new ArrayList<>();

    ///"slabe" ladowanie modelu na sztywno. Brak konstr.
    private Model model = Model.getInstance();
    //View mam bezposrednio z fxmla.

    ///FXML variable region
    @FXML private ChoiceBox<String> spinnerType;
    @FXML private TextField etMark, etName, etPrice;
    @FXML private ListView<VehicleAbs> listViewVehicle;
    ///endregion.

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initialize");
        /**
         * Ladowanie danych do ListView
         */
        setVehiclesArray(flag);
        setListViewVehicle(vehicles);
        /**
         * Ladowanie do ChoiceBox
         */
        List<String> list = new ArrayList<>();
        list.add(car);list.add(quad);list.add(motor);
        ObservableList obList = FXCollections.observableList(list);
        spinnerType.getItems().clear();
        spinnerType.setItems(obList);
        spinnerType.getSelectionModel().selectFirst();
    }

    ///private metod region
    private void setListViewVehicle(ArrayList<VehicleAbs> vehicles) {
        listViewVehicle.setCellFactory(new Callback<ListView<VehicleAbs>, ListCell<VehicleAbs>>() {
            @Override
            public ListCell<VehicleAbs> call(ListView<VehicleAbs> param) {
                ListCell<VehicleAbs> cell = new ListCell<VehicleAbs>() {
                    @Override
                    protected void updateItem(VehicleAbs item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(String.valueOf(vehicles.indexOf(item) + 1) + ". Mark: " + item.getMarka() + ", Name: " + item.getName()
                                    + ", Price: " + item.getPrice() + ", Avibility: " + item.getAvibility());
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void setVehiclesArray(String type) {
        vehicles = new ArrayList<>(model.getAllVehicleByTypeFromDB(type));
        ObservableList<VehicleAbs> data = FXCollections.observableArrayList(vehicles);
        listViewVehicle.setItems(data);
    }

    private void changeToAddByType(String flag) {
        if (Objects.equals(flag.toUpperCase(), car)) {
            btnShowCars();
        } else if (Objects.equals(flag.toUpperCase(), quad)) {
            btnShowQuads();
        } else if (Objects.equals(flag.toUpperCase(), motor)){
            btnShowMotors();
        }
    }

    //endregion


    ///FXML metod region
    @FXML protected void btnShowCars() {
        flag = car;
        setVehiclesArray(flag);
        setListViewVehicle(vehicles);
    }

    @FXML protected void btnShowQuads() {
        flag = quad;
        setVehiclesArray(flag);
        setListViewVehicle(vehicles);
    }

    @FXML protected void btnShowMotors() {
        flag = motor;
        setVehiclesArray(flag);
        setListViewVehicle(vehicles);
    }

    @FXML protected void btnSellVehicle() {
        logger.info("proba buttona sell");
        final int selectedIdx = listViewVehicle.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            VehicleAbs vehicleToRemove = listViewVehicle.getSelectionModel().getSelectedItem();
            final int newSelectedIdx = (selectedIdx == listViewVehicle.getItems().size()) ? selectedIdx - 1 : selectedIdx;
            if(!vehicleToRemove.getAvibility()) {
                JOptionPane.showMessageDialog(null, "Dany pojazd jest aktualnie wypozyczony i nie moze byc sprzedany!");
                return;
            }

            //usun z BD:
            model.deleteVehicleFromDB(vehicleToRemove);
            //usun z widoku:
            vehicles.remove(vehicleToRemove);
            listViewVehicle.getItems().remove(selectedIdx);
            listViewVehicle.getSelectionModel().select(newSelectedIdx);
            logger.info("Pousuwane teraz ustawiam listView");
            setListViewVehicle(vehicles);
        }
        //initialize(null, null);
    }

    @FXML protected void btnLendVehicle() {
        logger.info("proba buttona lend");
        final int selectedIdx = listViewVehicle.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            VehicleAbs vehicleToChangeAvibility = listViewVehicle.getSelectionModel().getSelectedItem();
            //BD:
            model.lendVehicle(vehicleToChangeAvibility);
            //widok:
            Integer arrIndex = vehicles.indexOf(vehicleToChangeAvibility);
            vehicles.set(arrIndex, vehicleToChangeAvibility);
            logger.info("zmieniono Avibility na false dla" + vehicleToChangeAvibility.toString());
            setListViewVehicle(vehicles);
        }
    }

    @FXML protected void btnReturnVehicle() {
        logger.info("proba buttona return");
        final int selectedIdx = listViewVehicle.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            VehicleAbs vehicleToChangeAvibility = listViewVehicle.getSelectionModel().getSelectedItem();

            //BD:
            model.returnVehicle(vehicleToChangeAvibility);
            //widok:
            Integer arrIndex = vehicles.indexOf(vehicleToChangeAvibility);
            vehicles.set(arrIndex, vehicleToChangeAvibility);
            logger.info("zmieniono Avibility na true dla" + vehicleToChangeAvibility.toString());
            setListViewVehicle(vehicles);
        }
    }

    private boolean validateInteger(String text)
    {
        if(text.matches("\\d*"))
            return false;
        else
            return true;
    }

    @FXML protected void btnOK() {
        logger.info("proba buttona add");
        ArrayList<String> atributes = new ArrayList<>();
        if(validateInteger(etPrice.getText())) {
            JOptionPane.showMessageDialog(null, "Please set an Integer into column \"Price\"");
            btnCANCEL();
            etPrice.setPromptText("Only Integer!");
            return;
        }
        atributes.add(etMark.getText());
        atributes.add(etName.getText());
        atributes.add(etPrice.getText());
        atributes.add(spinnerType.getValue().toUpperCase());
        logger.info(atributes.toString());
        VehicleAbs vehicle;
        if(atributes.size() != 0) { //nie wypelnione ZADNE pole.
            for (String string : atributes) {   //sprawdzam czy wypelnione WSZYSTKIE pola
                if (string.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all sections");
                    return;
                }
            }
            vehicle = VehicleFactory.getVehicle(atributes.get(0), atributes.get(1), atributes.get(2), true, atributes.get(3));
            logger.info("Vehicle: " + vehicle.toString());
            //BD
            model.putVehicleToDB(vehicle);
            //widok //sprawdza jaka jest flaga i wyswietla odpowiednie okno:
            flag = atributes.get(3);
            changeToAddByType(flag);
        } else {
            JOptionPane.showMessageDialog(null, "Please fill all sections");
            return;
        }

    }

    @FXML protected void btnCANCEL() {
        etMark.clear();
        etName.clear();
        etPrice.clear();
        spinnerType.getSelectionModel().selectFirst();
    }
    ///endregion







}

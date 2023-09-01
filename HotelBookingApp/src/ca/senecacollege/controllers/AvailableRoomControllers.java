package ca.senecacollege.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import ca.senecacollege.databaseDAs.JdbcBookingRoomEntryDA;
import ca.senecacollege.databaseDAs.JdbcRoomDA;
import ca.senecacollege.models.Rooms;
import ca.senecacollege.utility.Alerts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class AvailableRoomControllers {
	
	// Table List variables
	private ObservableList<Rooms> roomList;
    private Date checkInDate;
    private Date checkOutDate;
	private static int numOfRooms;

    private MainController mainController;
    
    JdbcBookingRoomEntryDA bookRoomDA = new JdbcBookingRoomEntryDA();
    JdbcRoomDA roomDA = new JdbcRoomDA();
    
    @FXML
    private Pane AvailableRoomScene;

    @FXML
    private TableView<Rooms> AvailableRoomTableView;

    @FXML
    private TableColumn<Rooms, String>  MaxPeoCol;

    @FXML
    private TableColumn<Rooms, String>  RoomPriceCol;

    @FXML
    private TableColumn<Rooms, String>  RoomTpyeCol;

    @FXML
    private TableColumn<Rooms, String> RoomUnitCol;

    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button clearBtn;

    @FXML
    private DatePicker checkInDateDP;

    @FXML
    private DatePicker checkOutDateDP;

    @FXML
    private Label noOfRoomLabel;

    @FXML
    private Button searchRoomBtn;

    @FXML
    // functions 
    // Referring MainController and set The MainController
    public void setMainController(MainController mController) {
        this.mainController = mController;
    }
    
    @FXML
    void handleClearBtn(ActionEvent event) {
        checkInDate = null;
        checkOutDate = null;
        checkInDateDP.setValue(null);
        checkOutDateDP.setValue(null);
        AvailableRoomTableView.getItems().clear();
        noOfRoomLabel.setText("");
        numOfRooms =0;
    }
    
    @FXML
    void handleCancelBtn(ActionEvent event) {
        // Clear data
    	clearAllInfo();

        // Close the current Pane
        mainController.setSectionInUsedToFalse();
        ((Pane) AvailableRoomScene.getParent()).getChildren().remove(AvailableRoomScene);
    }

    @FXML
    void handleSearchRoomBtn(ActionEvent event) {
       	
    	Window owner = searchRoomBtn.getScene().getWindow();
    	
    	try {
 
            if(checkInDate == null || checkOutDate == null) {
            	throw new NumberFormatException("No selected Check-In and Check Out Date");
            }
        	setRoomList();
        	
    	} catch (NumberFormatException e) {
    		Alerts.showAlert(Alert.AlertType.ERROR,owner,"Error!",e.getMessage());
    	    System.err.println("Invalid input. Please enter a valid number.");
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    // query for available rooms connecting to JDBC
    void setRoomList() throws SQLException{

    	java.sql.Date checkInDateSQL = new java.sql.Date(checkInDate.getTime());
    	java.sql.Date checkOutDateSQL = new java.sql.Date(checkOutDate.getTime());
    	
    	ArrayList<Integer> bookedRooms = bookRoomDA.queryBookedRoomsByDateRange(checkInDateSQL, checkOutDateSQL);
    	
    	
        roomList = roomDA.queryAvailableRooms(bookedRooms);
        numOfRooms = roomList.size();
        
        AvailableRoomTableView.setItems(roomList);
        AvailableRoomTableView.refresh();
        
        noOfRoomLabel.setText(String.valueOf(numOfRooms));
    }
    
    // Clear all information
    void clearAllInfo() {
        checkInDate = null;
        checkOutDate = null;
        checkInDateDP.setValue(null);
        checkOutDateDP.setValue(null);
        AvailableRoomTableView.getItems().clear();
    }
    @FXML
    public void initialize() {
    	
    	noOfRoomLabel.setText("");
    	
        // Table Setting
        RoomUnitCol.setCellValueFactory(cellData -> cellData.getValue().getRoomUnit());
        RoomTpyeCol.setCellValueFactory(cellData -> cellData.getValue().getTpye());
        RoomPriceCol.setCellValueFactory(cellData -> cellData.getValue().getRate());
        MaxPeoCol.setCellValueFactory(cellData -> cellData.getValue().getMaxPeople());

    	// Check-in Date setting
    	checkInDateDP.valueProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue != null) {
    	    	checkInDate = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	        numOfRooms = 0;
    	        noOfRoomLabel.setText("");
    	    }
    	});

        checkOutDateDP.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDate checkIn = checkInDateDP.getValue();
                if (checkIn != null && newValue.isBefore(checkIn.plusDays(1))) {
                    // Reset the check-out date or show an error message
                    checkOutDateDP.setValue(null);
                    Window owner = checkOutDateDP.getScene().getWindow();
                    Alerts.showAlert(Alert.AlertType.ERROR, owner, "Invalid Date Selection", "Check-out date must be after check-in date.");
                } else {
                    checkOutDate = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    numOfRooms = 0;
                    noOfRoomLabel.setText("");
                }
            }
        });
   
    }

}

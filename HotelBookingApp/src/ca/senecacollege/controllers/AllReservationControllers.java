package ca.senecacollege.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.senecacollege.databaseDAs.JdbcBillDA;
import ca.senecacollege.databaseDAs.JdbcBookingDA;
import ca.senecacollege.databaseDAs.JdbcBookingRoomEntryDA;
import ca.senecacollege.databaseDAs.JdbcCustomerDA;
import ca.senecacollege.databaseDAs.JdbcRoomDA;
import ca.senecacollege.models.BookingTable;
import ca.senecacollege.models.Bookings;
import ca.senecacollege.models.Customers;
import ca.senecacollege.utility.Alerts;
import javafx.collections.FXCollections;
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

public class AllReservationControllers {

	// Table List variables
	private ObservableList<BookingTable> myBookingList;
    private Map<Bookings, Integer> myBookings = new HashMap<>();
    private Date checkInDate;
    private Date checkOutDate;
    private static int numOfRooms;
    
    private MainController mainController;
    JdbcBookingDA bookingDA = new JdbcBookingDA();
    JdbcCustomerDA customerDA = new JdbcCustomerDA();
    JdbcBookingRoomEntryDA bookRoomDA = new JdbcBookingRoomEntryDA();
    JdbcRoomDA roomDA = new JdbcRoomDA();
	
    @FXML
    private Pane AllReservationScene;
    
    @FXML
    private TableView<BookingTable> BookingsTableView;

    @FXML
    private TableColumn<BookingTable, String> BookingIDCol;

    @FXML
    private TableColumn<BookingTable, String> CheckInCol;

    @FXML
    private TableColumn<BookingTable, String> CheckOutCol;

    @FXML
    private TableColumn<BookingTable, String> CustomerNameCol;

    @FXML
    private TableColumn<BookingTable, String> NoDaysCol;

    @FXML
    private TableColumn<BookingTable, String> NoRoomsCol;

    @FXML
    private TableColumn<BookingTable, String> RoomTpyeCol;

    @FXML
    private TableColumn<BookingTable, String> TotalCol;
    
    @FXML
    private Button cancelBtn;
    
    @FXML
    private DatePicker checkInDateDP;

    @FXML
    private DatePicker checkOutDateDP;

    @FXML
    private Label noOfRoomLabel;
    
    @FXML
    private Button clearBtn;

    @FXML
    private Button searchBtn;
    

    @FXML
    void handleClearBtn(ActionEvent event) {
        checkInDate = null;
        checkOutDate = null;
        checkInDateDP.setValue(null);
        checkOutDateDP.setValue(null);
        BookingsTableView.getItems().clear();
        numOfRooms = 0;
        noOfRoomLabel.setText("");
    }

    @FXML
    void handleSearchBtn(ActionEvent event) {
    	updateTableView();
    }
   

    // functions 
    // Referring MainController and set The MainController
    public void setMainController(MainController mController) {
        this.mainController = mController;
    }
    
    public void setTableView(ObservableList<BookingTable> bookingsList) {
        this.myBookingList = FXCollections.observableArrayList(bookingsList);
        BookingsTableView.setItems(this.myBookingList );
        BookingsTableView.refresh();
        noOfRoomLabel.setText(String.valueOf(numOfRooms));
    }
    
    @FXML
    void handleCancelBtn(ActionEvent event) {
        // Clear data
    	clearAllInfo();

        // Close the current Pane
        mainController.setSectionInUsedToFalse();
        ((Pane) AllReservationScene.getParent()).getChildren().remove(AllReservationScene);
    }
    
    @FXML
    void updateTableView() {
    	Window owner = searchBtn.getScene().getWindow();
    	
    	ObservableList<BookingTable> tmpBookList = FXCollections.observableArrayList(); 
    	try {
    		// Get all bookings
    		// myBookings is Map<Bookings, customerId>
    		
            if ((checkInDate == null && checkOutDate == null) || (checkInDate != null && checkOutDate != null)) {
                if (checkInDate != null) {
                    getBookingsWithDate(checkInDate, checkOutDate);
                } else {
                    getBookingsWithOutDate();
                }
            } else {
                throw new NumberFormatException("Please select both check-in and check-out dates");
            }
    	
            // loop through the myBookings
            for (Map.Entry<Bookings, Integer> entry : myBookings.entrySet()) {
                Bookings booking = entry.getKey();
                int customerId = entry.getValue();

                // Get customer object
                Customers tmpCustomer = getCustomerById(customerId);
                String customerName = getCustomerName(tmpCustomer);
               
                
                // Get a list of room IDs for the booking
                ArrayList<Integer> roomIds = getBookingRoomsById(booking.getBookId());
                int noOfRooms = roomIds.size();
                String RoomTypeList = setRoomTypeString(roomIds);
               
                // Get the days of Date they stay. 
                java.util.Date checkIn = booking.getDateCheckInDate();
                java.util.Date checkOut = booking.getDateCheckOutDate();
                LocalDate checkInDate = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOutDate = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long noOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

                BookingTable bookingTable = new BookingTable(
                		booking.getBookId(), customerName, RoomTypeList, noOfRooms, (int)noOfDays,
                		booking.getCheckInDateString(),booking.getCheckOutDateString(), booking.getTotal());


                tmpBookList.add(bookingTable);
            }

    		setTableView(tmpBookList);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} catch (NumberFormatException e) {
    		Alerts.showAlert(Alert.AlertType.ERROR,owner,"Error!",e.getMessage());
    	    clearAllInfo();
    	}
    }
    
    String getCustomerName(Customers c) {
    	StringBuilder customerName = new StringBuilder();
    	customerName.append("(").append(c.getTitle().getValue()).append(")").
    		append(c.getfName().getValue()).append(" ").
    		append(c.getlName().getValue());
    	return customerName.toString();
    }
    
    void getBookingsWithOutDate() throws SQLException {
    	myBookings = bookingDA.getAllBookingsAndCustomerIds();
    }
    
    void getBookingsWithDate(Date checkInDate, Date checkOutDate) throws SQLException {
    	java.sql.Date checkInDateSQL = new java.sql.Date(checkInDate.getTime());
    	java.sql.Date checkOutDateSQL = new java.sql.Date(checkOutDate.getTime());
    	myBookings = bookingDA.getAllBookingsAndCustomerIdsBetweenDates(checkInDateSQL, checkOutDateSQL);
    }
    
    int getBookingCustomerIdById(int id) throws SQLException{
    	return bookingDA.queryBookingCustomer(id);
    }
    
    ArrayList<Integer> getBookingRoomsById(int id) throws SQLException{
    	return bookRoomDA.queryRoomIdForBookingId(id);
    }
    
    Customers getCustomerById(int cId) throws SQLException{
    	return customerDA.getCustomerBySearchField("id", cId);
    }
    
    String setRoomTypeString(ArrayList<Integer> ids) {
        String types = "";
        for(Integer id:ids) {
            if (id <= 6) types += "Single | ";
            else if (id <= 12) types += "Double | ";
            else if (id <= 16) types += "Deluxe | ";
            else types += "Penthouse | ";
            numOfRooms++;
        }
        // Remove trailing comma and space
        if (types.endsWith("| ")) {
            types = types.substring(0, types.length() - 2);
        }
        return types;
    }
    
    // Clear all information
    void clearAllInfo() {
        BookingsTableView.getItems().clear();
        myBookings = new HashMap<>();
        checkInDate = null;
        checkOutDate = null;
        checkInDateDP.setValue(null);
        checkOutDateDP.setValue(null);
    }
    
    public void initialize() {

        BookingIDCol.setCellValueFactory(cellData -> cellData.getValue().getBookingId());
		CheckInCol.setCellValueFactory(cellData -> cellData.getValue().getCheckInDate());
		CheckOutCol.setCellValueFactory(cellData -> cellData.getValue().getCheckOutDate());
		CustomerNameCol.setCellValueFactory(cellData -> cellData.getValue().getCustomerName());
		NoDaysCol.setCellValueFactory(cellData -> cellData.getValue().getNumOfDays());
		NoRoomsCol.setCellValueFactory(cellData -> cellData.getValue().getNumOfRooms());
		RoomTpyeCol.setCellValueFactory(cellData -> cellData.getValue().getRoomType());
		TotalCol.setCellValueFactory(cellData -> cellData.getValue().getTotalPrice());
		
		
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

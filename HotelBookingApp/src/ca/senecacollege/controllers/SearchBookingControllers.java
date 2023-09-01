package ca.senecacollege.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import ca.senecacollege.databaseDAs.JdbcBillDA;
import ca.senecacollege.databaseDAs.JdbcBookingDA;
import ca.senecacollege.databaseDAs.JdbcBookingRoomEntryDA;
import ca.senecacollege.databaseDAs.JdbcCustomerDA;
import ca.senecacollege.databaseDAs.JdbcRoomDA;
import ca.senecacollege.models.Bills;
import ca.senecacollege.models.Bookings;
import ca.senecacollege.models.Customers;
import ca.senecacollege.models.Rooms;
import ca.senecacollege.utility.Alerts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class SearchBookingControllers {
	
	// Table List variables
	private ObservableList<Rooms> roomList;
	private static int selectedId;
	private static int numOfRooms;
	
    private Bookings myBooking;
    private Customers customer = new Customers();

    private MainController mainController;
    JdbcBookingDA bookingDA = new JdbcBookingDA();
    JdbcCustomerDA customerDA = new JdbcCustomerDA();
    JdbcBillDA billDA = new JdbcBillDA();
    JdbcBookingRoomEntryDA bookRoomDA = new JdbcBookingRoomEntryDA();
    JdbcRoomDA roomDA = new JdbcRoomDA();
    

    @FXML
    private TableView<Rooms> bookedRoomsTableView;
    
    @FXML
    private TextField ReservationIDTF;

    @FXML
    private TableColumn<Rooms, String> MaxPeoCol;

    @FXML
    private TableColumn<Rooms, String> RoomPriceCol;

    @FXML
    private TableColumn<Rooms, String> RoomTpyeCol;

    @FXML
    private TableColumn<Rooms, String> RoomUnitCol;
    
    @FXML
    private Pane SearchBookingScene;

    @FXML
    private Label bookIDLabel;

    @FXML
    private Label bookedDateLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label checkInDateLabel;

    @FXML
    private Label checkOutDateLabel;

    @FXML
    private Button clearInputBtn;

    @FXML
    private Label cusNameLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private Label noRoomsLabel;

    @FXML
    private Label pricePerNigLabel;

    @FXML
    private Button searchIDBtn;

    @FXML
    private Label totalAmountLabel;
    
    // functions 
    // Referring MainController and set The MainController
    public void setMainController(MainController mController) {
        this.mainController = mController;
    }
    
    @FXML
    void handleCancelBtn(ActionEvent event) {
        // Clear data
    	clearAllInfo();

        // Close the current Pane
        mainController.setSectionInUsedToFalse();
        ((Pane) SearchBookingScene.getParent()).getChildren().remove(SearchBookingScene);
    }

    @FXML
    void handleClearInputBtn(ActionEvent event) {

    }

    @FXML
    void handleSearchIDBtn(ActionEvent event) {
    	
    	Window owner = searchIDBtn.getScene().getWindow();
    	
    	try {
    		// Update selected BookingID
    		getSelectId();
    		
    		// Update Booking Information
    		getBookingById(selectedId);
//    		System.out.println(myBooking);
    		
    		// Get customerId and BillId for the selected Booking
    		int selectedCustomerId=getBookingCustomerIdById(selectedId);
    		
    		if(selectedCustomerId == -1) {
    			throw new NumberFormatException("Reservation Id not Found!");
    		}
    		// Get customer object
    		customer = getCustomerById(selectedCustomerId);
//    		System.out.println(customer);
    		
    		// Get Observation of the rooms for the booking
    		ArrayList<Integer> ids = getBookingRoomsById(selectedId);
    		numOfRooms = ids.size();;
    		roomList = getRoomById(ids);
    		
    		// Update TableView
    		bookedRoomsTableView.setItems(roomList);
    		bookedRoomsTableView.refresh();
    		
    		setAllLabels();
            
    	}catch (NumberFormatException e) {
    		Alerts.showAlert(Alert.AlertType.ERROR,owner,"Error!",e.getMessage());
    	    clearAllInfo();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    void getSelectId() {
    	if(ReservationIDTF.getText() != null) {
    		selectedId = Integer.parseInt(ReservationIDTF.getText());
    	}else {
    		throw new NumberFormatException("Please enter the reservation id you want to check!");
    	}
    }
    
    void setAllLabels() {
    	StringBuilder customerName = new StringBuilder();
    	customerName.append(customer.getTitle().getValue()).append(" ").
    		append(customer.getfName().getValue()).append(" ").
    		append(customer.getlName().getValue());
    	
        bookIDLabel.setText(String.valueOf(myBooking.getBookId()));
        bookedDateLabel.setText(myBooking.getBookedDateString());
        checkInDateLabel.setText(myBooking.getCheckInDateString());
        checkOutDateLabel.setText(myBooking.getCheckOutDateString());
        cusNameLabel.setText(customerName.toString());
        discountLabel.setText(String.valueOf(myBooking.getDiscount()));
        noRoomsLabel.setText(String.valueOf(numOfRooms));
        pricePerNigLabel.setText(myBooking.getPricePerNight());
        totalAmountLabel.setText(myBooking.getTotal());
    }
    
    void getBookingById(int id) throws SQLException {
    	myBooking = bookingDA.queryBookingById(id);
    }
    
    int getBookingCustomerIdById(int id) throws SQLException{
    	return bookingDA.queryBookingCustomer(id);
    }
    
    int getBookingBillIdById(int id) throws SQLException{
    	return bookingDA.queryBookingBill(id);
    }
    
    ArrayList<Integer> getBookingRoomsById(int id) throws SQLException{
    	return bookRoomDA.queryRoomIdForBookingId(id);
    }
    
    ObservableList<Rooms> getRoomById(ArrayList<Integer> ids) throws SQLException{
    	return roomDA.queryRoomsById(ids);
    }
    
    Customers getCustomerById(int cId) throws SQLException{
    	return customerDA.getCustomerBySearchField("id", cId);
    }
    
    // Clear all information
    void clearAllInfo() {
    	customer = new Customers();
    	roomList = null;
    	selectedId = 0;

        bookIDLabel.setText("");
        bookedDateLabel.setText("");
        checkInDateLabel.setText("");
        checkOutDateLabel.setText("");
        cusNameLabel.setText("");
        discountLabel.setText("");
        noRoomsLabel.setText("");
        pricePerNigLabel.setText("");
        totalAmountLabel.setText("");
        
        bookedRoomsTableView.getItems().clear();
        
        ReservationIDTF.setText("");
        
        myBooking = new Bookings();
    }

    public void initialize() {
        RoomUnitCol.setCellValueFactory(cellData -> cellData.getValue().getRoomUnit());
        RoomTpyeCol.setCellValueFactory(cellData -> cellData.getValue().getTpye());
        RoomPriceCol.setCellValueFactory(cellData -> cellData.getValue().getRate());
        MaxPeoCol.setCellValueFactory(cellData -> cellData.getValue().getMaxPeople());
    }
}

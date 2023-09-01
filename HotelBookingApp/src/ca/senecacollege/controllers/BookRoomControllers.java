package ca.senecacollege.controllers;

import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class BookRoomControllers {

	// Table List variables
	private ObservableList<Rooms> roomList;
	
	// Testing variables
	private static int selectedRoomId;
    private static Rooms selectedRoom;
    private static int maximunGuestOfSelectedRooms = 0;
    private static boolean phoneExisted = false;
    private static Customers foundCustomer = new Customers();
    
	// Booking Variable
    private Bookings myBooking;
    private static int numOfGuest = 0;
    private Date bookedDate;
    private Date checkInDate;
    private Date checkOutDate;
    private static double pricePerNight;
    private static int discount;
    private static double total;
    private Customers customer = new Customers();
    private ArrayList<Rooms> SelectedRooms = new ArrayList<>();
    private Bills bill;
    
    
    // FMXL 
    private MainController mainController;
    JdbcBookingDA bookingDA = new JdbcBookingDA();
    JdbcCustomerDA customerDA = new JdbcCustomerDA();
    JdbcBillDA billDA = new JdbcBillDA();
    JdbcBookingRoomEntryDA bookRoomDA = new JdbcBookingRoomEntryDA();
    JdbcRoomDA roomDA = new JdbcRoomDA();
    
    @FXML
    private TableView<Rooms> AvailableRoomTableView;

    @FXML
    private Pane BookARoomScene;
    
    @FXML
    private Label ChosenRoomsLabel;
    @FXML
    private TableColumn<Rooms, String> MaxPeoCol;

    @FXML
    private TableColumn<Rooms, String> RoomPriceCol;

    @FXML
    private TableColumn<Rooms, String> RoomTpyeCol;

    @FXML
    private TableColumn<Rooms, String> RoomUnitCol;

    @FXML
    private Button addRoomBtn;
    
    @FXML
    private TextField addressTF;

    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button clearRoomBtn;
    
    @FXML
    private Button clearInputBtn;
    
    @FXML
    private TextField emailTF;

    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField lastNameTF;
    
    @FXML
    private DatePicker checkInDateDP;

    @FXML
    private DatePicker checkOutDateDP;

    @FXML
    private TextField noOfGuestTF;

    @FXML
    private TextField phoneTF;

    @FXML
    private TextField rateOfferTF;
    
    @FXML
    private Button searchPhoneBtn;

    @FXML
    private Button searchRoomBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private ComboBox<String> titleComboBox;
    
    
    
    // functions 
    // Referring MainController and set The MainController
    public void setMainController(MainController mController) {
        this.mainController = mController;
    }
    
    // Add Room Btn handle function
    @FXML
    void handleAddRoomBtn(ActionEvent event) {
        Window owner = addRoomBtn.getScene().getWindow();
        
        if (selectedRoom != null) {
            StringBuilder roomDetails = new StringBuilder(ChosenRoomsLabel.getText());
            
            maximunGuestOfSelectedRooms += Integer.parseInt(selectedRoom.getMaxPeople().getValue());
            String rawValue = selectedRoom.getRate().getValue();
            String cleanValue = rawValue.replaceAll("[^\\d.]", ""); // This regex removes any character that isn't a digit or a decimal point
            pricePerNight += Double.parseDouble(cleanValue);
            
            roomDetails.append(selectedRoom.getTpye().getValue())
                .append(" Room ")
                .append(selectedRoom.getRoomUnit().getValue())
                .append("; ");

            SelectedRooms.add(selectedRoom);

            ChosenRoomsLabel.setText(roomDetails.toString());
        } else {
            Alerts.showAlert(Alert.AlertType.ERROR, owner, "Not Yet Select Rooms", "Please select the room that you want!");
        }
    }

    // Clear Btn handle function
    @FXML
    void handleClearInputBtn(ActionEvent event) {
    	clearAllInfo();
    }
    
    // Cancel Btn handle function
    @FXML
    void handleCancelBtn(ActionEvent event) {
        // Clear data
        clearAllInfo();

        // Close the current Pane
        mainController.setSectionInUsedToFalse();
        ((Pane) BookARoomScene.getParent()).getChildren().remove(BookARoomScene);
    }

    @FXML
    void handleClearRoomBtn(ActionEvent event) {
        Window owner = addRoomBtn.getScene().getWindow();
        
        if(SelectedRooms != null) {
            SelectedRooms.clear();
            maximunGuestOfSelectedRooms = 0;
            pricePerNight = 0.0;
        }else {
        	 Alerts.showAlert(Alert.AlertType.ERROR, owner, "Not Able to clear rooms", "Your selected room is empty!");
        }

        ChosenRoomsLabel.setText(""); 
        Alerts.showAlert(Alert.AlertType.CONFIRMATION, owner, "Clear", "Selected Rooms Clear!");
    }
    
    // Search Phone Btn handle function
    @FXML
    void handleSearchPhoneBtn(ActionEvent event) {
    
    	Window owner = searchPhoneBtn.getScene().getWindow();
    	
        // Check if phoneTF is not null or empty
        if (phoneTF == null || phoneTF.getText().trim().isEmpty()) {
            Alerts.showAlert(Alert.AlertType.ERROR, owner, "Invalid Input", 
            		"Please enter a phone number.");
            return;
        }
    	
    	// Check if the phone existed
    	int cPhone = Integer.parseInt(phoneTF.getText());
    	
    	phoneExisted = customerDA.phoneHasExisted(cPhone);
    	
    	if(phoneExisted) {
    	    foundCustomer = customerDA.getCustomerBySearchField("phone", cPhone);
    	    if (foundCustomer == null) {
    	        Alerts.showAlert(Alert.AlertType.ERROR, owner, "Data Error", 
    	                "Customer data not found for the provided phone number.");
    	        return;
    	    }
    	    
    	    // fill up the blank of 
    	    titleComboBox.setValue(foundCustomer.getTitle().getValue());
    	    firstNameTF.setText(foundCustomer.getfName().getValue());
    	    lastNameTF.setText(foundCustomer.getlName().getValue());
    	    addressTF.setText(foundCustomer.getAddress().getValue());
    	    emailTF.setText(foundCustomer.getEmail().getValue());
    	} else {
    	    Alerts.showAlert(Alert.AlertType.ERROR, owner, "Phone not found", 
    	            "Please enter new customer's information");
    	}

    }
    
    // Search Room Btn handle function
    @FXML
    void handleSearchRoomBtn(ActionEvent event) {
    	
    	Window owner = searchRoomBtn.getScene().getWindow();
    	
    	try {
    		//Reset the selected Room Record
            SelectedRooms.clear();
            maximunGuestOfSelectedRooms = 0;
            pricePerNight = 0.0;
            ChosenRoomsLabel.setText(""); 
            
            
            if(checkInDate == null || checkOutDate == null) {
            	throw new NumberFormatException("No selected Check-In and Check Out Date");
            }
            
    		numOfGuest = Integer.parseInt(noOfGuestTF.getText());
        	setRoomList(numOfGuest);
        	
    	} catch (NumberFormatException e) {
    		Alerts.showAlert(Alert.AlertType.ERROR,owner,"Error!",e.getMessage());
    	    System.err.println("Invalid input. Please enter a valid number.");
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    // Submit Form Btn handle function
    @FXML
    void handleSubmitBtn(ActionEvent event) {
        try {
            
        	validateGuestNumber();
        	
        	// Set up the customer from the form
            customer = setUpCustomer();
            
            // Set up the booking from the form
            createBooking();
            
            //
            boolean acceptBooking = Alerts.showReservationDetail(null, "Confirm You Reservation Detail", myBooking);
            System.out.println("Does User Accept the Booking? "+ acceptBooking);
            if(acceptBooking) {
            	
            	saveCustomerToDB();
            	saveBillToDB();
            	saveBookingRoomEntry();
            	saveMyBookingToDB();
           	
            	System.out.println("Booking info has saved to DB");
            	
            	clearAllInfo();
            	
            	Alerts.showAlert(Alert.AlertType.INFORMATION, null, "Reservation Booked", 
            			"Your reservation has saved!");
            }
            
        } catch (IllegalArgumentException e) {
            Alerts.showAlert(Alert.AlertType.ERROR, null, "Input Error", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    // Customer save to Database
    public void saveCustomerToDB() throws SQLException {
        
        boolean differentInput = false;
       
        differentInput = !customer.equals(foundCustomer);
        
        if(phoneExisted && differentInput) {
            // update the phone customer information with inputs
            customerDA.updateRecordByPhone(customer.getPhone().get(),
                    customer.getTitle().getValue(),
                    customer.getfName().getValue(),
                    customer.getlName().getValue(),
                    customer.getAddress().getValue(),
                    customer.getEmail().getValue());
        	System.out.println("Has the Customer and info changed!");
        } else if(!phoneExisted) {
            customerDA.insertRecord(customer.getTitle().getValue(),
                    customer.getfName().getValue(),
                    customer.getlName().getValue(),
                    customer.getAddress().getValue(),
                    customer.getPhone().get(),
                    customer.getEmail().getValue());
        	System.out.println("Doesn't have the Customer and create it!");
        }else {
        	System.out.println("Has the Customer and nothing changed!");
        }
    }

    
    // Bill save to Database
    public void saveBillToDB() throws SQLException {
    	
    	// update Bill
    	int billId = 0;
    	billId = billDA.insertRecord(myBooking.getBookId(),total);
    	bill = new Bills(billId, myBooking.getBookId(),total);
    	myBooking.setBill(bill);
    }
    
    // BookingRoom Entry save to Database
    public void saveBookingRoomEntry() throws SQLException {
    	
    	java.sql.Date checkInDateSQL = new java.sql.Date(checkInDate.getTime());
    	java.sql.Date checkOutDateSQL = new java.sql.Date(checkOutDate.getTime());
    	
    	int bookId = myBooking.getBookId();
    	for(Rooms room:SelectedRooms) {
    		bookRoomDA.insertRecord(bookId,room.getId().get(),checkInDateSQL,checkOutDateSQL);
    	}
    }
    
    // myBooking save to Database
    public void saveMyBookingToDB() throws SQLException {
    	java.sql.Date bookedDateSQL = new java.sql.Date(bookedDate.getTime());
    	java.sql.Date checkInDateSQL = new java.sql.Date(checkInDate.getTime());
    	java.sql.Date checkOutDateSQL = new java.sql.Date(checkOutDate.getTime());
    	
    	//(noOfGuest, bookedDate, checkInDate, checkOutDate, pricePerNight, discount, total, customerId, billId)
		bookingDA.insertRecord(numOfGuest, bookedDateSQL, checkInDateSQL,checkOutDateSQL, 
					pricePerNight, discount, total, customer.getCustomerId(),bill.getId());	
    }
    
    // Create Customer as for the Customer's input
    Customers setUpCustomer() {
        // Getting value
        JdbcCustomerDA customerDA = new JdbcCustomerDA();
        
        String cTitle = titleComboBox.getValue();
        String fName = firstNameTF.getText();
        String lName = lastNameTF.getText();
        String cEmail = emailTF.getText();
        String cAddress = addressTF.getText();
        String cPhone = phoneTF.getText();
        
        // Check if inputs are empty
        if(cTitle.isEmpty() || fName.isEmpty() || lName.isEmpty() || cEmail.isEmpty() || cAddress.isEmpty() || cPhone.isEmpty()) {
            throw new IllegalArgumentException("Error Input, Please fill in all fields.");
        }

        // Validate email format using regex
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cEmail);
        
        if(!matcher.matches()){
            throw new IllegalArgumentException("Error Input, Please enter a valid email.");
        }
        
        Customers tempCustomer = new Customers(cTitle,fName,lName,cAddress,Integer.parseInt(cPhone),cEmail);
        
        int latestId = customerDA.getLatestId() ;
        if(!phoneExisted) {
        	latestId += 1;
        }else {
        	latestId = foundCustomer.getCustomerId();
        }
        
        tempCustomer.setCustomerId(latestId);
        
        return tempCustomer;
    }
    
    // query for available rooms connecting to JDBC
    void setRoomList(int guestNum) throws SQLException{

    	java.sql.Date checkInDateSQL = new java.sql.Date(checkInDate.getTime());
    	java.sql.Date checkOutDateSQL = new java.sql.Date(checkOutDate.getTime());
    	
    	ArrayList<Integer> bookedRooms = bookRoomDA.queryBookedRoomsByDateRange(checkInDateSQL, checkOutDateSQL);

        roomList = roomDA.queryAvailableRooms(bookedRooms);
            
        AvailableRoomTableView.setItems(roomList);
        AvailableRoomTableView.refresh();

    }
    
    // ComboBox handle function
    @FXML
    void handleTitleComboBox(ActionEvent event) {
        String selectedTitle = titleComboBox.getValue();
    }
    
    // Clear all information
    void clearAllInfo() {
        // 1. Clear customer data
        customer = new Customers();
        foundCustomer =new Customers();
        phoneExisted = false;
        
        // 2. Reset text fields
        noOfGuestTF.clear();
        rateOfferTF.clear();
        firstNameTF.clear();
        lastNameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        emailTF.clear();
        
        // 3. Reset date pickers
        checkInDateDP.setValue(null);
        checkOutDateDP.setValue(null);
        
        // 4. Reset combo box
        titleComboBox.getSelectionModel().clearSelection();
        
        // 5. Reset table view
        AvailableRoomTableView.getItems().clear();
        
        // 6. Reset other related variables to default values
        SelectedRooms.clear();
        maximunGuestOfSelectedRooms = 0;
        ChosenRoomsLabel.setText("");
        selectedRoom = null;
        selectedRoomId = 0;
        numOfGuest = 0;
        bookedDate = null;
        checkInDate = null;
        checkOutDate = null;
        discount = 0;
        total = 0.0;
    }

    // Create Booking Object
    void createBooking() {
    	
    	// Set booking Id for booking database
    	int bookId = bookingDA.getLatestId() + 1;
    	
    	//Price Calculation
    	if(discount == 0) handleDiscountTF();
    	calculateTotalPrice();
    	
    	//public Bookings(int bookId, Customers customers, int noOfRooms, ArrayList<Rooms> rooms, double pricePerNight,int discount, double total)
    	myBooking = new Bookings(bookId,customer,numOfGuest,SelectedRooms,pricePerNight,discount,total);
    	
    	// update with the time
    	LocalDate currentDate = LocalDate.now();
    	bookedDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	myBooking.setDate(bookedDate, checkInDate, checkOutDate);
    }
    
    // Guest Number Validation
    void validateGuestNumber() {
    	Window owner = searchRoomBtn.getScene().getWindow();
    	
    	if(numOfGuest > maximunGuestOfSelectedRooms) {
    		throw new IllegalArgumentException("Number of guests (" + numOfGuest 
    				+ ") exceeds room limit (" 
    				+ maximunGuestOfSelectedRooms + ")");
    	}
    }
    
    // Discount TF handle function
    public void handleDiscountTF() {
        Window owner = searchRoomBtn.getScene().getWindow();
        
        try {
            if (rateOfferTF.getText().isEmpty()) {
                discount = 0;
            } else {
                discount = Integer.parseInt(rateOfferTF.getText());
                
                if (discount < 0 || discount > 25) {
                    throw new IllegalArgumentException("Discount rate should be between 0 and 25.");
                }
            }
        } catch (NumberFormatException e) {
        	rateOfferTF.setText("");
        	discount = 0;
            Alerts.showAlert(Alert.AlertType.ERROR, owner, "Error Input", 
            		"Please enter a correct rate of discount.");
            System.err.println("Invalid input. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
        	rateOfferTF.setText("");
            Alerts.showAlert(Alert.AlertType.ERROR, owner, "Invalid Discount", e.getMessage());
        }
    }
    
    // Calculate Total Price
    public double calculateTotalPrice() {
        long daysStay = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
        total = pricePerNight * daysStay * (100 - discount) / 100;
        return total;
    }

    @FXML
    public void initialize() {
    	
    	// Title ComboBox Setting
        titleComboBox.getItems().addAll("Miss", "Mr", "Mrs");
          
        // Table Setting
        RoomUnitCol.setCellValueFactory(cellData -> cellData.getValue().getRoomUnit());
        RoomTpyeCol.setCellValueFactory(cellData -> cellData.getValue().getTpye());
        RoomPriceCol.setCellValueFactory(cellData -> cellData.getValue().getRate());
        MaxPeoCol.setCellValueFactory(cellData -> cellData.getValue().getMaxPeople());

        AvailableRoomTableView.setOnMouseClicked(event -> {
            if(event.getClickCount() <= 1) {
                selectedRoom= AvailableRoomTableView.getSelectionModel().getSelectedItem();
                if (selectedRoom != null) {
                	selectedRoomId = selectedRoom.getId().getValue();
                }
            }
        });
        
        
        // Discount TF setting
        rateOfferTF.textProperty().addListener((observable, oldValue, newValue) -> {
            rateOfferTF.setDisable(false);
        });
        rateOfferTF.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                case TAB:
                    handleDiscountTF();
                    break;
                default:
                    // Do nothing for other keys
                    break;
            }
        });
        
        
        // Check-in Check-out Date setting
    	// update with the time
    	LocalDate currentDate = LocalDate.now();
    	bookedDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    	// Check-in Date setting
    	checkInDateDP.valueProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue != null) {
    	        // Check if the selected check-in date is earlier than the booked date
    	        if (newValue.isBefore(currentDate)) {
    	            checkInDateDP.setValue(null);
    	            Window owner = checkInDateDP.getScene().getWindow();
    	            Alerts.showAlert(Alert.AlertType.ERROR, owner, "Invalid Date Selection", "Check-in date cannot be earlier than the booked date.");
    	        } else {
    	            checkInDate = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	        }
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
                }
            }
        });
   
    }
}

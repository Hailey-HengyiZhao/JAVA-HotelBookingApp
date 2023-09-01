package ca.senecacollege.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {
	static boolean sectionInUsed;

    @FXML
    private Pane SectionShowPane;

    @FXML
    private Label adminNameLabel;

    @FXML
    private Button availableRoomBtn;

    @FXML
    private Button billServiceBtn;

    @FXML
    private Button bookRoomBtn;

    @FXML
    private Button currentBookingBtn;

    @FXML
    private Button exitBtn;

    @FXML
    void handleAvailableRoomBtn(ActionEvent event) {
    	if(!sectionInUsed) {
    		sectionInUsed = true;
    		loadSection("/ca/senecacollege/views/AvailableRoom.fxml");
    	}
    }

    @FXML
    void handleBillServiceBtn(ActionEvent event) {
    	if(!sectionInUsed) {
    		sectionInUsed = true;
    		loadSection("/ca/senecacollege/views/SearchBooking.fxml");
    	}
    }

    @FXML
    void handleBookRoomBtn(ActionEvent event) {
    	if(!sectionInUsed) {
    		sectionInUsed = true;
    		loadSection("/ca/senecacollege/views/BookARoom.fxml");
    	}
    }

    @FXML
    void handleCurrentBookingBtn(ActionEvent event) {
    	if(!sectionInUsed) {
    		sectionInUsed = true;
    		loadSection("/ca/senecacollege/views/AllReservation.fxml");
    	}
    }

    @FXML
    void handleExitBtn(ActionEvent event) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    private void loadSection(String fxmlPath) {
        try {
            // Create an instance of FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            
            // Load the FXML file
            Pane newLoadedPane = loader.load();
            
            // Check if the loaded controller is an instance of BookRoomControllers
            Object controller = loader.getController();
            if (controller instanceof BookRoomControllers) {
                BookRoomControllers bookRoomController = (BookRoomControllers) controller;
                bookRoomController.setMainController(this);  // Assuming "this" is an instance of MainController
            }else if(controller instanceof SearchBookingControllers) {
            	SearchBookingControllers searchBookingControllers = (SearchBookingControllers) controller;
            	searchBookingControllers.setMainController(this); 
            }else if(controller instanceof AllReservationControllers) {
            	AllReservationControllers allReservationControllers = (AllReservationControllers) controller;
            	allReservationControllers.setMainController(this); 
            }else if(controller instanceof AvailableRoomControllers){
            	AvailableRoomControllers availableRoomControllers = (AvailableRoomControllers) controller;
            	availableRoomControllers.setMainController(this); 
            }

            SectionShowPane.getChildren().clear();  // Clear existing content
            SectionShowPane.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void setSectionInUsedToFalse() {
    	sectionInUsed = false;
    }
}

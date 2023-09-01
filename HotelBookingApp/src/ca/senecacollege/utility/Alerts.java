package ca.senecacollege.utility;

import ca.senecacollege.models.Bookings;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class Alerts {

	public static Alert showAlert(Alert.AlertType alt, Window win, String title, String msg) {
	  	Alert alert = new Alert(alt);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(msg);
    	alert.initOwner(win);
    	alert.showAndWait();
		return alert;
	}
	
    public static boolean showReservationDetail(Window win, String title, Bookings bookedReservation) {
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText("Please read the observation detail: \n\n" + bookedReservation.getDetail());
    	alert.initOwner(win);
    	
        alert.getDialogPane().setPrefWidth(600);  // adjust width as needed
        alert.getDialogPane().setPrefHeight(500); // adjust height as needed
    	// This will hold the button type the user clicked
    	ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

    	// Return true if user clicked OK, false otherwise
    	return result == ButtonType.OK;
    }
}

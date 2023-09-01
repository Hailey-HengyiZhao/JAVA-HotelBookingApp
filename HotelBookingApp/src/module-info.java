module HotelBookingApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	opens ca.senecacollege.application to javafx.graphics, javafx.fxml;
	opens ca.senecacollege.controllers to javafx.fxml;
	exports ca.senecacollege.controllers to javafx.fxml;
}

package ca.senecacollege.controllers;

import ca.senecacollege.databaseDAs.JdbcUserDA;
import ca.senecacollege.utility.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController {
	
	static boolean  isLogin = false;
	static String logginedAccount; 
	static String logginedPassword;

    @FXML
    private TextField emailIdTextFiled;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTextField;

    
    @FXML
    void loginButtonEvent(ActionEvent event) {

    	Window owner = loginButton.getScene().getWindow();
    	if(emailIdTextFiled.getText().isEmpty()) {
    		Alerts.showAlert(Alert.AlertType.ERROR,owner,"Form Error","Please enter the admin account");
    	}
    	String account = emailIdTextFiled.getText();
    	String password = passwordTextField.getText();
    	
    	JdbcUserDA da = new JdbcUserDA();
    	boolean flag = da.validation(account, password);
    	
    	if(flag) {
    		isLogin = flag;
    		logginedAccount = account;
    		logginedPassword = password;
    		Alerts.showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successfull", "You has logged in as admin account: "
    					+ account + ".\n Click comfirm to the main menu.");
    		((Stage) owner).close();
    	}else {
    		Alerts.showAlert(Alert.AlertType.ERROR, owner, "Login Failure", "You are not able to login to the Reservation Application"
					+ account + ".\n Please enter correct account and password.");
    	}
    }
    
    public boolean getLoginInfo() {
    	return isLogin;
    }
    
    public String getLoginUserAccount(){
    	if(logginedAccount.isEmpty() == false)
    		return logginedAccount;
    	return "noLogin";
    }
    
    public String getLoginUserPassword(){
    	if(logginedAccount.isEmpty() == false)
    		return logginedPassword;
    	return "noLogin";
    }

}

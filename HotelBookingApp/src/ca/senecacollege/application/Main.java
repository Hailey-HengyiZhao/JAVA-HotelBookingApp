package ca.senecacollege.application;
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ca.senecacollege.controllers.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	

    private static String loggedInAdmin ="noAAdmin";
    private static String loggedInPassword ="";
    
    private Socket s;
    private DataOutputStream dout;
    private DataInputStream din;
    
	@Override
	public void start(Stage primaryStage) {
		try {
			showLoginPage();

            if(loggedInAdmin.startsWith("admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecacollege/views/MainMenu.fxml"));
                BorderPane root = (BorderPane) loader.load();

                
                Scene scene = new Scene(root, 1000, 620);
                scene.getStylesheets().add(getClass().getResource("/ca/senecacollege/views/application.css").toExternalForm());

                primaryStage.setTitle("Hotel Reservation System");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
                
                try {
                    s = new Socket("localhost", 8000);
                    din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());
                    sendToServer("Successfully Login By Account: " + loggedInAdmin 
                    		+ " With Password: " + loggedInPassword);
                    new Thread(() -> listenToServer()).start();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Login Page 
	private void showLoginPage() {
		try {
	        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/ca/senecacollege/views/Login.fxml"));
	        Pane loginRoot = loginLoader.load();
	        LoginController loginController = loginLoader.getController();
	        
	        // Set up the Login Stage
	        Stage loginStage = new Stage();
	        loginStage.setTitle("Login");
	        loginStage.setScene(new Scene(loginRoot));
	        loginStage.setResizable(false);
	        
	        // Show the Login Stage and wait
	        loginStage.setOnHidden(e->{
	        	loggedInAdmin = loginController.getLoginUserAccount();
	        	loggedInPassword = loginController.getLoginUserPassword();
	        });
	        loginStage.showAndWait();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	};
    
    private void listenToServer() {
        try {
            while (true) {
                String text = din.readUTF();
                Platform.runLater(() -> {
                    System.out.println("Server: " + text);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendToServer(String message) {
        try {
            if (dout != null) {
                dout.writeUTF(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (dout != null) dout.close();
            if (din != null) din.close();
            if (s != null) s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}

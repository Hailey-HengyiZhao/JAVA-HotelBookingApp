package ca.senecacollege.application;
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Main extends Application{

	private TextArea ta = new TextArea();
	private Hashtable outputStream = new Hashtable();
	private ServerSocket ss;
	
	@Override
	public void start(Stage ps) throws Exception {
		// TODO Auto-generated method stub
		ta.setWrapText(true);//true
		
		Scene sc = new Scene(new ScrollPane(ta),550,200);
		ps.setTitle("Server");
		ps.setScene(sc);
		ps.show();
		
		new Thread(()->listen()).start();
		
	}
	
	private void listen() {
		try {
			ss = new ServerSocket(8000);
			
			Platform.runLater(()->
							ta.appendText("Multi-Threaded server started: "+new Date()+"\n"));
			while(true) {
			
				Socket s = ss.accept();
				
				Platform.runLater(()-> ta.appendText("Connection from " + s + " at " + new Date()  + "\n") );
				
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				
				outputStream.put(s, dout);
				
				new ServerThread(this, s);
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	Enumeration getOutputStreams() {
		return outputStream.elements();
	}
	
	void sendToAll(String message) {
		for(Enumeration e = getOutputStreams(); e.hasMoreElements();) {
			DataOutputStream dout = (DataOutputStream) e.nextElement();
			try {
				dout.writeUTF(message);
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	class ServerThread extends Thread{
		private Main server;
		private Socket socket;
		
		public ServerThread(Main server, Socket socket) {
			this.socket = socket;
			this.server = server;
			start();
		}
		
		public void run() {
			try {
				DataInputStream din = new DataInputStream(socket.getInputStream());
				while(true) {
					String s = din.readUTF();
					server.sendToAll(s);
					
					ta.appendText(s + "\n");
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
public static void main(String[] args) {
	launch(args);
}

}


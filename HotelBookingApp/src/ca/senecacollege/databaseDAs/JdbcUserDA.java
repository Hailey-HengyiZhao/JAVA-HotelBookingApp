package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcUserDA {


	private static final String DB_NAME = "users.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "users";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+DB_TABLE+
			" (id integer not null primary key autoincrement, "
			+ " account varchar(20) not null, password varchar(20))";
	private static final String SELECT_QRY = "Select * from " + DB_TABLE +" where account = ? AND password = ?";

	// Get authenticated admin login
	// Passing argument: account, password
	// Return boolean isAuthenicated
	public boolean validation(String account, String pass) {
		try(Connection conn = DriverManager.getConnection(DB_JDBC+DB_CONNECTION+DB_NAME)){
			
			PreparedStatement ps = null;
			ps = conn.prepareStatement(CREATE_TBL_QRY);
			ps.execute();
			
			ps = conn.prepareStatement(SELECT_QRY);
			ps.setString(1, account);
			ps.setString(2, pass);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("The Account is found");
				return true;
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}

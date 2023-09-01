package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.senecacollege.models.Customers;


public class JdbcCustomerDA {
	private static final String DB_NAME = "customers.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "customers";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+ DB_TABLE
			+" (id integer not null primary key autoincrement, "
			+" title varchar(20) not null,"
			+" fName varchar(20) not null,"
			+" lName varchar(20) not null,"
			+" address varchar(20) not null,"
			+" phone int not null,"
			+" email varchar(20) not null) ";
	private static String SearchField;
	private static final String INSERT_QRY = "insert into "
			+ DB_TABLE
			+" (title, fName, lName, address, phone, email) values (?,?,?,?,?,?)";
	private static final String SELECT_QRY = "Select * from " 
			+ DB_TABLE 
			+" where " 
			+ SearchField
			+ " = ? ";
	
	// Create new customer
	// Passing arguments: title, firstname, lastname, address, phone, email
	// Return boolean
	public boolean insertRecord(String title, String fname, String lname, String address, int phone, String email) {
	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
	        PreparedStatement ps = conn.prepareStatement(CREATE_TBL_QRY);
	        ps.execute(); // Create table if not exists
	        
	        ps = conn.prepareStatement(INSERT_QRY);
	        ps.setString(1, title);
	        ps.setString(2, fname);
	        ps.setString(3, lname);
	        ps.setString(4, address);
	        ps.setInt(5, phone);
	        ps.setString(6, email);
	        int value = ps.executeUpdate();
	        if (value > 0) {
	            System.out.println("New customer record is inserted!");
	            return true;
	        } else {
	            System.out.println("Not able to create a new customer record.");
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}

	// Get a Customer by given either search by phone or email
	// Passing arguments: searching field, searching data
	// Return Customer Object
	public Customers queryRecord(String searchField , String searchData) {
	    Customers customer = null;
	    SearchField = searchField; 

	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
	        PreparedStatement ps = conn.prepareStatement(SELECT_QRY);
	        ps.setString(1, searchData);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	int id = rs.getInt("id");
	            String title = rs.getString("title");
	            String fName = rs.getString("fName");
	            String lName = rs.getString("lName");
	            String address = rs.getString("address");
	            int phone = rs.getInt("phone");
	            String email = rs.getString("email");
	            customer = new Customers(id, title, fName, lName, address, phone, email);
	        }
	        System.out.println("Query successful. Found " + customer + " matching records.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }

	    return customer;
	}

	// Update a Customer based on a given phone number
	// Passing arguments: phone (to search), title, firstname, lastname, address, email
	// Return boolean
	public boolean updateRecordByPhone(int searchPhone, String title, String fname, String lname, String address, String email) {
	    String updateQuery = "UPDATE " + DB_TABLE + 
	                         " SET title = ?, fName = ?, lName = ?, address = ?, email = ? " + 
	                         " WHERE phone = ?";

	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
	         PreparedStatement ps = conn.prepareStatement(updateQuery)) {

	        // Setting the parameters for the update statement
	        ps.setString(1, title);
	        ps.setString(2, fname);
	        ps.setString(3, lname);
	        ps.setString(4, address);
	        ps.setString(5, email);
	        ps.setInt(6, searchPhone);  // search by this phone number

	        int value = ps.executeUpdate();
	        if (value > 0) {
	            System.out.println("Customer record updated successfully!");
	            return true;
	        } else {
	            System.out.println("Not able to update the customer record.");
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Update failed.");
	    }
	    return false;
	}

	
	
	
	// Get Customer's id by given either search by phone or email
	// Passing arguments: searching field, searching data
	// Return Customer id ( Save to hotel reservation database)
	public Integer getCustomerId(String searchField, String searchData) {
		
	    Integer customerId = null;
	    SearchField = searchField; 
	    
	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
	        PreparedStatement ps = conn.prepareStatement(SELECT_QRY);
	        ps.setString(1, searchData);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            customerId = rs.getInt("id");
	        }
	        System.out.println(customerId != null ? "Customer ID found: " + customerId : "No matching record found.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }

	    return customerId;
	}
	
	// Get Customer by given customer's id or phone
	// Passing arguments: searchFeild(id/phone), data
	// Return Customer Object
	public Customers getCustomerBySearchField(String searchField, int data) {

	    Customers customer = null;

	    // Construct the SQL query based on the searchField
	    String query = "SELECT * FROM " + DB_TABLE +" WHERE " + searchField + " = ?";  // Replace YOUR_TABLE_NAME with the name of your table

	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setInt(1, data);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            int id = rs.getInt("id");
	            String title = rs.getString("title");
	            String fName = rs.getString("fName");
	            String lName = rs.getString("lName");
	            String address = rs.getString("address");
	            int phone = rs.getInt("phone");
	            String email = rs.getString("email");
	            customer = new Customers(id, title, fName, lName, address, phone, email);
	        }

//	        System.out.println(customer != null ? "Customer found: " + customer : "No matching record found.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }

	    return customer;
	}

	// Check if phone  existed
	// Passing arguments: phone
	// Return Boolean 
	public boolean phoneHasExisted(int phone) {
	    String checkQuery = "SELECT * FROM " + DB_TABLE + " WHERE phone = ? ";
	    
	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
	         PreparedStatement ps = conn.prepareStatement(checkQuery)) {
	        ps.setInt(1, phone);
	        ResultSet rs = ps.executeQuery();

	        return rs.next();  // true if a record exists, false otherwise
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }
	    return false;
	}
	
	// Check the latest customer id
	// Passing arguments: no
	// Return int of the largest id
	public int getLatestId() {
	    String query = "SELECT MAX(id) as maxId FROM " + DB_TABLE;

	    try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
	         PreparedStatement ps = conn.prepareStatement(query)) {
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("maxId");  // Return the maximum ID value
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }
	    return 0;  // Return -1 or throw an exception if the query fails or there's no data in the table
	}
}

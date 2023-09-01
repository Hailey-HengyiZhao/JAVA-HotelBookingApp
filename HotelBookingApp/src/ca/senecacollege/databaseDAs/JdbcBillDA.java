package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ca.senecacollege.models.Bills;

public class JdbcBillDA {
	private static final String DB_NAME = "bills.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "bills";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+ DB_TABLE
			+" (id integer not null primary key autoincrement, "
            +" bookingId int not null,"
            +" amountToPay double not null) ";
	private static String SearchField;
	private static final String INSERT_QRY = "insert into "
			+ DB_TABLE
			+" (bookignId,amountToPay) values (?,?)";
    private static final String SELECT_QRY = "Select * from "
            + DB_TABLE
            + " where "
            + SearchField
            + " = ? ";
    
	// InsertRecord to database
	// Passing argument: bookingID, amountToPay
	// Return the new created data id if the record is inserted
    public int insertRecord(int bookingId, double amountToPay) throws SQLException {
        int generatedId = -1; // assuming -1 as not valid
        String insertSQL = "INSERT INTO " + DB_TABLE + " (bookingId, amountToPay) VALUES (?, ?)";
        
        try (
            Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
            PreparedStatement ps = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, bookingId);
            ps.setDouble(2, amountToPay);
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating bill failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating bill failed, no ID obtained.");
                }
            }
        }
        return generatedId;
    }

    // Query booked rooms for an booking Id
    // Passing argument: bookingId
    // Return double totalToPay if the record is existed
    public Bills queryBillById(int bookingId) throws SQLException {
        Bills bill = null;  // initialized to null
        String selectQuery = "Select * from " + DB_TABLE + " where bookingId = ?";

        try (
            Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
            PreparedStatement ps = conn.prepareStatement(selectQuery)
        ) {       
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int bookId = rs.getInt("bookingId");
                    double amountToPay = rs.getDouble("amountToPay");
                    bill = new Bills(id, bookId, amountToPay);
                }
            }
        }
        return bill;
    }
    
    // Query booked rooms for an booking Id
    // Passing argument: bookingId
    // Return double totalToPay if the record is existed
    public double queryRoomIdForBookingId(int bookingId) throws SQLException {
    	double amountToPay = 0.0;
    	SearchField = "bookingId";
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {       
            PreparedStatement ps = conn.prepareStatement(SELECT_QRY);           
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                amountToPay = rs.getDouble("amountToPay");
            }
        }
    	return amountToPay;
    }
    
    // Query latest bill id
    // No passing
    // Return int 
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

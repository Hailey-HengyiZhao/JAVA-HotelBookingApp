package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ca.senecacollege.models.Bookings;

public class JdbcBookingDA {
	private static final String DB_NAME = "bookings.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "bookings";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+ DB_TABLE
			+" (id integer not null primary key autoincrement, "
            +" noOfGuest int not null,"
            +" bookedDate Date not null,"
            +" checkInDate Date not null,"
            +" checkOutDate Date not null,"
            +" pricePerNight double not null,"
            +" discount int not null, "
            +" total double not null,"
            +" customerId int not null,"
            +" billId int not null"
            + ") ";
    private static final String INSERT_QRY = "INSERT INTO " + DB_TABLE
            + " (noOfGuest, bookedDate, checkInDate, checkOutDate, pricePerNight, discount, total, customerId, billId) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_QRY = "SELECT * FROM " + DB_TABLE + " WHERE id = ?";
    private static final String SELECT_ALL_QRY = "SELECT * FROM " + DB_TABLE;
    private static final String SELECT_BETWEEN_DATE_QRY = 
    	    "SELECT * FROM " + DB_TABLE + " WHERE checkInDate >= ? AND checkOutDate <= ?";
    
    public void insertRecord(int noOfGuest, Date bookedDate, Date checkInDate, Date checkOutDate, double pricePerNight, 
            int discount, double total, int customerId, int billId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_QRY)) {

            pstmt.setInt(1, noOfGuest);
            pstmt.setDate(2, bookedDate);
            pstmt.setDate(3, checkInDate);
            pstmt.setDate(4, checkOutDate);
            pstmt.setDouble(5, pricePerNight);
            pstmt.setInt(6, discount);
            pstmt.setDouble(7, total);
            pstmt.setInt(8, customerId);
            pstmt.setInt(9, billId);

            pstmt.executeUpdate();
        }
    }
    // Query a booking by ID
    public Bookings queryBookingById(int id) throws SQLException {
    	
        Bookings booking = null;

        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_QRY)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("id");
                int noOfGuests = rs.getInt("noOfGuest");
                java.util.Date bDate = new java.util.Date(rs.getDate("bookedDate").getTime());
                java.util.Date ciDate = new java.util.Date(rs.getDate("checkInDate").getTime());
                java.util.Date coDate = new java.util.Date(rs.getDate("checkOutDate").getTime());
                double ppNight = rs.getDouble("pricePerNight");
                int disc = rs.getInt("discount");
                double tot = rs.getDouble("total");

                booking = new Bookings(bookId, noOfGuests, bDate, ciDate, coDate, ppNight, disc, tot);
            }

        }catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }
        return booking;
    }
    
    // Query the customer of a Booking by ID
    public int queryBookingCustomer(int id) throws SQLException {

        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_QRY)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Assuming you have a Booking class to map the record
            	return rs.getInt("customerId");
            }
        } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }
        return -1;
    }
    
    // Query the bill of a Booking by ID
    public int queryBookingBill(int id) throws SQLException {

        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_QRY)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Assuming you have a Booking class to map the record
            	return rs.getInt("billId");
            }
        } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Query failed.");
	    }
        return -1;
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
	
    // Query all Data 
    // No argument
    // Return A MAP<Bookings,Integer> with Booking Object and customer Id
    public Map<Bookings, Integer> getAllBookingsAndCustomerIds() {
        Map<Bookings, Integer> resultMap = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_QRY)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id");
                int noOfGuests = rs.getInt("noOfGuest");
                java.util.Date bDate = new java.util.Date(rs.getDate("bookedDate").getTime());
                java.util.Date ciDate = new java.util.Date(rs.getDate("checkInDate").getTime());
                java.util.Date coDate = new java.util.Date(rs.getDate("checkOutDate").getTime());
                double ppNight = rs.getDouble("pricePerNight");
                int disc = rs.getInt("discount");
                double tot = rs.getDouble("total");
                int customerId = rs.getInt("customerId");

                Bookings booking = new Bookings(bookId, noOfGuests, bDate, ciDate, coDate, ppNight, disc, tot);
                resultMap.put(booking, customerId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query failed.");
        }
        
        return resultMap;
    }
    
    // Query all Data between select date range
    // Passing arguments: start Date, end Date
    // Return A MAP<Bookings,Integer> with Booking Object and customer Id
    public Map<Bookings, Integer> getAllBookingsAndCustomerIdsBetweenDates(java.util.Date startDate, java.util.Date endDate) {
        Map<Bookings, Integer> resultMap = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BETWEEN_DATE_QRY)) {

            // Convert util.Date to sql.Date for the PreparedStatement
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
             
            pstmt.setDate(1, sqlStartDate);
            pstmt.setDate(2, sqlEndDate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id");
                int noOfGuests = rs.getInt("noOfGuest");
                java.util.Date bDate = new java.util.Date(rs.getDate("bookedDate").getTime());
                java.util.Date ciDate = new java.util.Date(rs.getDate("checkInDate").getTime());
                java.util.Date coDate = new java.util.Date(rs.getDate("checkOutDate").getTime());
                double ppNight = rs.getDouble("pricePerNight");
                int disc = rs.getInt("discount");
                double tot = rs.getDouble("total");
                int customerId = rs.getInt("customerId");

                Bookings booking = new Bookings(bookId, noOfGuests, bDate, ciDate, coDate, ppNight, disc, tot);
                resultMap.put(booking, customerId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query failed.");
        }
        
        return resultMap;
    }


}

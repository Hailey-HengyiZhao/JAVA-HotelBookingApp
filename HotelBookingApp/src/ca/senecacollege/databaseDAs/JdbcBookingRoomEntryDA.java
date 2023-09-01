package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JdbcBookingRoomEntryDA {
	private static final String DB_NAME = "BookingRoomEntry.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "BookingRoomEntry";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+ DB_TABLE
			+" (id integer not null primary key autoincrement, "
            +" bookingId int not null,"
            +" roomId int not null,"
            +" checkInDate Date not null,"
            +" checkOutDate Date not null"
            + ") ";
	private static final String INSERT_QRY = "insert into "
			+ DB_TABLE
			+" (bookingId,roomId, checkInDate, checkOutDate) values (?,?,?,?)";
	private static final String SELECT_QRY = "Select * from " + DB_TABLE + " where bookingId = ?";

    
    
	// InsertRecord to database
	// Passing argument: bookingID, roomID
	// Return Boolean if the record is inserted
    public boolean insertRecord(int bookingId, int roomId, Date checkInDate, Date checkOutDate) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
            PreparedStatement ps = conn.prepareStatement(CREATE_TBL_QRY);
            ps.execute();
            ps = conn.prepareStatement(INSERT_QRY);
            ps.setInt(1, bookingId);
            ps.setInt(2, roomId);
            ps.setDate(3, checkInDate);
            ps.setDate(4, checkOutDate);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Query booked rooms for an booking Id
    // Passing argument: bookingId
    // Return ArrayList<Rooms> if the record is existed
    public ArrayList<Integer> queryRoomIdForBookingId(int bookingId) throws SQLException {
    	ArrayList<Integer> roomIds = new ArrayList<Integer> ();
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {      
            PreparedStatement ps = conn.prepareStatement(SELECT_QRY);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("roomId");
                roomIds.add(roomId);
            }
        }
    	return roomIds;
    }
    
    // Query booked rooms for an time range
    // Passing argument: checkInDate, checkOutDate
    // Return ArrayList<Integer> for the bookedRoom
    public ArrayList<Integer> queryBookedRoomsByDateRange(Date checkIn, Date checkOut) throws SQLException {
        ArrayList<Integer> roomIds = new ArrayList<Integer>();
        String query = "SELECT DISTINCT roomId FROM " + DB_TABLE + " WHERE (checkInDate <= ? AND checkOutDate >= ?) OR (checkInDate BETWEEN ? AND ?) OR (checkOutDate BETWEEN ? AND ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {      
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, checkOut);
            ps.setDate(2, checkIn);
            ps.setDate(3, checkIn);
            ps.setDate(4, checkOut);
            ps.setDate(5, checkIn);
            ps.setDate(6, checkOut);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("roomId");
                roomIds.add(roomId);
            }
        }
        return roomIds;
    }
}

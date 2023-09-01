package ca.senecacollege.databaseDAs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.senecacollege.models.Rooms;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JdbcRoomDA {
	
	private static final String DB_NAME = "rooms.db";
	private static final String DB_CONNECTION = "C:\\Users\\cchon\\eclipse-workspace\\HotelBookingApp\\src\\ca\\senecacollege\\database\\";
	private static final String DB_JDBC = "jdbc:sqlite:";
	private static final String DB_TABLE = "rooms";
	private static final String CREATE_TBL_QRY = "Create table if not exists "+ DB_TABLE
			+" (id integer not null primary key autoincrement, "
            +" roomNumber varchar(20) not null,"
            +" roomType varchar(20) not null,"  
            +" rate double not null,"
            +" maxPeople int not null,"
            +" booked int not null) ";
	private static String SearchField;
	private static final String INSERT_QRY = "insert into "
			+ DB_TABLE
			+" (roomNumber,type,rate,maxPeop,booked) values (?,?,?,?,?)";
    private static final String SELECT_QRY = "Select * from "
            + DB_TABLE
            + " where "
            + SearchField
            + " = ? ";
	
	// InsertRecord to database
	// Passing argument: type, rate, maxPeople
	// Return Boolean if the record is inserted
    public boolean insertRecord(String roomNumber,String type, double rate, int maxPeople) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
            PreparedStatement ps = conn.prepareStatement(CREATE_TBL_QRY);
            ps.execute();
            ps = conn.prepareStatement(INSERT_QRY);

            ps.setString(1, roomNumber);
            ps.setString(2, type);
            ps.setDouble(3, rate);
            ps.setInt(4, maxPeople);
            ps.setInt(5, 0);

            return ps.executeUpdate() > 0;
        }
    }
	
    
	// Query Room by id
	// Passing argument: id
	// Return Room object if it exists
    public Rooms queryRoomById(int id) throws SQLException {
    	
        Rooms room = null;
        SearchField = "id";
        
        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
            
            PreparedStatement ps = conn.prepareStatement(SELECT_QRY);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	int id1 = rs.getInt("id");
            	String roomNumber = rs.getString("roomNumber");
                String type = rs.getString("roomType");
                double rate = rs.getDouble("rate");
                int maxPeople = rs.getInt("maxPeople");

                room = new Rooms(id1, roomNumber, type, rate, maxPeople);
            }
        }

        return room;
    }
    
    // Query Room details when not booked (using booked room ids)
	// Passing argument: arrayList of BookedRooms
	// Return ObservableList<Rooms> object if it exists
    public ObservableList<Rooms> queryAvailableRooms(ArrayList<Integer> bookedRooms) throws SQLException {
        ObservableList<Rooms> rooms = FXCollections.observableArrayList();
        StringBuilder roomIds = new StringBuilder();

        for (int id : bookedRooms) {
            roomIds.append(id).append(",");
        }

        if (roomIds.length() > 0) {
            roomIds.deleteCharAt(roomIds.length() - 1);  // Removing the last comma
        }

        String query = "SELECT * FROM " + DB_TABLE + " WHERE id NOT IN (" + roomIds + ")";

        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String roomNumber = rs.getString("roomNumber");
                String type = rs.getString("roomType");
                double rate = rs.getDouble("rate");
                int maxPeople = rs.getInt("maxPeople");
                
                Rooms room = new Rooms(id, roomNumber, type, rate, maxPeople);
                rooms.add(room);
            }
        }

        return rooms;
    }

    // Query Room details with a list of roomId
	// Passing argument: arrayList of BookedRooms
	// Return ObservableList<Rooms> object if it exists
    public ObservableList<Rooms> queryRoomsById(ArrayList<Integer> bookedRooms) throws SQLException {
        ObservableList<Rooms> rooms = FXCollections.observableArrayList();
        StringBuilder roomIds = new StringBuilder();

        for (int id : bookedRooms) {
            roomIds.append(id).append(",");
        }

        if (roomIds.length() > 0) {
            roomIds.deleteCharAt(roomIds.length() - 1);  // Removing the last comma
        }

        String query = "SELECT * FROM " + DB_TABLE + " WHERE id IN (" + roomIds + ")";

        try (Connection conn = DriverManager.getConnection(DB_JDBC + DB_CONNECTION + DB_NAME)) {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String roomNumber = rs.getString("roomNumber");
                String type = rs.getString("roomType");
                double rate = rs.getDouble("rate");
                int maxPeople = rs.getInt("maxPeople");
                
                Rooms room = new Rooms(id, roomNumber, type, rate, maxPeople);
                rooms.add(room);
            }
        }

        return rooms;
    }

}
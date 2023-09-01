package ca.senecacollege.models;

import java.text.SimpleDateFormat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookingTable {
	private final IntegerProperty bookingId;
	private final StringProperty customerName;
	private final StringProperty roomType;
	private final IntegerProperty numOfRooms;
	private final IntegerProperty numOfDays;
	private final StringProperty checkInDate;
	private final StringProperty checkOutDate;
	private final StringProperty totalPrice;
	public BookingTable() {
		this(0,null,null,0,0,null,null, null);
	}
	
	public BookingTable(int id, String cusName, String roomType,int noRooms, int noDays,  
			String checkInDate, String checkOutDate, String total) {
		this.bookingId = new SimpleIntegerProperty(id);
		this.customerName = new SimpleStringProperty(cusName);
		this.roomType = new SimpleStringProperty(roomType);
		this.numOfRooms = new SimpleIntegerProperty(noRooms);
		this.numOfDays = new SimpleIntegerProperty(noDays);
		this.checkInDate = new SimpleStringProperty(checkInDate);
		this.checkOutDate = new SimpleStringProperty(checkOutDate);
		this.totalPrice = new SimpleStringProperty(total);
	}

	public StringProperty getBookingId() {
		StringProperty bookingId = new SimpleStringProperty(String.valueOf(this.bookingId.get()));
		return bookingId;
	}

	@Override
	public String toString() {
		return "BookingTable [bookingId=" + bookingId + ", customerName=" + customerName + ", roomType=" + roomType
				+ ", numOfRooms=" + numOfRooms + ", numOfDays=" + numOfDays + ", checkInDate=" + checkInDate
				+ ", checkOutDate=" + checkOutDate + ", totalPrice=" + totalPrice + "]";
	}

	public StringProperty getTotalPrice() {
		return totalPrice;
	}

	public StringProperty getCustomerName() {
		return customerName;
	}

	public StringProperty getRoomType() {
		return roomType;
	}

	public StringProperty getNumOfRooms() {
		StringProperty numOfRooms = new SimpleStringProperty(String.valueOf(this.numOfRooms.get()));
		return numOfRooms;
	}

	public StringProperty getNumOfDays() {
		StringProperty numOfDays = new SimpleStringProperty(String.valueOf(this.numOfDays.get()));
		return numOfDays;
	}

	public StringProperty getCheckInDate() {
		return checkInDate;
	}

	public StringProperty getCheckOutDate() {
		return checkOutDate;
	}
}

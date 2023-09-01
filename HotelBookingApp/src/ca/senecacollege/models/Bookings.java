package ca.senecacollege.models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Bookings {
	
	private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();
	
	private int bookId;
	private Customers customers;
    private int noOfGuests;    
    private ArrayList<Rooms> rooms;
    private double pricePerNight;
    private int discount;
    private double total;
    
    private Date bookedDate;
    private Date checkInDate;
    private Date checkOutDate;
    
    private Bills bill;

    // Constructor
    public Bookings() {
    	this(0,null,0,null,0.0,0,0.0);
    }
    
    public Bookings(int bookId, Customers customers, int noOfGuests, ArrayList<Rooms> rooms, double pricePerNight,int discount, double total) {
        this.bookId = bookId;
        this.customers = customers;
        this.noOfGuests = noOfGuests;
        this.rooms = rooms;
        this.pricePerNight = pricePerNight;
        this.discount = discount;
        this.total = total;
    }
    
    public Bookings(int bookId, int noOfGuests, Date bookedDate, Date checkInDate, Date checkOutDate, double pricePerNight,int discount, double total) {
        this.bookId = bookId;
        this.noOfGuests = noOfGuests;
    	this.bookedDate = bookedDate;
    	this.checkInDate = checkInDate;
    	this.checkOutDate = checkOutDate;
        this.pricePerNight = pricePerNight;
        this.discount = discount;
        this.total = total;
    }

	public void setDate(Date bookedDate, Date checkInDate, Date checkOutDate) {
    	this.bookedDate = bookedDate;
    	this.checkInDate = checkInDate;
    	this.checkOutDate = checkOutDate;
    }
    
    
    // Database Foreign key referring
    public ArrayList<Integer> getRoomsId() {
    	ArrayList<Integer> roomIds =  new ArrayList<>();
    	for(Rooms room: rooms) {
    		int roomId = room.getId().getValue();
    		roomIds.add(roomId);
    	}
    	return roomIds;
    }
    
    @Override
	public String toString() {
		return "Bookings [customers=" + customers 
				+ ", \n rooms=" + rooms 
				+ ", \n bookId=" + bookId + ", noOfGuests="
				+ noOfGuests 
				+ ", \nbookedDate=" + bookedDate + ", checkInDate=" + checkInDate + ", checkOutDate="
				+ checkOutDate 
				+ ", \npricePerNight=" + pricePerNight + ", discount=" + discount + ", total=" + total
				+ ", \nbill=" + bill + "]";
	}


    public String getDetail() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Booking Information:\n");
        sb.append("Booking ID: ").append(bookId).append("\n");
        sb.append("Number of Guests: ").append(noOfGuests).append("\n");
        sb.append("Booked Date: ").append(this.getBookedDateString()).append("\n");
        sb.append("Check-in Date: ").append(this.getCheckInDateString()).append("\n");
        sb.append("Check-out Date: ").append(this.getCheckOutDateString()).append("\n");
        sb.append("Price Per Night: ").append(CURRENCY.format(pricePerNight)).append("\n");
        sb.append("Discount: ").append(discount).append("%\n");
        sb.append("Total Price: ").append(CURRENCY.format(total)).append("\n");
        sb.append("\nCustomer Details:\n").append(customers.getDetail()).append("\n"); // Assuming Customers has a detailed toString method
        sb.append("\nRooms Booked:\n");
        for (Rooms room : rooms) {
            sb.append(room.getDetail()).append("\n"); // Assuming Rooms has a detailed toString method
        }
        return sb.toString();
    }
    
	public int getCustomerId() {
    	int customerId = this.customers.getCustomerId();
    	return customerId;
    }
    
    public int getBillId() {
    	return bill.getId();
    }
    
    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBill(Bills bill) {
    	this.bill = bill;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public int getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(int noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    public ArrayList<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Rooms> rooms) {
        this.rooms = rooms;
    }

    public String getPricePerNight() {
        return CURRENCY.format(pricePerNight);
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getTotal() {
        return CURRENCY.format(total);
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public String getBookedDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        return sdf.format(bookedDate);
    }

    public String getCheckInDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        return sdf.format(checkInDate);
    }

    public String getCheckOutDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        return sdf.format(checkOutDate);
    }
    
    public Date getDateCheckInDate() {
    	return checkInDate;
    }
    
    public Date getDateCheckOutDate() {
    	return checkOutDate;
    }
}

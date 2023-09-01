package ca.senecacollege.models;

public class Bills {
	private final int id;
	private final int bookingId;
	private final double amountToPay;
	
	public Bills() {
		this(0, 0, 0.0);
	}
	public Bills(int id, int bookingId, double amountToPay) {
		this.id = id;
		this.bookingId = bookingId;
		this.amountToPay = amountToPay;
	}
	public int getId() {
		return id;
	}
	public double getAmountToPay() {
		return amountToPay;
	}
	@Override
	public String toString() {
		return "Bills [id=" + id + ", bookingId=" + bookingId + ", amountToPay=" + amountToPay + "]";
	}

}

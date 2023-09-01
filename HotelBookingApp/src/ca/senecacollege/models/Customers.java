package ca.senecacollege.models;

import java.util.Objects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customers {
	private int customerId;
	private final StringProperty title;
	private final StringProperty fName;
	private final StringProperty lName;
	private final StringProperty address;
	private final IntegerProperty phone;
	private final StringProperty email;
	
	public Customers() {
		this(null,null,null,null,0,null);
	}
	
	public Customers(String ttl, String fname, String lname, String add, int phn, String eml) {
		this.title = new SimpleStringProperty(ttl);
		this.fName = new SimpleStringProperty(fname);
		this.lName = new SimpleStringProperty(lname);
		this.address = new SimpleStringProperty(add);
		this.phone = new SimpleIntegerProperty(phn);
		this.email = new SimpleStringProperty(eml);
	}
	
	public Customers(int customerId, String ttl, String fname, String lname, String add, int phn, String eml) {
		this.customerId = customerId;
		this.title = new SimpleStringProperty(ttl);
		this.fName = new SimpleStringProperty(fname);
		this.lName = new SimpleStringProperty(lname);
		this.address = new SimpleStringProperty(add);
		this.phone = new SimpleIntegerProperty(phn);
		this.email = new SimpleStringProperty(eml);
	}

	public StringProperty getTitle() {
		return title;
	}

	public StringProperty getfName() {
		return fName;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public StringProperty getlName() {
		return lName;
	}

	public StringProperty getAddress() {
		return address;
	}

	public IntegerProperty getPhone() {
		return phone;
	}

	public StringProperty getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "Customers [customerId=" + customerId + ", title=" + title + ", fName=" + fName + ", lName=" + lName
				+ ", address=" + address + ", phone=" + phone + ", email=" + email + "]";
	}

	public String getDetail() {
		return "Customer Id: " + customerId
				+ "\nTitle: " + title.getValue()
				+ "\nName: " + fName.getValue() + " " + lName.getValue()
				+ "\nAdress: " + address.getValue()
				+ "\nPhone: " + phone.getValue()
				+ "\nEmail: " + email.getValue();
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;

	    Customers customers = (Customers) o;

	    if (customerId != customers.customerId) return false;
	    if (!title.getValue().equals(customers.title.getValue())) return false;
	    if (!fName.getValue().equals(customers.fName.getValue())) return false;
	    if (!lName.getValue().equals(customers.lName.getValue())) return false;
	    if (!address.getValue().equals(customers.address.getValue())) return false;
	    if (phone.get() != customers.phone.get()) return false;
	    return email.getValue().equals(customers.email.getValue());
	}

	@Override
	public int hashCode() {
	    int result = customerId;
	    result = 31 * result + title.getValue().hashCode();
	    result = 31 * result + fName.getValue().hashCode();
	    result = 31 * result + lName.getValue().hashCode();
	    result = 31 * result + address.getValue().hashCode();
	    result = 31 * result + phone.get();
	    result = 31 * result + email.getValue().hashCode();
	    return result;
	}
}	

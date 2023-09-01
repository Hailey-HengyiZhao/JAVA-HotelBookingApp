package ca.senecacollege.models;

import java.text.NumberFormat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Rooms {
	
	private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();
	
	private final IntegerProperty id;
	private final StringProperty roomUnit;
	private final StringProperty tpye;
	private final DoubleProperty rate;
	private int maxPeople;
	
	public Rooms() {
		this(0,null, null ,0.0, 0);
	}
	
	public Rooms(int id,String roomUnit,String type, double rate,int maxPeople) {
		this.id = new SimpleIntegerProperty(id);
		this.roomUnit = new SimpleStringProperty(roomUnit);
		this.tpye = new SimpleStringProperty(type);
		this.rate = new SimpleDoubleProperty(rate);
		this.maxPeople = maxPeople;
	}

	public int getMaxPeopleInt() {
		return maxPeople;
	}

	public void setMaxPeople(int maxPeople) {
		this.maxPeople = maxPeople;
	}

	public IntegerProperty getId() {
		return id;
	}

	public StringProperty getTpye() {
		return tpye;
	}

	public StringProperty getRoomUnit() {
		return roomUnit;
	}
	
	public StringProperty getRate() {
		StringProperty rate = new SimpleStringProperty(String.valueOf(CURRENCY.format(this.rate.get())));
		return rate;
	}
	
	public StringProperty getMaxPeople() {
		StringProperty maxPeople = new SimpleStringProperty(String.valueOf(this.maxPeople));
		return maxPeople;
	}

	@Override
	public String toString() {
		return "Rooms [id=" + id + ", roomUnit=" + roomUnit + ", tpye=" + tpye + ", rate=" + rate + ", maxPeople="
				+ maxPeople + "]";
	}
	
	public String getDetail() {
		return "Unit: " + roomUnit.getValue()
				+ " | Type: " + tpye.getValue()
				+ " | maxmiun with " + maxPeople
				+ " People | Price/Night: " + this.getRate().getValue();
	}
	
}

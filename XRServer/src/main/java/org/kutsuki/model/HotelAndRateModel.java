package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HotelAndRateModel {
	private String name;
	private Double rate;
	private Timestamp date;
	private Boolean full;
	private Boolean working;
	
	public HotelAndRateModel() {
		// do nothing
	}
	
	public HotelAndRateModel(ResultSet rs) throws SQLException {
		this.setName(rs.getString(1));
		this.rate = rs.getDouble(2);
		this.date = rs.getTimestamp(3);
		this.full = rs.getBoolean(4);
		this.working = rs.getBoolean(5);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Boolean getFull() {
		return full;
	}

	public void setFull(Boolean full) {
		this.full = full;
	}

	public Boolean getWorking() {
		return working;
	}

	public void setWorking(Boolean working) {
		this.working = working;
	}
}

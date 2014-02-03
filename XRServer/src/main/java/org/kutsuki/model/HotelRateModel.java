package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HotelRateModel {
	private Integer hotelRateId;
	private Integer hid;
	private Double rate;
	private Timestamp date;
	private Boolean full;
	private Boolean working;
	
	public HotelRateModel() {
		// do nothing
	}
	
	public HotelRateModel(ResultSet rs) throws SQLException {
		this.hotelRateId = rs.getInt(1);
		this.hid = rs.getInt(2);
		this.rate = rs.getDouble(3);
		this.date = rs.getTimestamp(4);
		this.full = rs.getBoolean(5);
		this.working = rs.getBoolean(6);
	}

	public Integer getHotelRateId() {
		return hotelRateId;
	}

	public void setHotelRateId(Integer hotelRateId) {
		this.hotelRateId = hotelRateId;
	}

	public Integer getHid() {
		return hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
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

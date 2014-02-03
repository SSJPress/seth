package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelModel {
	private Integer hotelId;
	private Integer hid;
	private String name;
	private Integer locationId;
	
	public HotelModel() {
		// do nothing
	}
	
	public HotelModel(ResultSet rs) throws SQLException {
		this.hotelId = rs.getInt(1);
		this.hid = rs.getInt(2);
		this.name = rs.getString(3);
		this.locationId = rs.getInt(4);
	}

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	public Integer getHid() {
		return hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
}

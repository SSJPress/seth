package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelLocationModel {
	private Integer locationId;
	private String location;
	private String email;
	
	public HotelLocationModel() {
		// do nothing
	}
	
	public HotelLocationModel(ResultSet rs) throws SQLException {
		this.locationId = rs.getInt(1);
		this.location = rs.getString(2);
		this.email = rs.getString(3);
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

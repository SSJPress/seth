package org.press.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShittyModel {
	private String name;
	private String addy;
	
	
	public ShittyModel(ResultSet rs) throws SQLException {
		this.name = rs.getString(0);
		this.name = rs.getString(1);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddy() {
		return addy;
	}
	public void setAddy(String addy) {
		this.addy = addy;
	}

}

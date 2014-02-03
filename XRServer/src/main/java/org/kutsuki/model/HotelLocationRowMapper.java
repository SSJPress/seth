package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HotelLocationRowMapper implements RowMapper<HotelLocationModel> {
	public HotelLocationModel mapRow(ResultSet resultSet, int line) throws SQLException {
		HotelLocationModel model = new HotelLocationModel(resultSet);
		return model;
	}
}

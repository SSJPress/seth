package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HotelRowMapper implements RowMapper<HotelModel> {
	public HotelModel mapRow(ResultSet resultSet, int line) throws SQLException {
		HotelModel model = new HotelModel(resultSet);
		return model;
	}
}

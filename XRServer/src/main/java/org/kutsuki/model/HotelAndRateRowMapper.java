package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HotelAndRateRowMapper implements RowMapper<HotelAndRateModel> {
	public HotelAndRateModel mapRow(ResultSet resultSet, int line) throws SQLException {
		HotelAndRateModel model = new HotelAndRateModel(resultSet);
		return model;
	}
}

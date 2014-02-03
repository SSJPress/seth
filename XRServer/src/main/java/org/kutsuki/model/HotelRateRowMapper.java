package org.kutsuki.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HotelRateRowMapper implements RowMapper<HotelRateModel> {
	public HotelRateModel mapRow(ResultSet resultSet, int line) throws SQLException {
		HotelRateModel model = new HotelRateModel(resultSet);
		return model;
	}
}

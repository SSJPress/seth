package org.press.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ShittyRowMapper implements RowMapper<ShittyModel> {

	public ShittyModel mapRow(ResultSet resultSet, int line) throws SQLException {
		ShittyModel sm = new ShittyModel(resultSet);
		return sm;
	}

}

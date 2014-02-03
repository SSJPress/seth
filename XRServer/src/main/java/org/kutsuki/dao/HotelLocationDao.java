package org.kutsuki.dao;

import org.kutsuki.model.HotelLocationModel;
import org.kutsuki.model.HotelLocationRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class HotelLocationDao extends AbstractDao<HotelLocationModel> {
	private static final String INSERT = "INSERT INTO hotel_location (location, email) VALUES (?, ?)";
	private static final String TABLE_NAME = "hotel_location";
	private static final String UPDATE = "UPDATE hotel_location SET location=?, email=? WHERE locationId = ?";
	private static final HotelLocationRowMapper ROW_MAPPER = new HotelLocationRowMapper();

	@Override
	public String getInsertSql() {
		return INSERT;
	}
	
	@Override
	public String getUpdateSql() {
		return UPDATE;
	}
	
	@Override
	public Object[] getInsertArray(HotelLocationModel model) {
		return new Object[] { model.getLocation(), model.getEmail() };
	}
	
	@Override
	public Object[] getUpdateArray(HotelLocationModel model) {
		return new Object[] { model.getLocation(), model.getEmail(), model.getLocationId() };
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public RowMapper<HotelLocationModel> getRowMapper() {
		return ROW_MAPPER;
	}
}

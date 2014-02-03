package org.kutsuki.dao;

import org.kutsuki.model.HotelModel;
import org.kutsuki.model.HotelRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class HotelDao extends AbstractDao<HotelModel> {
	private static final String INSERT = "INSERT INTO hotel (hid, name, locationId) VALUES (?, ?, ?)";
	private static final String TABLE_NAME = "hotel";
	private static final String UPDATE = "UPDATE hotel SET hid=?, name=?, locationId=? WHERE hotelId = ?";
	private static final HotelRowMapper ROW_MAPPER = new HotelRowMapper();

	@Override
	public String getInsertSql() {
		return INSERT;
	}
	
	@Override
	public String getUpdateSql() {
		return UPDATE;
	}
	
	@Override
	public Object[] getInsertArray(HotelModel model) {
		return new Object[] { model.getHid(), model.getName(), model.getLocationId() };
	}
	
	@Override
	public Object[] getUpdateArray(HotelModel model) {
		return new Object[] { model.getHid(), model.getName(), model.getLocationId(), model.getHotelId() };
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public RowMapper<HotelModel> getRowMapper() {
		return ROW_MAPPER;
	}
}

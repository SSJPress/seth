package org.press.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.press.model.ShittyModel;
import org.press.model.ShittyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ShittyDaoImpl implements ShittyDao {
	@Autowired
	DataSource dataSource;

	private String data;

	public String save() {
		return data;
	}

	public void insertShitty(ShittyModel sm) {

		String sql = "INSERT INTO shitty " + "(name,addy) VALUES (?, ?)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.update(sql, new Object[] { sm.getAddy(), sm.getName() });

	}

	public List<ShittyModel> getShitty() {
		List<ShittyModel> shitList = new ArrayList<ShittyModel>();

		String sql = "select * from shitty";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		shitList = jdbcTemplate.query(sql, new ShittyRowMapper());
		return shitList;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}

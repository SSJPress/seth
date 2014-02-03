package org.kutsuki.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractDao<T> {
	private static final String SELECT = "SELECT * FROM ";
	
	@Autowired
	DataSource dataSource;

	public abstract Object[] getInsertArray(T model);
	public abstract Object[] getUpdateArray(T model);
	public abstract RowMapper<T> getRowMapper();
	public abstract String getInsertSql();
	public abstract String getUpdateSql();
	public abstract String getTableName();

	
	// insert
	public void insert(T model) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(getInsertSql(), getInsertArray(model));
	}
	
	// update
	public void update(T model) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(getInsertSql(), getInsertArray(model));
	}
	
	// selectAll
	public List<T> selectAll() {
		List<T> list = new ArrayList<T>();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		list = jdbcTemplate.query(SELECT + getTableName(), getRowMapper());
		return list;
	}
	
	// setDataSource
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

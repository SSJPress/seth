package org.kutsuki.dao;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.kutsuki.model.EventModel;
import org.kutsuki.model.HotelAndRateModel;
import org.kutsuki.model.HotelAndRateRowMapper;
import org.kutsuki.model.HotelRateModel;
import org.kutsuki.model.HotelRateRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class HotelRateDao extends AbstractDao<HotelRateModel> {
	private static final String INSERT = "INSERT INTO hotel_rate (hid, rate, date, full, working) VALUES (?, ?, ?, ?, ?)";
	private static final String SELECT_ALL_BY_DATE = "SELECT * FROM hotel_rate WHERE date LIKE '";
	private static final String TABLE_NAME = "hotel_rate";
	private static final String UPDATE = "UPDATE hotel_rate SET hid=?, rate=?, date=?, full=?, working=? WHERE hotelRateId = ?";
	private static final String WILD_CARD_END = "%'";
	
	
	private static final String GET_RATES = "Get Rates";
	private static final String GREEN = "green";
	private static final String ORANGE = "orange";
	private static final String RED = "red";
	private static final String RETRIEVING = "Retrieving...";
	private static final String SOLD_OUT = "SOLD OUT! ";
	private static final String SPACE = " ";
	private static final String ZEBRA1 = "#3366FF";
	private static final String ZEBRA2 = "#3399FF";
	
	private static final DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
	private static final HotelRateRowMapper ROW_MAPPER = new HotelRateRowMapper();
	private static final HotelAndRateRowMapper HOTEL_AND_RATE_ROW_MAPPER = new HotelAndRateRowMapper();
	private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();

	public List<EventModel> getRates( int lid, long start, long end ) {
		List<EventModel> eventList = new ArrayList<EventModel>();
		List<HotelAndRateModel> harList = new ArrayList<HotelAndRateModel>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.name, b.rate, b.date, b.full, b.working FROM hotel a, hotel_rate b WHERE a.hid = b.hid AND a.locationId = ");
		sql.append(lid);
		sql.append(" AND UNIX_TIMESTAMP(b.date) > ");
		sql.append(start);
		sql.append(" AND UNIX_TIMESTAMP(b.date) < ");
		sql.append(end);
		sql.append(" ORDER BY DATE_FORMAT(b.date, '%Y%m%d'), a.name");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		harList = jdbcTemplate.query(sql.toString(), HOTEL_AND_RATE_ROW_MAPPER);

		boolean zebra = true;
		String prevDate = "";
		List<String> dateList = new ArrayList<String>();
		for( HotelAndRateModel har : harList ) {
			// add date to date list
			String date = df.format(har.getDate());
			dateList.add(date);
			
			// check if it's a new day
			if( !prevDate.equals(date) ) {
				prevDate = date;
				zebra = true;
			}
			
			EventModel event = new EventModel();
			if( har.getFull() ) {
				// it's full so it's sold out!
				event.setTitle(SOLD_OUT + har.getName() + SPACE + CURRENCY.format(har.getRate()));
				event.setColor(RED);
				
				if( zebra ) {
					zebra = false;
				} else {
					zebra = true;
				}
			} else if( har.getWorking() ) {
				// it's working so we will show retrieving.
				event.setTitle(RETRIEVING);
				event.setColor(GREEN);
			} else {
				// display rate
				event.setTitle(har.getName() + SPACE + CURRENCY.format(har.getRate()));
				
				if( zebra ) {
					event.setColor(ZEBRA1);
					zebra = false;
				} else {
					event.setColor(ZEBRA2);
					zebra = true;
				}
			}
			
			event.setStart(date);
			eventList.add(event);
		}

		// fill in the rest of the calendar
		for( long i = System.currentTimeMillis(); i < end * 1000; i += 86400000 ) {
			String date = df.format(i);

			if( !dateList.contains(date) ) {
				EventModel event = new EventModel();
				event.setTitle(GET_RATES);
				event.setColor(ORANGE);
				event.setStart(date);
				eventList.add(event);
			}
		}

		return eventList;
	}
	
	public List<HotelRateModel> selectAllByDate( String date ) {
		List<HotelRateModel> list = new ArrayList<HotelRateModel>();
		
		String sql = SELECT_ALL_BY_DATE + date + WILD_CARD_END;
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		list = jdbcTemplate.query(sql, getRowMapper());
		return list;
	}
	
	@Override
	public String getInsertSql() {
		return INSERT;
	}
	
	@Override
	public String getUpdateSql() {
		return UPDATE;
	}
	
	@Override
	public Object[] getInsertArray(HotelRateModel model) {
		return new Object[] { model.getHid(), model.getRate(), model.getDate(), model.getFull(), model.getWorking() };
	}
	
	@Override
	public Object[] getUpdateArray(HotelRateModel model) {
		return new Object[] { model.getHid(), model.getRate(), model.getDate(), model.getFull(), model.getWorking(), model.getHotelRateId() };
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public RowMapper<HotelRateModel> getRowMapper() {
		return ROW_MAPPER;
	}
}

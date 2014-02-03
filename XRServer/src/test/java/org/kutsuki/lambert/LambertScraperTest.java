package org.kutsuki.lambert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kutsuki.model.HotelLocationModel;
import org.kutsuki.model.HotelModel;
import org.kutsuki.model.HotelRateModel;
import org.kutsuki.rest.LambertScraper;

public class LambertScraperTest {
	private static final int HID = 64081;
	private static final int LID = 1;
	private static final long TIMESTAMP = 1391374024926L; // 2/2/2014
	private static final String EMAIL = "NanakaKutsuki@gmail.com";
	private static final String LOCATION = "Bozeman, Montana";
	private static final String NAME = "Super 8 Bozeman";

	
	@Test
	public void testGetLink() {
		String expected = "https://www.kayak.com/hotelreservation?HotelId=64081&CheckInDate=02%2F02%2F2014&CheckOutDate=02%2F03%2F2014&NumRooms=1&NumGuests=1&lr=true";
		
		LambertScraper ls = new LambertScraper();
		String link = ls.getLink(HID, TIMESTAMP);
		
		assertEquals("Incorrect Link", expected, link);
	}
	
	@Test
	public void testGetRates() {
		HotelModel hotel = new HotelModel();
		hotel.setHid(HID);
		hotel.setLocationId(LID);
		hotel.setName(NAME);
		
		LambertScraper ls = new LambertScraper();
		HotelRateModel hrm = ls.getRates(hotel, System.currentTimeMillis());
		
		assertNotNull("HID is null", hrm.getHid());
		assertNotNull("Date is null", hrm.getDate());
		assertNotNull("Rate is null", hrm.getRate());
		assertNotNull("Full is null", hrm.getFull());
		assertNotNull("Working is null", hrm.getWorking());
	}
	
	@Test
	public void testEmail() {
		HotelRateModel hrm = new HotelRateModel();
		hrm.setDate(new Timestamp(System.currentTimeMillis()));
		hrm.setFull(false);
		hrm.setRate(25.0);
		hrm.setWorking(false);
		hrm.setHid(HID);
		
		HotelModel hotel = new HotelModel();
		hotel.setHid(HID);
		hotel.setLocationId(LID);
		hotel.setName(NAME);
		
		Map<Integer, HotelLocationModel> hotelLocationMap = new HashMap<Integer, HotelLocationModel>();
		HotelLocationModel hlm = new HotelLocationModel();
		hlm.setEmail(EMAIL);
		hlm.setLocation(LOCATION);
		hlm.setLocationId(LID);
		hotelLocationMap.put(LID, hlm);
		
		boolean success = false;
		LambertScraper ls = new LambertScraper();
		ls.setHotelLocationMap(hotelLocationMap);
		success = ls.emailAlert(hrm, hotel, 1.0);
		
		assertTrue("Email unsuccessfully sent to: " + EMAIL, success);
	}
}

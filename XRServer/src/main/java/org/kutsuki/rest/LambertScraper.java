package org.kutsuki.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.kutsuki.dao.HotelDao;
import org.kutsuki.dao.HotelLocationDao;
import org.kutsuki.dao.HotelRateDao;
import org.kutsuki.model.HotelLocationModel;
import org.kutsuki.model.HotelModel;
import org.kutsuki.model.HotelRateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LambertScraper {
	private static final double ALERT = 10.0;
	private static final DateFormat DF1 = new SimpleDateFormat("yyyy'-'MM'-'dd");
	private static final DateFormat DF2 = new SimpleDateFormat("MM'%2F'dd'%2F'yyyy");
	private static final DateFormat DF3 = new SimpleDateFormat("MMMM dd, yyyy hh:mma");
	private static final long DAY = 1000L * 60L * 60L * 24L;
	private static final long TWO_HOURS = 1000L * 60L * 60L * 2L;
	private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();
	private static final String CHECKIN_DATE = "&CheckInDate=";
	private static final String CHECKOUT_DATE = "&CheckOutDate=";
	private static final String DIV_END = "</div>";
	private static final String HID = "https://www.kayak.com/hotelreservation?HotelId=";
	private static final String HTTP_AGENT = "http.agent";
	private static final String PASSWORD = "PHFxan01";
	private static final String PRICE = "<div class=\"price\">";
	private static final String ROOMS = "&NumRooms=1&NumGuests=1&lr=true";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0";
	private static final String USERNAME = "xanadujobs@gmail.com";
	private static final String TAX = "Tax";
	
	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private HotelLocationDao hotelLocationDao;
	
	@Autowired
	private HotelRateDao hotelRateDao;

	private Logger logger = LoggerFactory.getLogger(LambertScraper.class);
	private List<HotelModel> hotelList;
	private Map<Integer, HotelLocationModel> hotelLocationMap;
	private Map<Integer, HotelRateModel> hotelRateMap;
	
	// default constructor
	public LambertScraper() {
		System.setProperty(HTTP_AGENT, USER_AGENT);
		hotelLocationMap = new HashMap<Integer, HotelLocationModel>();
		hotelRateMap = new HashMap<Integer, HotelRateModel>();
	}
	
	// run
	public void run(long start) {
		getHotelList();
		getHotelRateMap(start);
		getTargetedRates(start);
	}
	
	//@Scheduled(cron = "00 08-20 * * * ?")
	public void dailyScrape() {
		// daily scrape, scrapes 6am - 6pm MST
		getHotelList();
		getHotelRateMap(System.currentTimeMillis());
		getTodaysRates();
	}
	
	// getHotelList
	private void getHotelList() {
		hotelList = hotelDao.selectAll();
		Collections.shuffle(hotelList);
		
		hotelLocationMap.clear();
		for( HotelLocationModel hlm : hotelLocationDao.selectAll() ) {
			hotelLocationMap.put(hlm.getLocationId(), hlm);
		}
	}
	
	// getHotelRateMap
	private void getHotelRateMap(long timestamp) {
		Timestamp ts = new Timestamp(timestamp);
		List<HotelRateModel> hrmList = hotelRateDao.selectAllByDate(DF1.format(ts));
		
		hotelRateMap.clear();
		for( HotelRateModel hrm : hrmList ) {
			hotelRateMap.put(hrm.getHid(), hrm);
		}
	}
	
	// getTodaysRates
	private void getTodaysRates() {
		for( HotelModel hotel : hotelList ) {
			HotelRateModel hrm = getRates(hotel, System.currentTimeMillis());
			
			if( hotelRateMap.containsKey(hotel.getHid()) ) {
				HotelRateModel prev = hotelRateMap.get(hrm.getHid());
				hrm.setHotelRateId(prev.getHotelRateId());
				
				// full
				if( hrm.getRate() == Double.MAX_VALUE ) {
					hrm.setFull(true);
					hrm.setRate(prev.getRate());
					
					if( !prev.getFull() ) {
						emailAlert(hrm, hotel, prev.getRate());
					}
				// alert
				} else if(prev.getRate() > 0 && (hrm.getRate() > prev.getRate() + ALERT || hrm.getRate() < prev.getRate() - ALERT)) {
					emailAlert(hrm, hotel, prev.getRate());
				}

				// update
				hotelRateDao.update(hrm);
			} else if( hrm.getRate() != Double.MAX_VALUE ) {
				hotelRateDao.insert(hrm);
			}
		}
	}
	
	// getTargetedRates
	private void getTargetedRates(long timestamp) {
		for( HotelModel hotel : hotelList ) {
			HotelRateModel hrm = getRates(hotel, timestamp);
			
			if( hotelRateMap.containsKey(hotel.getHid()) ) {
				HotelRateModel prev = hotelRateMap.get(hrm.getHid());
				hrm.setHotelRateId(prev.getHotelRateId());
				
				// full
				if( hrm.getRate() == Double.MAX_VALUE ) {
					hrm.setFull(true);
					hrm.setRate(prev.getRate());
				}
				
				hotelRateDao.update(hrm);
			} else if( hrm.getRate() != Double.MAX_VALUE ) {
				hotelRateDao.insert(hrm);
			}
		}
	}
	
	// getRates
	public HotelRateModel getRates(HotelModel hotel, long timestamp) {
		HotelRateModel hrm = new HotelRateModel();
		
		String link = getLink(hotel.getHid(), timestamp);
		double lowestPrice = Double.MAX_VALUE;
		
		BufferedReader br = null;
		InputStreamReader isr = null;

		try{
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			isr = new InputStreamReader(conn.getInputStream());
			br = new BufferedReader(isr);
			
			boolean searchPrice = false;
			boolean tax = false;
			String line = "";
			while( (line = br.readLine()) != null ) {
				if( line.contains(TAX) ) {
					tax = true;
				}
				
				if( line.contains(PRICE) ) {
					searchPrice = true;
				}
				
				if( searchPrice && line.contains(DIV_END) ) {
					searchPrice = false;
				}
				
				if( searchPrice && line.contains("$")) {
					if( !tax ) {
						String priceLine = line.substring(line.indexOf("$")+1).trim();
						double price = Double.parseDouble(priceLine);
						
						if( price < lowestPrice ) {
							lowestPrice = price;
						}
					} else {
						tax = false;
					}
					
					searchPrice = false;
				}	
			}
			
			hrm.setHid(hotel.getHid());
			hrm.setRate(lowestPrice);
			hrm.setDate(new Timestamp(timestamp));
			hrm.setFull(false);
			hrm.setWorking(false);
		} catch( Exception e ) {
			logger.error("Error getting Rates for: " + hotel.getName() + " at " + DF1.format(timestamp), e);
		} finally {
			if( br != null ) {
				try{
					br.close();
				} catch( Exception e ) {
					// do nothing
				}
			}
			
			if( isr != null ) {
				try{
					isr.close();
				} catch( Exception e ) {
					// do nothing
				}
			}
		}
		
		return hrm;
	}
	
	// getLink
	public String getLink(long hid, long timestamp) {
		StringBuilder sb = new StringBuilder();
		sb.append(HID).append(hid);
		sb.append(CHECKIN_DATE).append(DF2.format(new Timestamp(timestamp)));
		sb.append(CHECKOUT_DATE).append(DF2.format(new Timestamp(timestamp + DAY)));
		sb.append(ROOMS);
		return sb.toString();
	}
	
	// emailAlert
	public boolean emailAlert(HotelRateModel hrm, HotelModel hotel, double prev) {
		boolean success = false;
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props,new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});
 
		try {
			HotelLocationModel hl = hotelLocationMap.get(hotel.getLocationId());
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(hl.getEmail()));
			if( hrm.getFull() ) {
				message.setSubject("Rate Alert: " + hotel.getName() + " in " + hl.getLocation() + " is SOLD OUT!");
			} else {
				message.setSubject("Rate Alert: " + hotel.getName() + " in " + hl.getLocation() + " " + CURRENCY.format(hrm.getRate()));
			}

			// convert to MST
			Timestamp mst = new Timestamp(hrm.getDate().getTime()-TWO_HOURS);
			
			StringBuilder sb = new StringBuilder();
			sb.append(hotel.getName()).append("\n");
			sb.append(hl.getLocation()).append("\n\n");
			
			sb.append("As of ").append(DF3.format(mst)).append(" MST,\n");
			sb.append("Previous Rate: ").append(CURRENCY.format(prev)).append("\n");
			
			if( hrm.getFull() ) {
				sb.append("Current Rate: SOLD OUT!");
			} else {
				sb.append("Current Rate: ").append(CURRENCY.format(hrm.getRate()));
			}

		    MimeBodyPart mbp1 = new MimeBodyPart();
		    mbp1.setText(sb.toString());

		    Multipart mp = new MimeMultipart();
		    mp.addBodyPart(mbp1);
		    message.setContent(mp);
 
			Transport.send(message);
			success = true;
		} catch (Exception e) {
			logger.error("Error sending email about: " + hotel.getName(), e);
		}
		
		return success;
	}
	
	// setHotelLocationMap
	public void setHotelLocationMap(Map<Integer, HotelLocationModel> hotelLocationMap) {
		this.hotelLocationMap = hotelLocationMap;
	}

	// setHotelDao
	public void setHotelDao(HotelDao hotelDao) {
		this.hotelDao = hotelDao;
	}

	// setHotelLocationDao
	public void setHotelLocationDao(HotelLocationDao hotelLocationDao) {
		this.hotelLocationDao = hotelLocationDao;
	}

	// setHotelRateDao
	public void setHotelRateDao(HotelRateDao hotelRateDao) {
		this.hotelRateDao = hotelRateDao;
	}
}

package org.kutsuki.rest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kutsuki.dao.HotelDao;
import org.kutsuki.dao.HotelRateDao;
import org.kutsuki.model.EventModel;
import org.kutsuki.model.HotelModel;
import org.kutsuki.model.HotelRateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/hotelRate")
public class HotelRateRest {
	@Autowired
	private HotelRateDao dao;
	
	@Autowired
	private HotelDao hotelDao;

	@GET
	@Path("/getRates")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EventModel> getRates(@QueryParam("lid") int lid, @QueryParam("start") long start, @QueryParam("end") long end) {
		return dao.getRates(lid, start, end);
	}
	
	@GET
	@Path("/getTargetedRate")
	public Response getRates(@QueryParam("start") long start) {
		Map<Integer, Integer> hotelMap = new HashMap<Integer, Integer>();
		for( HotelModel hotel : hotelDao.selectAll() ) {
			if( !hotelMap.containsKey(hotel.getLocationId()) ) {
				hotelMap.put(hotel.getLocationId(), hotel.getHid());
			}
		}
		
		for( int hid : hotelMap.values() ) {
			HotelRateModel hotelRate = new HotelRateModel();
			hotelRate.setHid(hid);
			hotelRate.setRate(0.0);
			hotelRate.setDate(new Timestamp(start));
			hotelRate.setFull(false);
			hotelRate.setWorking(true);
			dao.insert(hotelRate);
		}
		
		LambertScraper ls = new LambertScraper();
		ls.run(start * 1000);
		
		return Response.status(200).build();
	}
}
package org.kutsuki.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.kutsuki.dao.HotelLocationDao;
import org.kutsuki.model.HotelLocationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/hotelLocation")
public class HotelLocationRest {
	@Autowired
	private HotelLocationDao dao;

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<HotelLocationModel> getAll() {
		return dao.selectAll();
	}
}
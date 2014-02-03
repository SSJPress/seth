package org.kutsuki.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kutsuki.dao.HotelDao;
import org.kutsuki.model.HotelModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/hotel")
public class HotelRest {
	@Autowired
	private HotelDao dao;

	@GET
	@Path("/test")
	public Response testGet() {
		List<HotelModel> list = dao.selectAll();
		return Response.status(200).entity(list.get(0).getName()).build();
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<HotelModel> testJson() {
		return dao.selectAll();
	}
}
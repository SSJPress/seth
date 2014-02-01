package org.press.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.press.dao.ShittyDaoImpl;
import org.press.model.ShittyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/")
public class HotelRest {

	@Autowired
	private ShittyDaoImpl sd;

	@GET
	@Path("/press")
	public Response testMethod() {
		String result = sd.save();

		return Response.status(200).entity(result).build();
	}

	@GET
	@Path("/test")
	public Response testGet() {
		List<ShittyModel> shittyList = sd.getShitty();

		return Response.status(200).entity(shittyList.get(0).getName()).build();
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShittyModel> testJson() {
		List<ShittyModel> sl = sd.getShitty();

		return sl;
	}
	
	@GET
	@Path("/one")
	@Produces(MediaType.APPLICATION_JSON)
	public ShittyModel returnOneRecord() {
		List<ShittyModel> sl = sd.getShitty();

		return sl.get(0);
	}

	

}

package org.press.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.press.dao.ShittyDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Path("/")
public class HotelRest {
	
	@Autowired
	private ShittyDaoImpl sd;
	
	@GET
	@Path("/press")
	public Response testMethod(){
		String result = sd.save();
		
		return Response.status(200).entity(result).build();
	}

}

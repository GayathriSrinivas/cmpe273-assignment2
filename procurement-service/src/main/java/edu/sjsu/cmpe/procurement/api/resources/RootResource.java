package edu.sjsu.cmpe.procurement.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.sjsu.cmpe.procurement.domain.OrderBook;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootResource {
	
	
	public RootResource() {
		
	}
	
	@GET
    public Response sayHello(){
    	return Response.ok().build();
    } 
}

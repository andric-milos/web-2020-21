package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.TipRestorana;
import dao.KorisnikDAO;

@Path("/restaurant")
public class RestoranService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	public RestoranService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnici") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("korisnici", new KorisnikDAO(path));
		}
	}
	
	@GET
	@Path("/types")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTypesOfRestaurant() {
		TipRestorana tipovi[] = TipRestorana.values();
		
		ArrayList<TipRestorana> dto = new ArrayList<TipRestorana>();
		for (TipRestorana t : tipovi) {
			dto.add(t);
		}
		
		return Response.status(Status.OK).entity(dto).build();
	}

}

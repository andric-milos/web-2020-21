package services;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Korisnik;
import dao.KorisnikDAO;

@Path("/user")
public class UserService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	public UserService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnici") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("korisnici", new KorisnikDAO(path));
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Korisnik k) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik je vec ulogovan
		if (korisnik != null) {
			return Response.status(Status.BAD_REQUEST).entity("ALREADY LOGGED IN").build();
		}
		
		// 2. scenario: nevalidan unos podataka
		if (k.getKorisnickoIme().equals("") || k.getLozinka().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		HashMap<String, Korisnik> korisnici = korisnikDAO.getKorisniciHashMap();
		
		// 3. scenario: korisnik sa unetim korisnickim imenom ne postoji
		if (!korisnici.containsKey(k.getKorisnickoIme()))
			return Response.status(Status.BAD_REQUEST).entity("USER DOES NOT EXIST").build();
		
		korisnik = korisnici.get(k.getKorisnickoIme());	
		if (korisnik.getLozinka().equals(k.getLozinka())) {
			// 4. scenario: kredencijali su dobri
			request.getSession().setAttribute("korisnik", korisnik);
			return Response.status(Status.OK).entity(korisnik.getTipKorisnika().toString()).build();
		} else {
			// 5. scenario: uneto korisnicko ime postoji, ali sifra je pogresna
			return Response.status(Status.BAD_REQUEST).entity("WRONG PASSWORD").build();
		}
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else {
			request.getSession().invalidate();
			return Response.status(Status.OK).build();
		}
	}
	
	@GET
	@Path("/loggedIn")
	public Response loggedIn() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.OK).entity("NOT LOGGED IN").build();
		} else {
			return Response.status(Status.OK).entity(korisnik.getTipKorisnika().toString()).build();
		}
	}

}

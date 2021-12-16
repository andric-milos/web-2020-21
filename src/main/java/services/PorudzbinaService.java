package services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Korisnik;
import beans.Korpa;
import beans.Porudzbina;
import beans.StatusPorudzbine;
import beans.TipKorisnika;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dto.PorudzbinaDTO;

@Path("/order")
public class PorudzbinaService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	public PorudzbinaService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnici") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("korisnici", new KorisnikDAO(path));
		}
		
		if (ctx.getAttribute("restorani") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("restorani", new RestoranDAO(path));
		}
		
		if (ctx.getAttribute("porudzbine") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("porudzbine", new PorudzbinaDAO(path));
		}
	}
	
	public static String generateId() {
		char[] arrayOfChars = {
			'0', '1', '2', '3' ,'4' ,'5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'O', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};
		
		int max = arrayOfChars.length - 1;
		int min = 0;
		int range = max - min + 1;
		
		int lengthOfId = 10;
		String id = "";
		
		for (int i = 0; i < lengthOfId; i++) {
			id += arrayOfChars[(int)(Math.random() * range) + min];
		}
		
		return id;
	}

	@POST
	public Response createOrder() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		if (!korisnikDAO.getKupciHashMap().containsKey(korisnik.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A CUSTOMER").build();
		}
		
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		if (korpa == null) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY CART").build();
		} else if (korpa.getArtikli().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY CART").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		String id = null;
		
		boolean unique = false;
		while(!unique) {
			id = generateId();
			System.out.println("Generated id: " + id);
			
			if (!porudzbinaDAO.getAllPorudzbineHashMap().containsKey(id)) {
				unique = true;
			}
		}
		
		Porudzbina porudzbina = new Porudzbina(
			id,
			korpa.getArtikli(),
			korpa.getArtikli().get(0).getArtikal().getRestoran(),
			new Date(),
			korpa.getCena(),
			korisnik.getKorisnickoIme(),
			StatusPorudzbine.OBRADA
		);
		
		porudzbinaDAO.dodajPorudzbinu(porudzbina);	// u okviru ove metode se vrsi i serijalizacija
		
		request.getSession().removeAttribute("korpa");	// isprazni korpu
		
		korisnikDAO.dodajPoeneZaKreiranuPorudzbinu(korisnik.getKorisnickoIme(), korpa.getCena());
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	public Response getAllOrders() {
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		
		return Response.status(Status.OK).entity(porudzbinaDAO.getAllPorudzbineList()).build();
	}
	
	@GET
	@Path("/customer")
	public Response getAllOfCustomersOrders() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.KUPAC)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A CUSTOMER").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		List<PorudzbinaDTO> porudzbine = porudzbinaDAO.findAllPorudzbineByKupac(korisnik.getKorisnickoIme());
		
		return Response.status(Status.OK).entity(porudzbine).build();
	}
	
	@PUT
	@Path("/cancel/{id}")
	public Response cancelOrder(@PathParam("id") String id) {
		// porudzbina moze biti oktazana samo ako je porudzbina u statusu OBRADA
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.KUPAC)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A CUSTOMER").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		Porudzbina porudzbina = porudzbinaDAO.getPorudzbinaByItsId(id);
		
		if (porudzbina == null) {
			return Response.status(Status.BAD_REQUEST).entity("NON EXISTING ID").build();
		}
		
		if (!porudzbina.getKupac().equals(korisnik.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG CUSTOMER").build();
		}
		
		/* Porudzbina moze biti otkazana samo ako je njen status "U OBRADI" */
		if (!porudzbina.getStatus().equals(StatusPorudzbine.OBRADA)) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG STATUS").build();
		}
		
		boolean porudzbinaOtkazana = porudzbinaDAO.otkaziPorudzbinu(porudzbina);
		
		if (!porudzbinaOtkazana) {
			return Response.status(Status.BAD_REQUEST).entity("ERROR DURING THE PROCESS OF CANCELING").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		korisnikDAO.oduzmiPoeneZaOtkazanuPorudzbinu(korisnik.getKorisnickoIme(), porudzbina.getCena());
		
		return Response.status(Status.OK).build();
	}
}

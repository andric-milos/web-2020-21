package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Dostavljac;
import beans.Korisnik;
import beans.Korpa;
import beans.Menadzer;
import beans.Porudzbina;
import beans.StatusPorudzbine;
import beans.TipKorisnika;
import beans.ZahtevZaDostavu;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dao.ZahtevZaDostavuDAO;
import dto.PorudzbinaDTO;
import dto.ZahtevZaDostavuDTO;

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
		
		if (ctx.getAttribute("zahtevi") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("zahtevi", new ZahtevZaDostavuDAO(path));
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
		// porudzbinu moze da otkaze samo korisnik koji je i kreirao
		
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
	
	@GET
	@Path("/manager")
	public Response getAllOfManagersOrders() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.MENADZER)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A MANAGER").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());
		
		if (menadzer.getRestoran() == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT NULL").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		List<PorudzbinaDTO> porudzbine = porudzbinaDAO.findAllPorudzbineByRestoran(menadzer.getRestoran());
		
		return Response.status(Status.OK).entity(porudzbine).build();
	}
	
	@PUT
	@Path("/prepare/{id}")
	public Response processOrder(@PathParam("id") String id) {
		// Menjanje statusa porudzbine iz "OBRADA" u "U_PRIPREMI"
		// Dakle, ova akcija moze da se izvrsi, samo ako je porudzbina sa statusom "OBRADA"
		// Ovu akciju moze da izvrsi samo menadzer restorana iz kojeg je porudzbina
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.MENADZER)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A MANAGER").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		Porudzbina porudzbina = porudzbinaDAO.getPorudzbinaByItsId(id);
		
		if (porudzbina == null) {
			return Response.status(Status.BAD_REQUEST).entity("NON EXISTING ID").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());
		
		if (menadzer.getRestoran() == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT NULL").build();
		}
		
		if (!porudzbina.getRestoran().equals(menadzer.getRestoran())) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG MANAGER").build();
		}
		
		/* Status porudzbine mora biti "OBRADA" */
		if (!porudzbina.getStatus().equals(StatusPorudzbine.OBRADA)) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG STATUS").build();
		}
		
		boolean statusPromenjen = porudzbinaDAO.pripremanjePorudzbine(porudzbina);
		
		if (!statusPromenjen) {
			return Response.status(Status.BAD_REQUEST).entity("STATUS OF THE ORDER HAS NOT CHANGED").build();
		}
		
		return Response.status(Status.OK).build();
	}
	
	@PUT
	@Path("/done/{id}")
	public Response orderDone(@PathParam("id") String id) {
		// Menjanje statusa porudzbine iz "U_PRIPREMI" U "CEKA_DOSTAVLJACA"
		// Dakle, ova akcija moze da se izvrsi, samo ako je porudzbina sa statusom "U_PRIPREMI"
		// Ovu akciju moze da izvrsi samo menadzer restorana iz kojeg je porudzbina
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.MENADZER)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A MANAGER").build();
		}
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		Porudzbina porudzbina = porudzbinaDAO.getPorudzbinaByItsId(id);
		
		if (porudzbina == null) {
			return Response.status(Status.BAD_REQUEST).entity("NON EXISTING ID").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());
		
		if (menadzer.getRestoran() == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT NULL").build();
		}
		
		if (!porudzbina.getRestoran().equals(menadzer.getRestoran())) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG MANAGER").build();
		}
		
		/* Status porudzbine mora biti "U_PRIPREMI" */
		if (!porudzbina.getStatus().equals(StatusPorudzbine.U_PRIPREMI)) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG STATUS").build();
		}
		
		boolean statusPromenjen = porudzbinaDAO.porudzbinaPripremljena(porudzbina);
		
		if (!statusPromenjen) {
			return Response.status(Status.BAD_REQUEST).entity("STATUS OF THE ORDER HAS NOT CHANGED").build();
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/deliverer")
	public Response getAllOfDeliverersOrders() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.DOSTAVLJAC)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A DELIVERER").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Dostavljac dostavljac = korisnikDAO.getDostavljaciHashMap().get(korisnik.getKorisnickoIme());
		
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		List<PorudzbinaDTO> porudzbine = porudzbinaDAO.findAllPorudzbineToShowToDostavljac(dostavljac);
		
		return Response.status(Status.OK).entity(porudzbine).build();
	}
	
	@POST
	@Path("/deliveryRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDeliveryRequest(ZahtevZaDostavu zahtev) {
		// Korisnik mora biti dostavljac
		// Zahtev za dostavu se cuva
		// Menadzer restorana mora da odobri zahtev i onda se status porudzbine menja u "U_TRANSPORTU"
		// Salje se samo id porudzbine kroz parametar "zahtev"
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.DOSTAVLJAC)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A DELIVERER").build();
		}
		
		// Validacija - prosledjeni id ne sme biti prazan
		zahtev.setId_porudzbine(zahtev.getId_porudzbine().trim());
		if (zahtev.getId_porudzbine() == null || zahtev.getId_porudzbine().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("ID IS NULL").build();
		}
		
		// Validacija - porudzbina sa prosledjenim id-em ne postoji
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		if (!porudzbinaDAO.getAllPorudzbineHashMap().containsKey(zahtev.getId_porudzbine())) {
			return Response.status(Status.BAD_REQUEST).entity("NON EXISTING ORDER").build();
		}
		
		ZahtevZaDostavuDAO zahtevDAO = (ZahtevZaDostavuDAO) ctx.getAttribute("zahtevi");
		String key = zahtev.getId_porudzbine() + "_" + korisnik.getKorisnickoIme();	// key - idPorudzbine_dostavljac
		if (zahtevDAO.getAllZahteviHashMap().containsKey(key)) {
			return Response.status(Status.BAD_REQUEST).entity("REQUEST ALREADY MADE").build();
		}
		
		zahtev.setDostavljac(korisnik.getKorisnickoIme());
		zahtev.setDatum(new Date());
		zahtevDAO.dodajZahtev(zahtev);	// u okviru ove metode se vrsi i serijalizacija
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/requests")
	public Response getAllDeliveryRequests() {
		ZahtevZaDostavuDAO zahtevDAO = (ZahtevZaDostavuDAO) ctx.getAttribute("zahtevi");
		List<ZahtevZaDostavu> zahtevi = zahtevDAO.getAllZahteviList();
		
		return Response.status(Status.OK).entity(zahtevi).build();
	}
	
	@GET
	@Path("/managersRequests")
	public Response getAllDeliveryRequestsForManagersRestaurant() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.MENADZER)) {
			return Response.status(Status.BAD_REQUEST).entity("NOT A MANAGER").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());
		String restoran = menadzer.getRestoran();
		
		HashMap<String, Porudzbina> porudzbineIzMenadzerovogRestorana = new HashMap<String, Porudzbina>();
		PorudzbinaDAO porudzbinaDAO = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		for (Porudzbina p : porudzbinaDAO.getAllPorudzbineCollection()) {
			if (p.getRestoran().equals(restoran)) {
				porudzbineIzMenadzerovogRestorana.put(p.getId(), p);
			}
		}
		
		ZahtevZaDostavuDAO zahtevDAO = (ZahtevZaDostavuDAO) ctx.getAttribute("zahtevi");
		List<ZahtevZaDostavuDTO> zahtevi = new ArrayList<ZahtevZaDostavuDTO>();
		for (ZahtevZaDostavu z : zahtevDAO.getAllZahteviCollection()) {
			if (porudzbineIzMenadzerovogRestorana.containsKey(z.getId_porudzbine())) {
				Porudzbina p = porudzbineIzMenadzerovogRestorana.get(z.getId_porudzbine());
				
				ZahtevZaDostavuDTO dto = new ZahtevZaDostavuDTO();
				dto.setId_porudzbine(z.getId_porudzbine());
				dto.setRestoran(restoran);
				dto.setDatumZahteva(z.getDatum().getTime());
				dto.setDatumPorudzbine(p.getVremePorudzbine().getTime());
				dto.setDostavljac(z.getDostavljac());
				dto.setCena(p.getCena());
				dto.setKupac(p.getKupac());
				dto.setStatus(p.getStatus());
				
				zahtevi.add(dto);
			}
		}
		
		return Response.status(Status.OK).entity(zahtevi).build();
	}
	
}

package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Dostavljac;
import beans.Korisnik;
import beans.Korpa;
import beans.Kupac;
import beans.Menadzer;
import beans.Pol;
import beans.StaticMethods;
import beans.TipKorisnika;
import beans.sort.SortKorisnikDTOByFirstnameAscending;
import beans.sort.SortKorisnikDTOByFirstnameDescending;
import beans.sort.SortKorisnikDTOByLastnameAscending;
import beans.sort.SortKorisnikDTOByLastnameDescending;
import beans.sort.SortKorisnikDTOByUsernameAscending;
import beans.sort.SortKorisnikDTOByUsernameDescending;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dao.ZahtevZaDostavuDAO;
import dto.KorisnikDTO;
import dto.MenadzerDTO;
import dto.PromenaLozinkeDTO;
import dto.RegistracijaDTO;

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
		
		if (ctx.getAttribute("komentari") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("komentari", new KomentarDAO(path));
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
			
			Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
			if (korpa != null && !korisnik.getTipKorisnika().equals(TipKorisnika.KUPAC)) {
				request.getSession().removeAttribute("korpa");
			}
			
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
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(RegistracijaDTO dto) {
		// 1. scenario: nevalidan unos podataka
		if (dto.getKorisnickoIme().equals("") ||
			dto.getLozinka().equals("") ||
			dto.getIme().equals("") ||
			dto.getPrezime().equals("") ||
			dto.getPotvrda_lozinke().equals("") ||
			dto.getPol().equals("") ||
			dto.getDatumRodjenja().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
		
		// 2. scenario: nevalidan unos pola
		if (!(dto.getPol().equals("MUSKO") || dto.getPol().equals("ZENSKO"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID GENDER").build();
		}
		
		// 3. scenario: datum ne valja
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date datumRodjenja = null;
		try {
			datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
		} catch (ParseException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID DATE FORMAT").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		HashMap<String, Korisnik> korisnici = korisnikDAO.getKorisniciHashMap();
		
		// 4. scenario: korisnicko ime je zauzeto tj. vec postoji korisnik sa takvim korisnickim imenom
		if (korisnici.containsKey(dto.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("USERNAME ALREADY TAKEN").build();
		}
		
		// 5. scenario: sadržaj polja za potvrdu lozinke se ne poklapa sa unetom lozinkom
		if (!dto.getLozinka().equals(dto.getPotvrda_lozinke())) {
			return Response.status(Status.BAD_REQUEST).entity("PASSWORDS DO NOT MATCH").build();
		}
		
		// 6. scenario: sve je u redu
		Korisnik korisnik = new Korisnik(
			dto.getKorisnickoIme(),
			dto.getLozinka(),
			dto.getIme(),
			dto.getPrezime(),
			Pol.stringToPol(dto.getPol()),
			datumRodjenja,
			TipKorisnika.KUPAC,
			false
		);
		
		/* TipKupca: ime, popust, brojBodova
		 * 		3 kategorije:
		 * 			- pocetnik, 0, 0
		 * 			- bronzana, 5, 50
		 * 			- srebrna, 10, 100
		 * 			- zlatna, 15, 150
		 */
		
		Kupac kupac = new Kupac(korisnik);
		
		korisnikDAO.dodajKupca(kupac);
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.OK).entity("NOT ADMIN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.ADMINISTRATOR)) {
			return Response.status(Status.OK).entity("NOT ADMIN").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		List<KorisnikDTO> responseData = new ArrayList<KorisnikDTO>();
		
		for (Korisnik k : korisnikDAO.getAllKorisnici()) {
			KorisnikDTO dto = new KorisnikDTO();
			dto.setKorisnickoIme(k.getKorisnickoIme());
			dto.setIme(k.getIme());
			dto.setPrezime(k.getPrezime());
			dto.setPol(Pol.polToString(k.getPol()));
			dto.setTipKorisnika(TipKorisnika.tipKorisnikaToString(k.getTipKorisnika()));
			dto.setDatumRodjenja(dateFormat.format(k.getDatumRodjenja()));
			dto.setObrisan(k.getObrisan());
			
			responseData.add(dto);
		}
		
		return Response.status(Status.OK).entity(responseData).build();
	}
	
	@GET
	@Path("/getLoggedInUserData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLoggedInUserData() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.OK).entity("NOT LOGGED IN").build();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		KorisnikDTO dto = new KorisnikDTO();
		dto.setKorisnickoIme(korisnik.getKorisnickoIme());
		dto.setIme(korisnik.getIme());
		dto.setPrezime(korisnik.getPrezime());
		dto.setDatumRodjenja(dateFormat.format(korisnik.getDatumRodjenja()));
		dto.setPol(Pol.polToString(korisnik.getPol()));
		dto.setTipKorisnika(TipKorisnika.tipKorisnikaToString(korisnik.getTipKorisnika()));
		dto.setObrisan(korisnik.getObrisan());
		
		return Response.status(Status.OK).entity(dto).build();
	}
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserInfo(KorisnikDTO dto) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: nevalidan unos podataka
		if (dto.getKorisnickoIme().equals("") ||
			dto.getIme().equals("") ||
			dto.getPrezime().equals("") ||
			dto.getPol().equals("") ||
			dto.getDatumRodjenja().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
		
		// 3. scenario: nevalidan unos pola
		if (!(dto.getPol().equals("MUSKO") || dto.getPol().equals("ZENSKO"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID GENDER").build();
		}
		
		// 4. scenario: datum ne valja
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date datumRodjenja = null;
		try {
			datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
		} catch (ParseException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID DATE FORMAT").build();
		}
		
		// 5. scenario: korisnik ne postoji
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		if (!korisnikDAO.getKorisniciHashMap().containsKey(dto.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("USER DOES NOT EXIST").build();
		}
		
		// 6. scenario: ulogovani korisnik != korisnik ciji se podaci menjaju
		if (!korisnik.getKorisnickoIme().equals(dto.getKorisnickoIme())) {
			return Response.status(Status.FORBIDDEN).entity("CANNOT CHANGE OTHERS' DATA").build();
		}
		
		Boolean success = korisnikDAO.azurirajKorisnika(dto);
		
		// 7. scenario: korisnik je uspesno azuriran
		if (success) {
			return Response.status(Status.OK).entity("SUCCESS").build();
		}
		
		// 8. scenario: korisnk nije azuriran uspesno
		return Response.status(Status.BAD_REQUEST).entity("SOMETHING WENT WRONG").build();
	}

	@PUT
	@Path("/changePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(PromenaLozinkeDTO dto) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: nevalidan unos podataka
		if (dto.getLozinka().equals("") ||
			dto.getNova_lozinka().equals("") ||
			dto.getPotvrda_nove_lozinke().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
		
		// 3. scenario: netacna lozinka
		if (!dto.getLozinka().equals(korisnik.getLozinka())) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG PASSWORD").build();
		}
		
		// 4. scenario: sadrzaj polja za potvrdu lozinke se ne poklapa sa unetom novom lozinkom
		if (!dto.getNova_lozinka().equals(dto.getPotvrda_nove_lozinke())) {
			return Response.status(Status.BAD_REQUEST).entity("PASSWORDS DO NOT MATCH").build();
		}
		
		// 5. scenario: nema potrebe za promenom lozinke, jer je uneta ista
		if (dto.getNova_lozinka().equals(korisnik.getLozinka())) {
			return Response.status(Status.OK).entity("SUCCESS").build();
		}
		
		// 6. scenario: nova_lozinka != stara_lozinka
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Boolean success = korisnikDAO.promeniLozinku(korisnik, dto.getNova_lozinka());
		
		if (success) {
			return Response.status(Status.OK).entity("SUCCESS").build();
		} 
		
		return Response.status(Status.BAD_REQUEST).entity("SOMETHING WENT WRONG").build();
	}
	
	@POST
	@Path("/newManager")
	public Response addNewManager(RegistracijaDTO dto) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: tipKorisnika mora biti ADMINISTRATOR
		if (korisnik.getTipKorisnika() != TipKorisnika.ADMINISTRATOR) {
			return Response.status(Status.FORBIDDEN).entity("NOT ADMINISTRATOR").build();
		}
		
		// 3. scenario: nevalidan unos podataka
		dto.setKorisnickoIme(dto.getKorisnickoIme().trim());
		dto.setIme(dto.getIme().trim());
		dto.setPrezime(dto.getPrezime().trim());
		
		if (dto.getKorisnickoIme() == null || dto.getKorisnickoIme().equals("") ||
			dto.getLozinka().equals("") ||
			dto.getIme() == null || dto.getIme().equals("") ||
			dto.getPrezime() == null || dto.getPrezime().equals("") ||
			dto.getPotvrda_lozinke().equals("") ||
			dto.getPol().equals("") ||
			dto.getDatumRodjenja().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
				
		// 4. scenario: nevalidan unos pola
		if (!(dto.getPol().equals("MUSKO") || dto.getPol().equals("ZENSKO"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID GENDER").build();
		}
				
		// 5. scenario: datum ne valja
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date datumRodjenja = null;
		try {
			datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
		} catch (ParseException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID DATE FORMAT").build();
		}
				
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		HashMap<String, Korisnik> korisnici = korisnikDAO.getKorisniciHashMap();
				
		// 6. scenario: korisnicko ime je zauzeto tj. vec postoji korisnik sa takvim korisnickim imenom
		if (korisnici.containsKey(dto.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("USERNAME ALREADY TAKEN").build();
		}
				
		// 7. scenario: sadrzaj polja za potvrdu lozinke se ne poklapa sa unetom lozinkom
		if (!dto.getLozinka().equals(dto.getPotvrda_lozinke())) {
			return Response.status(Status.BAD_REQUEST).entity("PASSWORDS DO NOT MATCH").build();
		}
		
		// 8. scenario: sve je u redu
		Korisnik noviKorisnik = new Korisnik(
			dto.getKorisnickoIme(),
			dto.getLozinka(),
			dto.getIme(),
			dto.getPrezime(),
			Pol.stringToPol(dto.getPol()),
			datumRodjenja,
			TipKorisnika.MENADZER,
			false
		);
		
		Menadzer menadzer = new Menadzer(noviKorisnik);
		
		korisnikDAO.dodajMenadzera(menadzer);
		
		return Response.status(Status.OK).build();
	}
	
	@POST
	@Path("/newDeliverer")
	public Response addNewDeliverer(RegistracijaDTO dto) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: tipKorisnika mora biti ADMINISTRATOR
		if (korisnik.getTipKorisnika() != TipKorisnika.ADMINISTRATOR) {
			return Response.status(Status.FORBIDDEN).entity("NOT ADMINISTRATOR").build();
		}
		
		// 3. scenario: nevalidan unos podataka
		if (dto.getKorisnickoIme().equals("") ||
			dto.getLozinka().equals("") ||
			dto.getIme().equals("") ||
			dto.getPrezime().equals("") ||
			dto.getPotvrda_lozinke().equals("") ||
			dto.getPol().equals("") ||
			dto.getDatumRodjenja().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
				
		// 4. scenario: nevalidan unos pola
		if (!(dto.getPol().equals("MUSKO") || dto.getPol().equals("ZENSKO"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID GENDER").build();
		}
				
		// 5. scenario: datum ne valja
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date datumRodjenja = null;
		try {
			datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
		} catch (ParseException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID DATE FORMAT").build();
		}
				
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		HashMap<String, Korisnik> korisnici = korisnikDAO.getKorisniciHashMap();
				
		// 6. scenario: korisnicko ime je zauzeto tj. vec postoji korisnik sa takvim korisnickim imenom
		if (korisnici.containsKey(dto.getKorisnickoIme())) {
			return Response.status(Status.BAD_REQUEST).entity("USERNAME ALREADY TAKEN").build();
		}
				
		// 7. scenario: sadrzaj polja za potvrdu lozinke se ne poklapa sa unetom lozinkom
		if (!dto.getLozinka().equals(dto.getPotvrda_lozinke())) {
			return Response.status(Status.BAD_REQUEST).entity("PASSWORDS DO NOT MATCH").build();
		}
		
		// 8. scenario: sve je u redu
		Korisnik noviKorisnik = new Korisnik(
			dto.getKorisnickoIme(),
			dto.getLozinka(),
			dto.getIme(),
			dto.getPrezime(),
			Pol.stringToPol(dto.getPol()),
			datumRodjenja,
			TipKorisnika.DOSTAVLJAC,
			false
		);
		
		Dostavljac dostavljac = new Dostavljac(noviKorisnik);
		
		korisnikDAO.dodajDostavljaca(dostavljac);
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/allAvailableManagers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAvailableManagers() {
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		List<MenadzerDTO> available = korisnikDAO.slobodniMenadzeri();
		
		return Response.status(Status.OK).entity(available).build();
	}
	
	@GET
	@Path("/search")
	public Response search(@DefaultValue("") @QueryParam("firstname") String firstname,
						   @DefaultValue("") @QueryParam("lastname") String lastname,
						   @DefaultValue("") @QueryParam("username") String username,
						   @DefaultValue("username") @QueryParam("sortBy") String sortBy,
						   @DefaultValue("asc") @QueryParam("sortOrder") String sortOrder,
						   @DefaultValue("") @QueryParam("typeOfUser") String typeOfUser) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		} else if (!korisnik.getTipKorisnika().equals(TipKorisnika.ADMINISTRATOR)) {
			return Response.status(Status.FORBIDDEN).entity("NOT ADMINISTRATOR").build();
		}
		
		/*
		System.out.println("firstname: " + firstname);
		System.out.println("lastname: " + lastname);
		System.out.println("username: " + username);
		System.out.println("sortBy: " + sortBy);
		System.out.println("sortOrder: " + sortOrder);
		System.out.println("typeOfUser: " + typeOfUser);
		*/
		
		if (!(sortOrder.equals("asc") || sortOrder.equals("desc"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID SORT ORDER").build();
		}
		
		if (!(sortBy.equals("username") || sortBy.equals("firstname") || sortBy.equals("lastname"))) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID SORT BY").build();
		}
		
		List<String> existingTypes = Arrays.asList("customer", "administrator", "deliverer", "manager");
		List<String> types = Arrays.asList(typeOfUser.split(","));
		
		if (!typeOfUser.equals("")) {
			for (String t : types) {
				if (!existingTypes.contains(t)) {
					// System.out.println("Non existing type of user: " + t);
					return Response.status(Status.BAD_REQUEST).entity("NON EXISTING TYPE OF USER").build();
				}
			}
		} else {
			return Response.status(Status.OK).entity(new ArrayList<KorisnikDTO>()).build();
		}
		
		List<KorisnikDTO> korisnici = new ArrayList<KorisnikDTO>();
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		if (types.contains("customer")) {
			korisnici.addAll(korisnikDAO.getAllKupciAsKorisnici());
		} 
		
		if (types.contains("administrator")) {
			korisnici.addAll(korisnikDAO.getAllAdministratoriAsKorisnici());
		}
		
		if (types.contains("deliverer")) {
			korisnici.addAll(korisnikDAO.getAllDostavljaciAsKorisnici());
		}
		
		if (types.contains("manager")) {
			korisnici.addAll(korisnikDAO.getAllMenadzeriAsKorisnici());
		}
		
		if (!username.equals("")) {
			Iterator<KorisnikDTO> i = korisnici.iterator();
			
			while (i.hasNext()) {
				KorisnikDTO k = i.next();
				
				String transformedUsername = StaticMethods.transformToNonDiacritical(username);
				if (!k.getKorisnickoIme().toLowerCase().startsWith(transformedUsername.toLowerCase())) {
					i.remove();
				}
			}
		}
		
		if (!firstname.equals("")) {
			Iterator<KorisnikDTO> i = korisnici.iterator();
			
			while (i.hasNext()) {
				KorisnikDTO k = i.next();
				
				String transformedFirstname = StaticMethods.transformToNonDiacritical(firstname);
				if (!k.getIme().toLowerCase().startsWith(transformedFirstname.toLowerCase())) {
					i.remove();
				}
			}
		}
		
		if (!lastname.equals("")) {
			Iterator<KorisnikDTO> i = korisnici.iterator();
			
			while (i.hasNext()) {
				KorisnikDTO k = i.next();
				
				String transformedLastname = StaticMethods.transformToNonDiacritical(lastname);
				if (!k.getPrezime().toLowerCase().startsWith(transformedLastname.toLowerCase())) {
					i.remove();
				}
			}
		}
		
		if (sortBy.equals("username") && sortOrder.equals("asc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByUsernameAscending());
		} else if (sortBy.equals("username") && sortOrder.equals("desc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByUsernameDescending());
		} else if (sortBy.equals("firstname") && sortOrder.equals("asc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByFirstnameAscending());
		} else if (sortBy.equals("firstname") && sortOrder.equals("desc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByFirstnameDescending());
		} else if (sortBy.equals("lastname") && sortOrder.equals("asc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByLastnameAscending());
		} else if (sortBy.equals("lastname") && sortOrder.equals("desc")) {
			Collections.sort(korisnici, new SortKorisnikDTOByLastnameDescending());
		}
		
		return Response.status(Status.OK).entity(korisnici).build();
	}
}

package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.Adresa;
import beans.Korisnik;
import beans.Lokacija;
import beans.Menadzer;
import beans.Restoran;
import beans.StatusRestorana;
import beans.TipKorisnika;
import beans.TipRestorana;
import dao.KorisnikDAO;
import dao.RestoranDAO;

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
		
		if (ctx.getAttribute("restorani") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("restorani", new RestoranDAO(path));
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
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createNewRestaurant(@FormDataParam("nazivRestorana") String nazivRestorana,
										@FormDataParam("tipRestorana") String tipRestorana,
										@FormDataParam("geoSirina") float geoSirina,
										@FormDataParam("geoDuzina") float geoDuzina,
										@FormDataParam("ulica") String ulica,
										@FormDataParam("broj") int broj,
										@FormDataParam("mesto") String mesto,
										@FormDataParam("postanskiBroj") int postanskiBroj,
										@FormDataParam("menadzer") String menadzer,
										@FormDataParam("logo") File logo) {
		/* ako parametri koji su primitivnog tipa float ili int (geoSirina, geoDuzina,
		 * broj, postanskiBroj) imaju vrednost praznog stringa, automatski bivaju
		 * konvertovane u 0 ili 0.0, a ako imaju neku drugu tekstualnu vrednost,
		 * server vraæa status kod 400 */
		
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
		if (nazivRestorana == null || nazivRestorana.equals("") || nazivRestorana.trim().equals("") ||
			ulica == null || ulica.equals("") || ulica.trim().equals("") ||
			mesto == null || mesto.equals("") || mesto.trim().equals("") ||
			tipRestorana == null || tipRestorana.equals("") || tipRestorana.trim().equals("") ||
			menadzer == null || menadzer.equals("") || menadzer.trim().equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
		
		// 4. scenario: nevalidan unos tipa restorana
		try {
			TipRestorana.valueOf(tipRestorana);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID RESTAURANT TYPE").build();
		}
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		HashMap<String, Menadzer> menadzeri = korisnikDAO.getMenadzeriHashMap();
		
		// 5. scenario: menadzer sa takvim imenom ne postoji
		if (!menadzeri.containsKey(menadzer)) {
			return Response.status(Status.BAD_REQUEST).entity("MANAGER DOES NOT EXIST").build();
		}
		
		// 6. scenario: menadzer sa takvim imenom postoji, ali vec upravlja nekim restoranom
		Menadzer menadzerObject = menadzeri.get(menadzer);
		if (menadzerObject.getRestoran() != null) {
			return Response.status(Status.BAD_REQUEST).entity("MANAGER ALREADY TAKEN").build();
		}
		
		// 7. scenario: broj mora biti > 0
		if (broj <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID STREET NUMBER").build();
		}
		
		// 8. scenario: poštanski broj mora biti > 0
		if (postanskiBroj <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID POSTAL CODE").build();
		}
		
		// 9. scenario: logo == null
		if (logo == null) {
			return Response.status(Status.BAD_REQUEST).entity("LOGO IS NULL").build();
		}
		
		String logoPath = ctx.getRealPath("") + "images\\restaurant-logos\\" + nazivRestorana + ".jpg";
		// System.out.println(logo);
		
		// saving uploaded logo file to "images\restaurant-logos\"
		FileInputStream reader = null;
		FileOutputStream writer = null;
		File newLogoFile = null;
		try {
			newLogoFile = new File(logoPath);
			reader = new FileInputStream(logo);
			writer = new FileOutputStream(newLogoFile);
			
			int byteRead;
			while ((byteRead = reader.read()) != -1) {
				writer.write(byteRead);
			}
		} catch (FileNotFoundException e) {
			/* exception occurred while initializing FileInputStream or
			 * while initializing FileOutputStream */
			e.printStackTrace();
			
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("FILE NOT FOUND").build();
		} catch (IOException e) {
			/* exception occurred while reading or writing bytes */
			e.printStackTrace();
			
			File file = new File(logoPath);
			if (file.exists()) {
				file.delete();
			}
			
			try {
				reader.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("FILE CORRUPTED").build();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// kreirati objekat klase Restoran
		Adresa adresa = new Adresa(ulica, broj, mesto, postanskiBroj);
		Lokacija lokacija = new Lokacija(geoDuzina, geoSirina, adresa);
		Restoran restoran = new Restoran(nazivRestorana,
										 TipRestorana.valueOf(tipRestorana),
										 StatusRestorana.RADI,
										 lokacija,
										 newLogoFile,
										 menadzer);
		
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		restoranDAO.dodajRestoran(restoran);	// implementirati i serijalizaciju u okviru metode
		
		menadzerObject.setRestoran(nazivRestorana);
		korisnikDAO.sacuvajMenadzere(ctx.getRealPath(""));
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	public Response getAllRestaurants() {
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		
		List<Restoran> restorani = restoranDAO.getAllRestoraniList();
		
		return Response.status(Status.OK).entity(restorani).build();
	}
}

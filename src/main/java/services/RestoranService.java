package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.Adresa;
import beans.Artikal;
import beans.Korisnik;
import beans.Lokacija;
import beans.Menadzer;
import beans.Restoran;
import beans.StatusRestorana;
import beans.TipArtikla;
import beans.TipKorisnika;
import beans.TipRestorana;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dao.ZahtevZaDostavuDAO;
import dto.ArtikalDTO;
import dto.RestoranDTO;

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
		
		if (ctx.getAttribute("porudzbine") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("porudzbine", new PorudzbinaDAO(path));
		}
		
		if (ctx.getAttribute("zahtevi") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("zahtevi", new ZahtevZaDostavuDAO(path));
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
		// resiti se whitespace-ova
		nazivRestorana = nazivRestorana.trim();
		ulica = ulica.trim();
		mesto = mesto.trim();
		tipRestorana = tipRestorana.trim();
		menadzer = menadzer.trim();
		if (nazivRestorana == null || nazivRestorana.equals("") ||
			ulica == null || ulica.equals("") ||
			mesto == null || mesto.equals("") ||
			tipRestorana == null || tipRestorana.equals("") ||
			menadzer == null || menadzer.equals("")) {
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
		Restoran restoran = new Restoran(
			nazivRestorana,
			TipRestorana.valueOf(tipRestorana),
			StatusRestorana.RADI,
			lokacija,
			newLogoFile,
			menadzer
		);
		
		// dodavanje restorana i serijalizacija
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		restoranDAO.dodajRestoran(restoran);	// u okviru metode se poziva i metoda sacuvajRestorane
		
		// update menadzera kojem je upravo dodeljen restorana
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
	
	@GET
	@Path("/byManager/{manager}")
	public Response getRestaurantByItsManager(@PathParam("manager") String manager) {
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		// 1. scenario: menadzer sa zadatim korisnickim imenom ne postoji
		if (!korisnikDAO.getMenadzeriHashMap().containsKey(manager)) {
			return Response.status(Status.BAD_REQUEST).entity("MANAGER DOES NOT EXIST").build();
		}
		
		// 2. scenario: menadzer ne rukovodi ni jednim restoranom
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(manager);
		if (menadzer.getRestoran() == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT IS NULL").build();
		}
		
		// 3. scenario: vratiti restoran kojim menadzer rukovodi
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		Restoran restoran = restoranDAO.getRestaurantByItsName(menadzer.getRestoran());
		
		RestoranDTO dto = new RestoranDTO();
		dto.setNaziv(restoran.getNaziv());
		dto.setTip(restoran.getTip().toString());
		List<ArtikalDTO> artikli = new ArrayList<ArtikalDTO>();
		for (Artikal a : restoran.getArtikli()) {
			ArtikalDTO artikalDTO = new ArtikalDTO();
			artikalDTO.setNaziv(a.getNaziv());
			artikalDTO.setCena(a.getCena());
			artikalDTO.setTip(a.getTip().toString());
			artikalDTO.setRestoran(a.getRestoran());
			artikalDTO.setKolicina(a.getKolicina());
			artikalDTO.setOpis(a.getOpis());
			
			artikli.add(artikalDTO);
		}
		dto.setArtikli(artikli);
		dto.setStatus(restoran.getStatus().toString());
		dto.setLokacija(restoran.getLokacija());
		dto.setMenadzer(restoran.getMenadzer());
		
		return Response.status(Status.OK).entity(dto).build();
	}
	
	@POST
	@Path("/addNewArticle")
	public Response addNewArticle(@FormDataParam("nazivArtikla") String nazivArtikla,
								  @FormDataParam("tipArtikla") String tipArtikla,
								  @FormDataParam("slikaArtikla") File slikaArtikla,
								  @FormDataParam("opisArtikla") String opisArtikla,
								  @FormDataParam("kolicina") int kolicina,
								  @FormDataParam("cena") float cena,
								  @FormDataParam("nazivRestorana") String nazivRestorana) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: tipKorisnika mora biti MENADZER
		if (korisnik.getTipKorisnika() != TipKorisnika.MENADZER) {
			return Response.status(Status.FORBIDDEN).entity("NOT MANAGER").build();
		}
		
		// 3. scenario: tipKorisnika je MENADZER, ali to nije menadzer odgovarajuceg restorana
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());
		
		if (!menadzer.getRestoran().equals(nazivRestorana)) {
			return Response.status(Status.FORBIDDEN).entity("WRONG MANAGER").build();
		}
		
		// 4. scenario: nevalidan unos podataka
		// resiti se whitespace-ova
		nazivRestorana = nazivRestorana.trim();
		nazivArtikla = nazivArtikla.trim();
		tipArtikla = tipArtikla.trim();
		opisArtikla = opisArtikla.trim();
		
		if (nazivRestorana == null || nazivRestorana.equals("") ||
			nazivArtikla == null || nazivArtikla.equals("") ||
			tipArtikla == null || tipArtikla.equals("") ||
			opisArtikla == null	|| opisArtikla.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}
		
		// 5. scenario: ime artikla je vec zauzeto
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		Restoran restoran = restoranDAO.getRestaurantByItsName(nazivRestorana);
		if (restoran == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT DOES NOT EXIST").build();
		} else {
			if (restoran.sadrziArtikal(nazivArtikla)) {
				return Response.status(Status.BAD_REQUEST).entity("ARTICLE NAME TAKEN").build();
			}
		}
		
		// 6. scenario: nevalidan unos tipa artikla
		try {
			TipArtikla.valueOf(tipArtikla);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID ARTICLE TYPE").build();
		}
		
		// 7. scenario: kolicina mora biti > 0
		if (kolicina <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID QUANTITY NUMBER").build();
		}
		
		// 8. scenario: cena mora biti > 0
		if (cena <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID PRICE NUMBER").build();
		}
		
		// 9. scenario: slikaArtikla == null
		if (slikaArtikla == null) {
			return Response.status(Status.BAD_REQUEST).entity("IMAGE IS NULL").build();
		}
		
		// Snimanje slike artikla u "images\article-images\"
		String imagePath = ctx.getRealPath("") + "images\\article-images\\" + nazivRestorana + "-" + nazivArtikla + ".jpg";
		// System.out.println(imagePath);
		
		FileInputStream reader = null;
		FileOutputStream writer = null;
		File newImageFile = null;
		try {
			newImageFile = new File(imagePath);
			reader = new FileInputStream(slikaArtikla);
			writer = new FileOutputStream(newImageFile);
			
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
			
			File file = new File(imagePath);
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
		
		// kreirati objekat klase Artikal
		Artikal artikal = new Artikal(
			nazivArtikla,
			cena,
			TipArtikla.valueOf(tipArtikla),
			nazivRestorana,
			kolicina,
			opisArtikla,
			newImageFile
		);
		
		// dodavanje artikla i serijalizacija
		restoranDAO.dodajArtikal(artikal);	// u okviru metode se poziva i metoda sacuvajRestorane
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/{restaurantName}")
	public Response getRestaurantByItsName(@PathParam("restaurantName") String restaurantName) {
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		
		Restoran restoran = restoranDAO.getRestaurantByItsName(restaurantName);
		
		if (restoran == null) {
			return Response.status(Status.BAD_REQUEST).entity("DOES NOT EXIST").build();
		}
		
		return Response.status(Status.OK).entity(restoran).build();
	}
	
	@PUT
	@Path("/editArticle")
	public Response editArticle(@FormDataParam("stariNazivArtikla") String stariNazivArtikla,
								@FormDataParam("noviNazivArtikla") String noviNazivArtikla,
			  					@FormDataParam("tipArtikla") String tipArtikla,
			  					@FormDataParam("slikaArtikla") File slikaArtikla,
			  					@FormDataParam("opisArtikla") String opisArtikla,
			  					@FormDataParam("kolicina") int kolicina,
			  					@FormDataParam("cena") float cena,
			  					@FormDataParam("nazivRestorana") String nazivRestorana,
			  					@FormDataParam("slikaMenjana") boolean slikaMenjana) {
		// validacija
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
		
		// 1. scenario: korisnik nije ulogovan
		if (korisnik == null) {
			return Response.status(Status.BAD_REQUEST).entity("NOT LOGGED IN").build();
		}
		
		// 2. scenario: tipKorisnika mora biti MENADZER
		if (korisnik.getTipKorisnika() != TipKorisnika.MENADZER) {
			return Response.status(Status.FORBIDDEN).entity("NOT MANAGER").build();
		}
		
		// 3. scenario: tipKorisnika je MENADZER, ali to nije menadzer odgovarajuceg restorana
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		Menadzer menadzer = korisnikDAO.getMenadzeriHashMap().get(korisnik.getKorisnickoIme());

		if (!menadzer.getRestoran().equals(nazivRestorana)) {
			return Response.status(Status.FORBIDDEN).entity("WRONG MANAGER").build();
		}
		
		// 4. scenario: nevalidan unos podataka
		// resiti se whitespace-ova
		nazivRestorana = nazivRestorana.trim();
		stariNazivArtikla = stariNazivArtikla.trim();
		noviNazivArtikla = noviNazivArtikla.trim();
		tipArtikla = tipArtikla.trim();
		opisArtikla = opisArtikla.trim();

		if (nazivRestorana == null || nazivRestorana.equals("") ||
			noviNazivArtikla == null || noviNazivArtikla.equals("") ||
			tipArtikla == null || tipArtikla.equals("") ||
			opisArtikla == null	|| opisArtikla.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("EMPTY FIELDS").build();
		}

		// 5. scenario: ime artikla je vec zauzeto
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		Restoran restoran = restoranDAO.getRestaurantByItsName(nazivRestorana);
		if (restoran == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT DOES NOT EXIST").build();
		} else {
			if (restoran.sadrziArtikal(noviNazivArtikla)) {
				return Response.status(Status.BAD_REQUEST).entity("ARTICLE NAME TAKEN").build();
			}
		}
		
		// 6. scenario: nevalidan unos tipa artikla
		try {
			TipArtikla.valueOf(tipArtikla);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("INVALID ARTICLE TYPE").build();
		}

		// 7. scenario: kolicina mora biti > 0
		if (kolicina <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID QUANTITY NUMBER").build();
		}

		// 8. scenario: cena mora biti > 0
		if (cena <= 0) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID PRICE NUMBER").build();
		}

		// 9. scenario: slikaArtikla == null
		if (slikaArtikla == null) {
			return Response.status(Status.BAD_REQUEST).entity("IMAGE IS NULL").build();
		}
		
		// Snimanje slike artikla u "images\article-images\"
		File newImageFile = null;
		
		if (!slikaMenjana) {
			if (!stariNazivArtikla.equals(noviNazivArtikla)) {
				// promeni naziv fajla samo
				String imagePath = ctx.getRealPath("") + "images\\article-images\\" + nazivRestorana + "-" + stariNazivArtikla + ".jpg";
				newImageFile = new File(imagePath);
				
				if (newImageFile.exists()) {
					String renamedFilePath = ctx.getRealPath("") + "images\\article-images\\" + nazivRestorana + "-" + noviNazivArtikla + ".jpg";
					File renamedFile = new File(renamedFilePath);
					
					if (!renamedFile.exists()) {
						try {
							newImageFile.renameTo(renamedFile);
						} catch(Exception e) {
							e.printStackTrace();
							return Response.status(Status.BAD_REQUEST).entity("FILE ERROR").build();
						}
					} else {
						return Response.status(Status.BAD_REQUEST).entity("FILE ERROR").build();
					}
				} else {
					return Response.status(Status.BAD_REQUEST).entity("FILE ERROR").build();
				}
			}
		} else {
			/* 1) ako se stari naziv artikla razlikuje od novog naziva artikla,
			 * prvo je potrebno obrisati sliku koja je snimljena pod starim nazivom, pa onda
			 * snimiti novu sliku pod novim nazivom
			 * 2) ako naziv artikla nije menjan, onda nema potrebe brisati staru sliku, nego
			 * je dovoljno samo snimiti novu sliku pod istim nazivom, jer æe tako nova slika
			 * biti presnimljena preko stare */
			
			if (!stariNazivArtikla.equals(noviNazivArtikla)) {
				// obriši fajl sa starim nazivom artikla, a snimi fajl sa novim nazivom artikla
				String stariNazivImagePath = ctx.getRealPath("") + "images\\article-images\\" + nazivRestorana + "-" + stariNazivArtikla + ".jpg";
				System.out.println(stariNazivImagePath);
				File file = new File(stariNazivImagePath);
				
				if (file.exists()) {
					try {
						file.delete();
					} catch (Exception e) {
						e.printStackTrace();
						return Response.status(Status.BAD_REQUEST).entity("FILE ERROR").build();
					}
				} else {
					return Response.status(Status.BAD_REQUEST).entity("FILE ERROR").build();
				}
			}
			
			String imagePath = ctx.getRealPath("") + "images\\article-images\\" + nazivRestorana + "-" + noviNazivArtikla + ".jpg";
			System.out.println(imagePath);
			
			FileInputStream reader = null;
			FileOutputStream writer = null;
			try {
				newImageFile = new File(imagePath);
				reader = new FileInputStream(slikaArtikla);
				writer = new FileOutputStream(newImageFile);

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

				File file = new File(imagePath);
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
		}
		
		// kreirati objekat klase Artikal
		Artikal artikal = new Artikal(
			noviNazivArtikla,
			cena,
			TipArtikla.valueOf(tipArtikla),
			nazivRestorana,
			kolicina,
			opisArtikla,
			newImageFile
		);

		// dodavanje artikla i serijalizacija
		restoranDAO.izmeniArtikal(stariNazivArtikla, artikal);	// u okviru metode se poziva i metoda sacuvajRestorane
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/search")
	public Response searchRestaurants(@DefaultValue("") @QueryParam("name") String name,
									  @DefaultValue("") @QueryParam("address") String address,
									  @DefaultValue("") @QueryParam("type") String type) {
		// Search by: name, address, type
		
		/*
		System.out.println("Name: " + name);
		System.out.println("Address: " + address);
		System.out.println("Type: " + type);
		*/
		
		name = name.trim();
		address = address.trim();
		type = type.trim();
		
		if (!type.equals("")) {
			try {
				TipRestorana.valueOf(type);
			} catch (IllegalArgumentException e) {
				// e.printStackTrace();
				return Response.status(Status.BAD_REQUEST).entity("INVALID RESTAURANT TYPE").build();
			}
		}
		
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		List<Restoran> restorani = restoranDAO.getAllRestoraniList();
		
		if (!name.equals("")) {
			Iterator<Restoran> i = restorani.iterator();
			
			while (i.hasNext()) {
				Restoran r = i.next();
				
				String transformedName = RestoranService.transformToNonDiacritical(r.getNaziv());
				if (!transformedName.toLowerCase().startsWith(name.toLowerCase())) {
					i.remove();
				}
			}
		}
		
		if (!address.equals("")) {
			Iterator<Restoran> i = restorani.iterator();
			
			while (i.hasNext()) {
				Restoran r = i.next();
				
				String fullAddress = r.getLokacija().getAdresa().getUlica() + " " + r.getLokacija().getAdresa().getBroj();
				String transformedAddress = RestoranService.transformToNonDiacritical(fullAddress);
				if (!transformedAddress.toLowerCase().startsWith(address.toLowerCase())) {
					i.remove();
				}
			}
		}
		
		if (!type.equals("")) {
			Iterator<Restoran> i = restorani.iterator();
			
			while (i.hasNext()) {
				Restoran r = i.next();
				
				if (!r.getTip().equals(TipRestorana.valueOf(type))) {
					i.remove();
				}
			}
		}
		
		return Response.status(Status.OK).entity(restorani).build();
	}
	
	public static String transformToNonDiacritical(String diacritical) {
		String nonDiacritical = diacritical;
		
		nonDiacritical = nonDiacritical.replace('š', 's');
		nonDiacritical = nonDiacritical.replace('ž', 'z');
		nonDiacritical = nonDiacritical.replace("ð", "dj");
		nonDiacritical = nonDiacritical.replace('è', 'c');
		nonDiacritical = nonDiacritical.replace('æ', 'c');
		
		nonDiacritical = nonDiacritical.replace('Š', 's');
		nonDiacritical = nonDiacritical.replace('Ž', 'z');
		nonDiacritical = nonDiacritical.replace("Ð", "dj");
		nonDiacritical = nonDiacritical.replace('È', 'c');
		nonDiacritical = nonDiacritical.replace('Æ', 'c');
		
		return nonDiacritical;
	}

}

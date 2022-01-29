package services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Administrator;
import beans.ArtikalSaKolicinom;
import beans.Dostavljac;
import beans.Korisnik;
import beans.Korpa;
import beans.Kupac;
import beans.Menadzer;
import enums.Pol;
import beans.Porudzbina;
import beans.Restoran;
import beans.StaticMethods;
import enums.TipKorisnika;
import beans.TipKupca;
import enums.TipRestorana;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dao.ZahtevZaDostavuDAO;

@Path("/test")
public class TestService {
	
	@Context
	ServletContext ctx;
	
	public TestService() {}
	
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
	
	@GET
	@Path("/1")
	public Response test() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			Date date = dateFormat.parse("09-06-1997");
			String dateString = dateFormat.format(date);
			return Response.status(Status.OK).entity(dateString).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nothing").build();
	}
	
	@GET
	@Path("/2")
	public Response test2() {
		String path = ctx.getRealPath("") + "data\\korisnici.txt";
		return Response.status(Status.OK).entity(path).build();
	}
	
	@GET
	@Path("/3")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test3() {
		/* primer kako radi readValue funkcija - tj. u neku ruku deserijalizacija */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		List<Korisnik> listKorisnici = new ArrayList<Korisnik>();
		
		String jsonArray = "[{ \"korisnickoIme\" : \"proba\", \"lozinka\" : \"proba\", \"ime\" : \"proba\", \"prezime\" : \"proba\", \"pol\" : \"MUSKO\", \"datumRodjenja\" : \"09-06-1997\", \"tipKorisnika\" : \"KUPAC\" },"
							+ "	{ \"korisnickoIme\" : \"admin\", \"lozinka\" : \"admin\", \"ime\" : \"admin\", \"prezime\" : \"admin\", \"pol\" : \"MUSKO\", \"datumRodjenja\" : \"09-06-1997\", \"tipKorisnika\" : \"KUPAC\" }]";
		try {
			listKorisnici = objectMapper.readValue(jsonArray, new TypeReference<List<Korisnik>>(){});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Korisnik k : listKorisnici) {
			System.out.println("Korisnik: " + k.getKorisnickoIme());
		}
		
		return Response.status(Status.OK).entity(listKorisnici).build();
	}
	
	@GET
	@Path("/4")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test4() {
		/* primer kako radi writeValue funkcija - tj. u neku ruku serijalizacija */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		Korisnik k = new Korisnik("proba", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC, false);
		Korisnik k2 = new Korisnik("admin", "admin", "admin", "admin", Pol.MUSKO, new Date(), TipKorisnika.ADMINISTRATOR, false);
		List<Korisnik> korisnici = new ArrayList<Korisnik>();
		korisnici.add(k); korisnici.add(k2);
		
		try {
			objectMapper.writeValue(new File("C:\\Users\\Miloš\\Desktop\\korisnik.json"), korisnici);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/5")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test5() {
		/* primer kako radi writeValue funkcija na malo kompleksnijem primeru - tj. u neku ruku serijalizacija */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		TipKupca tipKupca = new TipKupca("zlatni", 20, 67);
		List<Porudzbina> porudzbine = new ArrayList<Porudzbina>();
		Korpa korpa = new Korpa();
		korpa.setArtikli(new ArrayList<ArtikalSaKolicinom>());
		korpa.setKupac(null);
		korpa.setCena(0);
		
		Kupac kupac = new Kupac("proba", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC, false, tipKupca, porudzbine, korpa, 0);
		
		try {
			objectMapper.writeValue(new File("C:\\Users\\Miloš\\Desktop\\korisnik.json"), kupac);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/6")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test6() {
		/* primer ucitavanja (deserijalizacije)
		 * napomena: korisnik.json treba da postoji na desktopu i da sadrzi ispravno upisane json objekte */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		
		ArrayList<Kupac> kupci = new ArrayList<Kupac>();
		
		try {
			kupci = objectMapper.readValue(new File("C:\\Users\\Miloš\\Desktop\\korisnik.json"), new TypeReference<ArrayList<Kupac>>(){});
			
			for (Kupac k : kupci) {
				System.out.println("Kupac: " + k.getKorisnickoIme());
			}
			
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/7")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test7() {
		/* primer writerWithDefaultPrettyPrinter */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		TipKupca tipKupca = new TipKupca("zlatni", 20, 67);
		List<Porudzbina> porudzbine = new ArrayList<Porudzbina>();
		Korpa korpa = new Korpa();
		korpa.setArtikli(new ArrayList<ArtikalSaKolicinom>());
		korpa.setKupac(null);
		korpa.setCena(0);
		
		Kupac kupac = new Kupac("proba", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC, false, tipKupca, porudzbine, korpa, 0);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Miloš\\Desktop\\test.json"), kupac);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
		
		/* rezultat ove metode bio je Response proslo i kreiran fajl na desktopu test2.json sa sadrzajem: 
		 * 		{
				  	"korisnickoIme" : "proba",
					"lozinka" : "proba",
			  		"ime" : "proba",
				  	"prezime" : "proba",
				  	"pol" : "MUSKO",
				  	"datumRodjenja" : "21-07-2021",
				  	"tipKorisnika" : "KUPAC",
				  	"tipKupca" : {
				    	"ime" : "zlatni",
					   	"popust" : 20,
					    "brojBodova" : 67
					},
					"svePorudzbine" : [ ],
				  	"korpa" : {
				    	"artikli" : [ ],
					    "korisnik" : null,
					    "cena" : 0
					},
				 	"sakupljeniBodovi" : 0
				}
		*/
	}
	
	@GET
	@Path("/8")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test8() {
		/* test serijalizacije objekata koji sadrže null vrednosti */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		Kupac kupac = new Kupac();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Miloš\\Desktop\\test2.json"), kupac);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
		
		/* rezultat ove metode bio je Response proslo i kreiran fajl na desktopu test2.json sa sadrzajem: 
		 * 		{
				  	"korisnickoIme" : null,
				  	"lozinka" : null,
				  	"ime" : null,
				  	"prezime" : null,
				  	"pol" : null,
				  	"datumRodjenja" : null,
				  	"tipKorisnika" : null,
				  	"tipKupca" : null,
				  	"svePorudzbine" : [ ],
				  	"korpa" : {
				    	"artikli" : [ ],
				    	"kupac" : null,
				    	"cena" : 0
				  	},
				  	"sakupljeniBodovi" : 0
				}
		*/
	}
	
	@GET
	@Path("/9")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test9() {
		/* test kako radi serijalizacija vrednosti iz hashmap-e */
		
		Korisnik korisnik1 = new Korisnik("korisnik1", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC, false);
		//Korisnik korisnik2 = new Korisnik("korisnik2", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC);
		//Korisnik korisnik3 = new Korisnik("korisnik3", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC);
		
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		
		korisnici.put("korisnik1", korisnik1);
		//korisnici.put("korisnik2", korisnik2);
		//korisnici.put("korisnik3", korisnik3);
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Miloš\\Desktop\\serijalizacija_mape.json"), korisnici);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
		
		/* rezultat metode je Response proslo i kreiran fajl na desktopu serijalizacija_mape.json sa sadrzajem:
		 * 	{
			  "korisnik1" : {
			    "korisnickoIme" : "korisnik1",
			    "lozinka" : "proba",
			    "ime" : "proba",
			    "prezime" : "proba",
			    "pol" : "MUSKO",
			    "datumRodjenja" : "21-07-2021",
			    "tipKorisnika" : "KUPAC"
			  },
			  "korisnik2" : {
			    "korisnickoIme" : "korisnik2",
			    "lozinka" : "proba",
			    "ime" : "proba",
			    "prezime" : "proba",
			    "pol" : "MUSKO",
			    "datumRodjenja" : "21-07-2021",
			    "tipKorisnika" : "KUPAC"
			  },
			  "korisnik3" : {
			    "korisnickoIme" : "korisnik3",
			    "lozinka" : "proba",
			    "ime" : "proba",
			    "prezime" : "proba",
			    "pol" : "MUSKO",
			    "datumRodjenja" : "21-07-2021",
			    "tipKorisnika" : "KUPAC"
			  }
			} 
		 */
	}
	
	@GET
	@Path("/10")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test10() {
		/* test kako radi deserijalizacija hasmap-e */
		/* pre ove metode pokrenuti test9() da bi se kreirao fajl serijalizacije_mape.json */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		//objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		
		HashMap<String, Korisnik> korisnici;
		
		try {
			korisnici = objectMapper.readValue(new File("C:\\Users\\Miloš\\Desktop\\serijalizacija_mape.json"), new TypeReference<HashMap<String, Korisnik>>(){});
			
			for (Korisnik k : korisnici.values()) {
				System.out.println("Korisnik: " + k.getKorisnickoIme() + " " + k.getIme() + " " + k.getPrezime());
			}
			
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}

	@GET
	@Path("/11")
	public Response test11() {
		/* test kako radi ucitavanje iz fajla koji ne postoji */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		
		try {
			korisnici = objectMapper.readValue(new File("C:\\Users\\Miloš\\Desktop\\aaaaaaa.json"), new TypeReference<HashMap<String, Korisnik>>(){});
			
			System.out.println("Broj objekata u mapi: " + korisnici.size());
			
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File: \"C:\\Users\\Miloš\\Desktop\\aaaaaaa.json\" couldn't be open. It probably doesn't exist.");
		} finally {
			System.out.println("Broj objekata u mapi: " + korisnici.size());
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/12")
	public Response test12() {
		/* test kako radi ucitavanje u mapu iz fajla koji postoji, ali je prazan
		 * uputstvo: kreirati prazan fajl a.json */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		
		try {
			korisnici = objectMapper.readValue(new File("C:\\Users\\Miloš\\Desktop\\a.json"), new TypeReference<HashMap<String, Korisnik>>(){});
			
			System.out.println("Broj objekata u mapi: " + korisnici.size());
			
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File: \"C:\\Users\\Miloš\\Desktop\\a.json\" couldn't be open. It probably doesn't exist.");
		} finally {
			System.out.println("Broj objekata u mapi: " + korisnici.size());
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/13")
	public Response test13() {
		/* test kako radi cuvanje prazne mape */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Miloš\\Desktop\\bzvz.json"), korisnici);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
		
		/* rezultat je kreiran fajl na desktopu koji ima sadrzaj: { } */
	}
	
	@GET
	@Path("/14")
	public Response test14() {
		/* primer kako radi ucitavanje prazne mape
		 * napomena: pokrenuti prvo test13 kako bi se kreirao fajl bzvz.json iz kojeg treba da se ucita prazna mapa */
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		
		try {
			korisnici = objectMapper.readValue(new File("C:\\Users\\Miloš\\Desktop\\bzvz.json"), new TypeReference<HashMap<String, Korisnik>>(){});
			
			System.out.println("Broj objekata u mapi: " + korisnici.size());
			
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
	}
	
	@GET
	@Path("/15")
	public Response test15() {
		/* test kastovanja npr. Kupca u Korisnik */
		
		Korisnik korisnik1 = new Korisnik("proba", "proba", "proba", "proba", Pol.MUSKO, new Date(), TipKorisnika.KUPAC, false);
		
		Kupac kupac = new Kupac(korisnik1);
		
		Korisnik korisnik2 = (Korisnik) kupac;
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Miloš\\Desktop\\proba.json"), korisnik2);
			return Response.status(Status.OK).entity("proslo").build();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(Status.OK).entity("nije proslo").build();
		
		/* hteo sam da vidim kada dodajem novog kupca, jer treba odmah da snimim tog kupca kao kupca, ali takodje i
		 * kao korisnika, da li mogu samo da odradim cast (Korisnik) kupac i da to snimim u korisnici.json, ali
		 * ne mogu, jer snimanjem kastovanog kupca, snimamo sva njegova polja, ne samo ona koja su karakteristicna
		 * za Korisnik klasu, sto mi ne odgovara ... */
	}
	
	@POST
	@Path("/init")
	public Response inicijalnoKreiranjeKorisnika() {
		/* metoda kojom se dodaju pocetni korisnici tj. dodaje se 4 korisnika (1 kupac, 1 admin, 1 menadzer, 1 dostavljac)
		 * 
		 * ti korisnici se dodaju metodama iz klase KorisnikDAO: dodajKupca, dodajAdministratora, dodajMenadzera, 
		 * dodajDostavljaca
		 * 
		 * u okviru tih metoda se vrsi serijalizacija, tako da ce biti kreirani fajlovi korisnici.json, kupci.json,
		 * administratori.json, menadzeri.json, dostavljaci.json */
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse("1997-06-09");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.OK).entity("DATE PARSE EXCEPTION").build();
		}
				
		Korisnik korisnik1 = new Korisnik("proba", "proba", "proba", "proba", Pol.MUSKO, date, TipKorisnika.KUPAC, false);
		Korisnik korisnik2 = new Korisnik("admin", "admin", "admin", "admin", Pol.MUSKO, date, TipKorisnika.ADMINISTRATOR, false);
		Korisnik korisnik3 = new Korisnik("dostavljac", "dostavljac", "dostavljac", "dostavljac", Pol.MUSKO, date, TipKorisnika.DOSTAVLJAC, false);
		Korisnik korisnik4 = new Korisnik("menadzerka", "menadzerka", "menadzerka", "menadzerka", Pol.ZENSKO, date, TipKorisnika.MENADZER, false);
		
		Kupac kupac = new Kupac(korisnik1);
		Administrator admin = new Administrator(korisnik2);
		Dostavljac dostavljac = new Dostavljac(korisnik3);
		Menadzer menadzerka = new Menadzer(korisnik4);
		
		KorisnikDAO korisnikDAO = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		/*
		HashMap<String, Korisnik> korisnici = korisnikDAO.getKorisniciHashMap();
		HashMap<String, Kupac> kupci = korisnikDAO.getKupciHashMap();
		HashMap<String, Administrator> administratori = korisnikDAO.getAdministratoriHashMap();
		HashMap<String, Menadzer> menadzeri = korisnikDAO.getMenadzeriHashMap();
		HashMap<String, Dostavljac> dostavljaci = korisnikDAO.getDostavljaciHashMap();
		
		korisnici.put(korisnik1.getKorisnickoIme(), korisnik1);
		korisnici.put(korisnik2.getKorisnickoIme(), korisnik2);
		korisnici.put(korisnik3.getKorisnickoIme(), korisnik3);
		korisnici.put(korisnik4.getKorisnickoIme(), korisnik4);
		
		kupci.put(kupac.getKorisnickoIme(), kupac);
		administratori.put(admin.getKorisnickoIme(), admin);
		dostavljaci.put(dostavljac.getKorisnickoIme(), dostavljac);
		menadzeri.put(menadzerka.getKorisnickoIme(), menadzerka);
		*/
		
		korisnikDAO.dodajKupca(kupac);
		korisnikDAO.dodajAdministratora(admin);
		korisnikDAO.dodajDostavljaca(dostavljac);
		korisnikDAO.dodajMenadzera(menadzerka);
		
		return Response.status(Status.OK).entity("OK").build();
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/16")
	public Response testCreateNewRestaurant(@FormDataParam("nazivRestorana") String nazivRestorana,
											@FormDataParam("tipRestorana") String tipRestorana,
											@FormDataParam("geoSirina") Float geoSirina,
											@FormDataParam("geoDuzina") Float geoDuzina,
											@FormDataParam("ulica") String ulica,
											@FormDataParam("broj") Integer broj,
											@FormDataParam("mesto") String mesto,
											@FormDataParam("postanskiBroj") Integer postanskiBroj,
											@FormDataParam("menadzer") String menadzer,
											@FormDataParam("logo") File logo) {
		// ispis parametara
		/* Inicijalno su geoSirina i geoDuzina bile float, a ne Float, a broj
		 * i postanskiBroj integer, a ne Integer. Fora je sto kod float i kod integer,
		 * ako budu prosledjeni prazni stringovi, Java to tumaci kao default value 0 ili 0.0, a
		 * kod Float i Integer, ako dobijemo prazne stringove, Java to tumaci kao null.
		 * Ovaj slucaj sa Float i Integer vise odgovara, jer ako korisnik ne prosledi
		 * koordinate, Java ce ih tumaciti kao koordinate (0, 0).
		 */
		
		System.out.println("Naziv restorana: " + nazivRestorana);
		System.out.println("Tip restorana: " + tipRestorana);
		System.out.println("Geografska sirina: " + geoSirina);
		System.out.println("Geografska duzina: " + geoDuzina);
		System.out.println("Ulica: " + ulica);
		System.out.println("Broj: " + broj);
		System.out.println("Mesto: " + mesto);
		System.out.println("Postanski broj: " + postanskiBroj);
		System.out.println("Menadzer: " + menadzer);
		System.out.println("Logo: " + logo);
		
		if (logo == null) {
			System.out.println("Logo is null!");
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/17")
	public Response test17() {
		if (TipRestorana.valueOf("ITALIJANSKI") == TipRestorana.ITALIJANSKI) {
			System.out.println("Scenario 1: OK!");
		}
		
		String gibberish = "asfasfa";
		
		/*
		int i = 0;
		for (TipRestorana t : TipRestorana.values()) {
			if (TipRestorana.valueOf(gibberish) == t) {
				System.out.println("This line is never going to be executed.");
			}
			
			System.out.println("Scenario 2: " + i); ++i;
		}
		// baca IllegalArgumentException
		*/
		
		try {
			TipRestorana.valueOf(gibberish);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Exception catched!");
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/18")
	public Response test18() {
		String emptyString = "";
		String spaceString = "     ";
		String normalString = "     whatever   ";
		
		if ((emptyString = emptyString.trim()).equals("")) {
			System.out.println("Trimmed empty string is still an empty string.");
		}
		
		if ((spaceString = spaceString.trim()).equals("")) {
			System.out.println("spaceString trimmed is an empty string.");
		}
		
		normalString = normalString.trim();
		System.out.println("normalString after a trim: " + normalString);
		
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/19")
	public Response test19(Korisnik korisnik) {
		/* testiram ako se prosledi null kao vrednost za neko polje klase
		 * Korisnik koje je tipa String, da li Java taj null konvertuje
		 * u "" automatski ili taj null ostaje null */
		
		System.out.println("Ime: " + korisnik.getIme());
		System.out.println("Prezime: " + korisnik.getPrezime());
		System.out.println("Korisnicko ime: " + korisnik.getKorisnickoIme());
		
		if (korisnik.getIme() == null) {
			System.out.println("Ime je null!");
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/20")
	public Response test20() {
		/* testiranje metode File.exists() */
		
		String naziv_fajla = "fajl_koji_ne_postoji.jpg";
		String imagePath = System.getProperty("user.home") + "\\Desktop\\" + naziv_fajla;
		System.out.println(imagePath);
		
		File file = new File(imagePath);
		
		if (!file.exists()) {
			System.out.println("Fajl" + imagePath + " ne postoji!");
		} else {
			System.out.println("Fajl" + imagePath + " postoji!");
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/21")
	public Response test21() {
		// testiranje napisane funkcije za generisanja identifikatora
		
		String id = StaticMethods.generateId();
		
		return Response.status(Status.OK).entity(id).build();
	}
	
	@GET
	@Path("/22")
	public Response test22() {
		// testiranje sta vraca Date.now()
		return Response.status(Status.OK).entity(new Date()).build();
	}
	
	@GET
	@Path("/23")
	public Response test23() {
		float a = 56.7f;
		
		System.out.println("Kastovano a: " + (int) a);
		System.out.println("Rezultat: " + (int) a / 5);
		/* kastovano u int 56.7 postaje 56 -> 56 / 5 = 11.2, a kako je deljenje int sa intom
		 * treba da ispise 11 */
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/24")
	public Response test24() {
		// testing removing objects from map through iteration
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "aaa");
		map.put("b", "bbb");
		map.put("c", "ccc");
		map.put("d", "aaa");
		map.put("e", "eee");
		map.put("f", "aaa");
		
		for (Entry<String, String> s : map.entrySet()) {
			if (s.getValue().equals("aaa")) {
				map.remove(s.getKey());
			}
		}
		
		for (Entry<String, String> s : map.entrySet()) {
			System.out.println(s.getKey() + ": " + s.getValue());
		}
		
		return Response.status(Status.OK).build();
		
		// baca exception
	}
	
	@GET
	@Path("/25")
	public Response test25() {
		// testing removing objects from map with the help of Iterator
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "aaa");
		map.put("b", "bbb");
		map.put("c", "ccc");
		map.put("d", "aaa");
		map.put("e", "eee");
		map.put("f", "aaa");
		
		Iterator<String> i = map.values().iterator();
		
		while (i.hasNext()) {
			String s = i.next();
			
			if (s.equals("aaa")) {
				i.remove();
			}
		}
		
		for (Entry<String, String> s : map.entrySet()) {
			System.out.println(s.getKey() + ": " + s.getValue());
		}
		
		return Response.status(Status.OK).build();
		
		// works properly
	}
	
	@GET
	@Path("/26")
	public Response test26() {
		// testing how RestoranService.transformToNonDiacritical function works
		
		String string = "šaèaæaðaž";
		String string2 = StaticMethods.transformToNonDiacritical(string);
		
		System.out.println("string: " + string);
		System.out.println("string2: " + string2);
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/27")
	public Response test27() {
		Korisnik korisnik = new Korisnik(
			"asfasf",
			"asdasf",
			"safasf",
			"asfasf",
			Pol.MUSKO,
			new Date(),
			TipKorisnika.KUPAC,
			false
		);
		
		Kupac kupac =  new Kupac(korisnik);
		
		return Response.status(Status.OK).entity((Korisnik) kupac).build();
	}
	
	@GET
	@Path("/28")
	public Response test28() {
		String s1 = "abc";
		String s2 = "abd";
		
		if (s1.compareTo(s2) > 0) {
			System.out.println(">0");
		} else {
			System.out.println("<=0");
		}
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Path("/29")
	public Response test29() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:8080/web-2020-21/rest/restaurant"))
				.GET()
				.build();
			
			HttpResponse<String> response = HttpClient.newBuilder()
					.build()
					.send(request, BodyHandlers.ofString());
			
			ObjectMapper objectMapper = new ObjectMapper();
			List<Restoran> restorani = objectMapper.readValue(response.body(), new TypeReference<List<Restoran>>(){});
			
			return Response.status(Status.OK).entity(restorani).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
}

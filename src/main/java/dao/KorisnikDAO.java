package dao;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Administrator;
import beans.Dostavljac;
import beans.Korisnik;
import beans.Kupac;
import beans.Menadzer;
import beans.Pol;
import beans.TipKorisnika;

public class KorisnikDAO {
	private HashMap<String, Korisnik> korisnici;	// key = korisnicko ime; value = objekat (korisnik)
	private HashMap<String, Kupac> kupci;
	private HashMap<String, Administrator> administratori;
	private HashMap<String, Menadzer> menadzeri;
	private HashMap<String, Dostavljac> dostavljaci;
	private String contextPath;
	
	/* contextPath je parametar koji prosledjujemo u servletu tj. u service klasi i predstavlja
	 * putanju do aplikacije u tomcat-u
	 */
	public KorisnikDAO(String contextPath) {
		this.korisnici = new HashMap<String, Korisnik>();
		this.kupci = new HashMap<String, Kupac>();
		this.administratori = new HashMap<String, Administrator>();
		this.menadzeri = new HashMap<String, Menadzer>();
		this.dostavljaci = new HashMap<String, Dostavljac>();
		
		this.contextPath = contextPath;
		
		ucitajKorisnike(contextPath);
		ucitajAdministratore(contextPath);
		ucitajKupce(contextPath);
		ucitajMenadzere(contextPath);
		ucitajDostavljace(contextPath);
		
		// dodavanje korisnika kako bih mogao da istestiram login/logout
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("06-09-1997");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Korisnik korisnik1 = new Korisnik("proba", "proba", "proba", "proba", Pol.MUSKO, date, TipKorisnika.KUPAC, false);
		Korisnik korisnik2 = new Korisnik("admin", "admin", "admin", "admin", Pol.MUSKO, date, TipKorisnika.ADMINISTRATOR, false);
		Korisnik korisnik3 = new Korisnik("dostavljac", "dostavljac", "dostavljac", "dostavljac", Pol.MUSKO, date, TipKorisnika.DOSTAVLJAC, false);
		Korisnik korisnik4 = new Korisnik("menadzerka", "menadzerka", "menadzerka", "menadzerka", Pol.ZENSKO, date, TipKorisnika.MENADZER, false);
		
		Kupac kupac = new Kupac(korisnik1);
		Administrator admin = new Administrator(korisnik2);
		Dostavljac dostavljac = new Dostavljac(korisnik3);
		Menadzer menadzerka = new Menadzer(korisnik4);
		
		korisnici.put(korisnik1.getKorisnickoIme(), korisnik1);
		korisnici.put(korisnik2.getKorisnickoIme(), korisnik2);
		korisnici.put(korisnik3.getKorisnickoIme(), korisnik3);
		korisnici.put(korisnik4.getKorisnickoIme(), korisnik4);
		
		kupci.put(kupac.getKorisnickoIme(), kupac);
		administratori.put(admin.getKorisnickoIme(), admin);
		dostavljaci.put(dostavljac.getKorisnickoIme(), dostavljac);
		menadzeri.put(menadzerka.getKorisnickoIme(), menadzerka);
	}
	
	/* ucitava korisnike iz web-2020-21/data/korisnici.json fajla i dodaje ih u hashmap-u
	 * kljuc hashmap-e je username korisnika
	 * datum se cuva u obliku "MM-dd-yyyy" (jer je html standard "MM-dd-yyyy" u <input type="date">, pa da ne komplikujem za milisekundama ...)
	 * primer jednog json objekta u korisnici.json fajlu:
	 * 	{
	  		"korisnickoIme" : "proba",
  			"lozinka" : "proba",
  			"ime" : "proba",
  			"prezime" : "proba",
  			"pol" : "MUSKO",
  			"datumRodjenja" : "21-07-2021",
  			"tipKorisnika" : "KUPAC"	
	 * 	}
	 */
	private void ucitajKorisnike(String contextPath) {
		String path = contextPath + "data\\korisnici.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			this.korisnici = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Korisnik>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KorisnikDAO[method ucitajKorisnike]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	private void ucitajKupce(String contextPath) {
		String path = contextPath + "data\\kupci.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			this.kupci = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Kupac>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KorisnikDAO[method ucitajKupce]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	private void ucitajAdministratore(String contextPath) {
		String path = contextPath + "data\\administratori.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			this.administratori = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Administrator>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KorisnikDAO[method ucitajAdministratore]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	private void ucitajDostavljace(String contextPath) {
		String path = contextPath + "data\\dostavljaci.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			this.dostavljaci = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Dostavljac>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KorisnikDAO[method ucitajDostavljace]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	private void ucitajMenadzere(String contextPath) {
		String path = contextPath + "data\\menadzeri.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			this.menadzeri = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Menadzer>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KorisnikDAO[method ucitajMenadzere]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	/* ove "sacuvaj" metode bi mozda trebale biti private, s obzirom da se koriste samo u okviru ove klase */
	public void sacuvajKorisnike(String contextPath) {
		String path = contextPath + "data\\korisnici.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.korisnici);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sacuvajKupce(String contextPath) {
		String path = contextPath + "data\\kupci.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.kupci);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sacuvajAdministratore(String contextPath) {
		String path = contextPath + "data\\administratori.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.administratori);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sacuvajDostavljace(String contextPath) {
		String path = contextPath + "data\\dostavljaci.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.dostavljaci);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sacuvajMenadzere(String contextPath) {
		String path = contextPath + "data\\menadzeri.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		objectMapper.setDateFormat(dateFormat);
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.menadzeri);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dodajKorisnika(Korisnik korisnik) {
		if (!korisnici.containsKey(korisnik.getKorisnickoIme())) {
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			sacuvajKorisnike(contextPath);
		}
	}
	
	public void dodajKupca(Kupac kupac) {
		if (!korisnici.containsKey(kupac.getKorisnickoIme())) {
			Korisnik korisnik = new Korisnik(
				kupac.getKorisnickoIme(),
				kupac.getLozinka(),
				kupac.getIme(),
				kupac.getPrezime(),
				kupac.getPol(),
				kupac.getDatumRodjenja(),
				kupac.getTipKorisnika(),
				kupac.getObrisan()
			);
			
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			kupci.put(kupac.getKorisnickoIme(), kupac);
			
			sacuvajKorisnike(contextPath);
			sacuvajKupce(contextPath);
		}
	}
	
	public void dodajAdministratora(Administrator administrator) {
		if (!korisnici.containsKey(administrator.getKorisnickoIme())) {
			Korisnik korisnik = new Korisnik(
				administrator.getKorisnickoIme(),
				administrator.getLozinka(),
				administrator.getIme(),
				administrator.getPrezime(),
				administrator.getPol(),
				administrator.getDatumRodjenja(),
				administrator.getTipKorisnika(),
				administrator.getObrisan()
			);
			
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			administratori.put(administrator.getKorisnickoIme(), administrator);
			
			sacuvajKorisnike(contextPath);
			sacuvajAdministratore(contextPath);
		}
	}
	
	public void dodajDostavljaca(Dostavljac dostavljac) {
		if (!korisnici.containsKey(dostavljac.getKorisnickoIme())) {
			Korisnik korisnik = new Korisnik(
				dostavljac.getKorisnickoIme(),
				dostavljac.getLozinka(),
				dostavljac.getIme(),
				dostavljac.getPrezime(),
				dostavljac.getPol(),
				dostavljac.getDatumRodjenja(),
				dostavljac.getTipKorisnika(),
				dostavljac.getObrisan()
			);
			
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			dostavljaci.put(dostavljac.getKorisnickoIme(), dostavljac);
			
			sacuvajKorisnike(contextPath);
			sacuvajDostavljace(contextPath);
		}
	}
	
	public void dodajMenadzera(Menadzer menadzer) {
		if (!korisnici.containsKey(menadzer.getKorisnickoIme())) {
			Korisnik korisnik = new Korisnik(
				menadzer.getKorisnickoIme(),
				menadzer.getLozinka(),
				menadzer.getIme(),
				menadzer.getPrezime(),
				menadzer.getPol(),
				menadzer.getDatumRodjenja(),
				menadzer.getTipKorisnika(),
				menadzer.getObrisan()
			);
			
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			menadzeri.put(menadzer.getKorisnickoIme(), menadzer);
			
			sacuvajKorisnike(contextPath);
			sacuvajMenadzere(contextPath);
		}
	}
	
	public Collection<Korisnik> getAllKorisnici() {
		return korisnici.values();
	}
	
	public Collection<Kupac> getAllKupci() {
		return kupci.values();
	}
	
	public Collection<Administrator> getAllAdministratori() {
		return administratori.values();
	}
	
	public Collection<Menadzer> getAllMenadzeri() {
		return menadzeri.values();
	}
	
	public Collection<Dostavljac> getAllDostavljaci() {
		return dostavljaci.values();
	}
	
	public HashMap<String, Korisnik> getKorisniciHashMap() {
		return korisnici;
	}
	
	public HashMap<String, Kupac> getKupciHashMap() {
		return kupci;
	}
	
	public HashMap<String, Administrator> getAdministratoriHashMap() {
		return administratori;
	}
	
	public HashMap<String, Dostavljac> getDostavljaciHashMap() {
		return dostavljaci;
	}

	public HashMap<String, Menadzer> getMenadzeriHashMap() {
		return menadzeri;
	}
}

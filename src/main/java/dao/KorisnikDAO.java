package dao;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import dto.KorisnikDTO;
import dto.MenadzerDTO;

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
	}
	
	/* ucitava korisnike iz web-2020-21/data/korisnici.json fajla i dodaje ih u hashmap-u
	 * kljuc hashmap-e je username korisnika
	 * datum se cuva u obliku "yyyy-MM-dd" (jer je html standard "yyyy-MM-dd" u <input type="date">, pa da ne komplikujem za milisekundama ...)
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
	
	/* returns true if it was successful, false if it wasn't */
	public Boolean azurirajKorisnika(KorisnikDTO dto) {
		if (korisnici.containsKey(dto.getKorisnickoIme())) {
			Korisnik korisnik = korisnici.get(dto.getKorisnickoIme());
			
			if (korisnik.getTipKorisnika() == TipKorisnika.KUPAC) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date datumRodjenja = null;
				try {
					datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
					
					korisnik.setIme(dto.getIme());
					korisnik.setPrezime(dto.getPrezime());
					korisnik.setPol(Pol.stringToPol(dto.getPol()));
					korisnik.setDatumRodjenja(datumRodjenja);
					
					Kupac kupac = kupci.get(dto.getKorisnickoIme());
					
					kupac.setIme(dto.getIme());
					kupac.setPrezime(dto.getPrezime());
					kupac.setPol(Pol.stringToPol(dto.getPol()));
					kupac.setDatumRodjenja(datumRodjenja);
					
					korisnici.replace(dto.getKorisnickoIme(), korisnik);
					kupci.replace(dto.getKorisnickoIme(), kupac);
					
					sacuvajKorisnike(contextPath);
					sacuvajKupce(contextPath);
					
					return true;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (korisnik.getTipKorisnika() == TipKorisnika.ADMINISTRATOR) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date datumRodjenja = null;
				try {
					datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
					
					korisnik.setIme(dto.getIme());
					korisnik.setPrezime(dto.getPrezime());
					korisnik.setPol(Pol.stringToPol(dto.getPol()));
					korisnik.setDatumRodjenja(datumRodjenja);
					
					Administrator admin = administratori.get(dto.getKorisnickoIme());
					
					admin.setIme(dto.getIme());
					admin.setPrezime(dto.getPrezime());
					admin.setPol(Pol.stringToPol(dto.getPol()));
					admin.setDatumRodjenja(datumRodjenja);
					
					korisnici.replace(dto.getKorisnickoIme(), korisnik);
					administratori.replace(dto.getKorisnickoIme(), admin);
					
					sacuvajKorisnike(contextPath);
					sacuvajAdministratore(contextPath);
					
					return true;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (korisnik.getTipKorisnika() == TipKorisnika.DOSTAVLJAC) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date datumRodjenja = null;
				try {
					datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
					
					korisnik.setIme(dto.getIme());
					korisnik.setPrezime(dto.getPrezime());
					korisnik.setPol(Pol.stringToPol(dto.getPol()));
					korisnik.setDatumRodjenja(datumRodjenja);
					
					Dostavljac dostavljac = dostavljaci.get(dto.getKorisnickoIme());
					
					dostavljac.setIme(dto.getIme());
					dostavljac.setPrezime(dto.getPrezime());
					dostavljac.setPol(Pol.stringToPol(dto.getPol()));
					dostavljac.setDatumRodjenja(datumRodjenja);
					
					korisnici.replace(dto.getKorisnickoIme(), korisnik);
					dostavljaci.replace(dto.getKorisnickoIme(), dostavljac);
					
					sacuvajKorisnike(contextPath);
					sacuvajDostavljace(contextPath);
					
					return true;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (korisnik.getTipKorisnika() == TipKorisnika.MENADZER) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date datumRodjenja = null;
				try {
					datumRodjenja = dateFormat.parse(dto.getDatumRodjenja());
					
					korisnik.setIme(dto.getIme());
					korisnik.setPrezime(dto.getPrezime());
					korisnik.setPol(Pol.stringToPol(dto.getPol()));
					korisnik.setDatumRodjenja(datumRodjenja);
					
					Menadzer menadzer = menadzeri.get(dto.getKorisnickoIme());
					
					menadzer.setIme(dto.getIme());
					menadzer.setPrezime(dto.getPrezime());
					menadzer.setPol(Pol.stringToPol(dto.getPol()));
					menadzer.setDatumRodjenja(datumRodjenja);
					
					korisnici.replace(dto.getKorisnickoIme(), korisnik);
					menadzeri.replace(dto.getKorisnickoIme(), menadzer);
					
					sacuvajKorisnike(contextPath);
					sacuvajMenadzere(contextPath);
					
					return true;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			return false;
		}
		
		return false;
	}
	
	/* returns true if it was successful, false if it wasn't */
	public Boolean promeniLozinku(Korisnik korisnik, String novaLozinka) {
		if (korisnici.containsKey(korisnik.getKorisnickoIme())) {
			if (korisnik.getTipKorisnika() == TipKorisnika.KUPAC) {
				Kupac kupac = kupci.get(korisnik.getKorisnickoIme());
				
				korisnik.setLozinka(novaLozinka);
				kupac.setLozinka(novaLozinka);
				
				korisnici.replace(korisnik.getKorisnickoIme(), korisnik);
				kupci.replace(kupac.getKorisnickoIme(), kupac);
				
				sacuvajKorisnike(contextPath);
				sacuvajKupce(contextPath);
				
				return true;
			} else if (korisnik.getTipKorisnika() == TipKorisnika.ADMINISTRATOR) {
				Administrator admin = administratori.get(korisnik.getKorisnickoIme());
					
				korisnik.setLozinka(novaLozinka);
				admin.setLozinka(novaLozinka);
					
				korisnici.replace(korisnik.getKorisnickoIme(), korisnik);
				administratori.replace(admin.getKorisnickoIme(), admin);
					
				sacuvajKorisnike(contextPath);
				sacuvajAdministratore(contextPath);
					
				return true;
			} else if (korisnik.getTipKorisnika() == TipKorisnika.DOSTAVLJAC) {
				Dostavljac dostavljac = dostavljaci.get(korisnik.getKorisnickoIme());
				
				korisnik.setLozinka(novaLozinka);
				dostavljac.setLozinka(novaLozinka);
				
				korisnici.replace(korisnik.getKorisnickoIme(), korisnik);
				dostavljaci.replace(dostavljac.getKorisnickoIme(), dostavljac);
				
				sacuvajKorisnike(contextPath);
				sacuvajDostavljace(contextPath);
				
				return true;
			} else if (korisnik.getTipKorisnika() == TipKorisnika.MENADZER) {
				Menadzer menadzer = menadzeri.get(korisnik.getKorisnickoIme());
				
				korisnik.setLozinka(novaLozinka);
				menadzer.setLozinka(novaLozinka);
				
				korisnici.replace(korisnik.getKorisnickoIme(), korisnik);
				menadzeri.replace(menadzer.getKorisnickoIme(), menadzer);
				
				sacuvajKorisnike(contextPath);
				sacuvajMenadzere(contextPath);
				
				return true;
			}
		}
		
		return false;
	}
	
	public List<MenadzerDTO> slobodniMenadzeri() {
		List<MenadzerDTO> returnList = new ArrayList<MenadzerDTO>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Menadzer m : menadzeri.values()) {
			if (m.getRestoran() == null) {
				MenadzerDTO dto = new MenadzerDTO();
				dto.setKorisnickoIme(m.getKorisnickoIme());
				dto.setIme(m.getIme());
				dto.setPrezime(m.getPrezime());
				dto.setRestoran(m.getRestoran());
				dto.setPol(m.getPol().toString());
				dto.setDatumRodjenja(dateFormat.format(m.getDatumRodjenja()));
				
				returnList.add(dto);
			}
		}
		
		return returnList;
	}
	
	public void dodajPoeneZaKreiranuPorudzbinu(String korisnickoImeKupca, float cenaPorudzbine) {
		Kupac kupac = this.kupci.get(korisnickoImeKupca);
		
		if (kupac != null) {
			int bodovi = kupac.getSakupljeniBodovi() + (int) (cenaPorudzbine / 1000 * 133);
			kupac.setSakupljeniBodovi(bodovi);
			
			sacuvajKupce(contextPath);
		}
	}
	
	public void oduzmiPoeneZaOtkazanuPorudzbinu(String korisnickoImeKupca, float cenaPorudzbine) {
		Kupac kupac = this.kupci.get(korisnickoImeKupca);
		
		if (kupac != null) {
			int bodovi = kupac.getSakupljeniBodovi() - (int) (cenaPorudzbine / 1000 * 133 * 4);
			kupac.setSakupljeniBodovi(bodovi);
			
			sacuvajKupce(contextPath);
		}
	}
}

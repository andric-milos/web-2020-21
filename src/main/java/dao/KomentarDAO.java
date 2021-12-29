package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Komentar;
import beans.StatusKomentara;

public class KomentarDAO {
	private HashMap<String, Komentar> komentari; // key - komentar.kupac + "_" + komentar.restoran
	private String contextPath;

	public KomentarDAO(String contextPath) {
		this.komentari = new HashMap<String, Komentar>();
		
		this.contextPath = contextPath;
		
		ucitajKomentare(contextPath);
	}
	
	private void ucitajKomentare(String contextPath) {
		String path = contextPath + "data\\komentari.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			this.komentari = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Komentar>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("KomentarDAO[method ucitajKomentare]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	private void sacuvajKomentare(String contextPath) {
		String path = contextPath + "data\\komentari.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.komentari);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dodajKomentar(Komentar komentar) {
		/* Ako je kupac vec ostavio komentar vezan za odredjeni restoran,
		 * ova metoda ce samo ubaciti novi komentar umesto starog */
		
		String key = komentar.getKupac() + "_" + komentar.getRestoran();
		this.komentari.put(key, komentar); // If the map previously contained a mapping for the key, the old value is replaced.
		sacuvajKomentare(this.contextPath);
	}
	
	public HashMap<String, Komentar> getAllKomentariHashMap() {
		return this.komentari;
	}
	
	public List<Komentar> getAllKomentariByRestoran(String restoran) {
		List<Komentar> komentari = new ArrayList<Komentar>();
		
		for (Komentar k : this.komentari.values()) {
			if (k.getRestoran().equals(restoran)) {
				komentari.add(k);
			}
		}
		
		return komentari;
	}
	
	public void odobriKomentar(String key) {
		if (this.komentari.containsKey(key)) {
			Komentar komentar = this.komentari.get(key);
			
			if (komentar.getStatus().equals(StatusKomentara.NA_CEKANJU)) {
				komentar.setStatus(StatusKomentara.ODOBREN);
				sacuvajKomentare(contextPath);
			}
		}
	}
	
	public void odbijKomentar(String key) {
		if (this.komentari.containsKey(key)) {
			Komentar komentar = this.komentari.get(key);
			
			if (komentar.getStatus().equals(StatusKomentara.NA_CEKANJU)) {
				komentar.setStatus(StatusKomentara.ODBIJEN);
				sacuvajKomentare(contextPath);
			}
		}
	}
	
	public List<Komentar> getKomentariNaCekanjuFromSpecificRestaurant(String restoran) {
		List<Komentar> naCekanju = new ArrayList<Komentar>();
		
		for (Komentar k : this.komentari.values()) {
			if (k.getRestoran().equals(restoran) && k.getStatus().equals(StatusKomentara.NA_CEKANJU)) {
				naCekanju.add(k);
			}
		}
		
		return naCekanju;
	}
}

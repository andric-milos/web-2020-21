package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.ZahtevZaDostavu;

public class ZahtevZaDostavuDAO {
	private HashMap<String, ZahtevZaDostavu> zahtevi;	// key - zahtev.id_porudzbine + "_" + zahtev.dostavljac, value - zahtev za dostavu
	private String contextPath;
	
	public ZahtevZaDostavuDAO(String contextPath) {
		this.zahtevi = new HashMap<String, ZahtevZaDostavu>();
		
		this.contextPath = contextPath;
		
		ucitajZahteve(contextPath);
	}
	
	public void dodajZahtev(ZahtevZaDostavu zahtev) {
		String key = zahtev.getId_porudzbine() + "_" + zahtev.getDostavljac();
		
		if (!this.zahtevi.containsKey(key)) {
			this.zahtevi.put(key, zahtev);
			
			sacuvajZahteve(contextPath);
		}
	}
	
	public HashMap<String, ZahtevZaDostavu> getAllZahteviHashMap() {
		return this.zahtevi;
	}
	
	public Collection<ZahtevZaDostavu> getAllZahteviCollection() {
		return this.zahtevi.values();
	}
	
	public List<ZahtevZaDostavu> getAllZahteviList() {
		List<ZahtevZaDostavu> zahtevi = new ArrayList<ZahtevZaDostavu>();
		
		for (ZahtevZaDostavu z : this.zahtevi.values()) {
			zahtevi.add(z);
		}
		
		return zahtevi;
	}
	
	public void sacuvajZahteve(String contextPath) {
		String path = contextPath + "data\\zahtevi.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.zahtevi);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ucitajZahteve(String contextPath) {
		String path = contextPath + "data\\zahtevi.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			this.zahtevi = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, ZahtevZaDostavu>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ZahtevZaDostavuDAO[method ucitajZahteve]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
}

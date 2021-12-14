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

import beans.Artikal;
import beans.Restoran;

public class RestoranDAO {
	private HashMap<String, Restoran> restorani;	// key = ime restorana koje je unikatno, value = restoran
	private String contextPath;
	
	public RestoranDAO(String contextPath) {
		this.restorani = new HashMap<String, Restoran>();
		
		this.contextPath = contextPath;
		
		ucitajRestorane(contextPath);
	}
	
	public Collection<Restoran> getAllRestoraniCollection() {
		return restorani.values();
	}
	
	public List<Restoran> getAllRestoraniList() {
		List<Restoran> restorani = new ArrayList<Restoran>();
		
		for (Restoran r : this.restorani.values()) {
			restorani.add(r);
		}
		
		return restorani;
	}
	
	public HashMap<String, Restoran> getRestoraniHashMap() {
		return restorani;
	}
	
	public void dodajRestoran(Restoran restoran) {
		if (!this.restorani.containsKey(restoran.getNaziv())) {
			this.restorani.put(restoran.getNaziv(), restoran);
			
			sacuvajRestorane(contextPath);
		}
	}
	
	public void sacuvajRestorane(String contextPath) {
		String path = contextPath + "data\\restorani.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.restorani);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ucitajRestorane(String contextPath) {
		String path = contextPath + "data\\restorani.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			this.restorani = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Restoran>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("RestoranDAO[method ucitajRestorane]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	public Restoran getRestaurantByItsName(String name) {
		if (!this.restorani.containsKey(name)) {
			return null;
		}
		
		return this.restorani.get(name);
	}
	
	public void dodajArtikal(Artikal artikal) {
		if (this.restorani.containsKey(artikal.getRestoran())) {
			Restoran restoran = this.restorani.get(artikal.getRestoran());
			restoran.getArtikli().add(artikal);
			
			this.restorani.replace(artikal.getRestoran(), restoran);
			
			sacuvajRestorane(contextPath);
		}
	}
	
	public void izmeniArtikal(String stariNazivArtikla, Artikal artikal) {
		if (this.restorani.containsKey(artikal.getRestoran())) {
			Restoran restoran = this.restorani.get(artikal.getRestoran());
			
			if (restoran.sadrziArtikal(stariNazivArtikla)) {
				Artikal stariArtikal = getArtikalByItsName(restoran, stariNazivArtikla);
				restoran.getArtikli().remove(stariArtikal);
				restoran.getArtikli().add(artikal);
				
				sacuvajRestorane(contextPath);
			}
		}
	}
	
	public Artikal getArtikalByItsName(Restoran restoran, String nazivArtikla) {
		for (Artikal a : restoran.getArtikli()) {
			if (a.getNaziv().equals(nazivArtikla)) {
				return a;
			}
		}
		
		return null;
	}
}

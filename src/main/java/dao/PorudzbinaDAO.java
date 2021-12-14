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

import beans.Porudzbina;

public class PorudzbinaDAO {
	private HashMap<String, Porudzbina> porudzbine;	// key - jedinstveni identifikator, value - porudzbina
	private String contextPath;
	
	public PorudzbinaDAO(String contextPath) {
		this.porudzbine = new HashMap<String, Porudzbina>();
		
		this.contextPath = contextPath;
		
		ucitajPorudzbine(contextPath);
	}
	
	public Collection<Porudzbina> getAllPorudzbineCollection() {
		return porudzbine.values();
	}
	
	public List<Porudzbina> getAllPorudzbineList() {
		List<Porudzbina> porudzbine = new ArrayList<Porudzbina>();
		
		for (Porudzbina p : this.porudzbine.values()) {
			porudzbine.add(p);
		}
		
		return porudzbine;
	}
	
	public HashMap<String, Porudzbina> getAllPorudzbineHashMap() {
		return porudzbine;
	}
	
	public void sacuvajPorudzbine(String contextPath) {
		String path = contextPath + "data\\porudzbine.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), this.porudzbine);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ucitajPorudzbine(String contextPath) {
		String path = contextPath + "data\\porudzbine.json";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			this.porudzbine = objectMapper.readValue(new File(path), new TypeReference<HashMap<String, Porudzbina>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("PorudzbinaDAO[method ucitajPorudzbine]: File " + path + " couldn't be open. It probably doesn't exist.");
		}
	}
	
	public void dodajPorudzbinu(Porudzbina porudzbina) {
		if (!this.porudzbine.containsKey(porudzbina.getId())) {
			this.porudzbine.put(porudzbina.getId(), porudzbina);
			
			sacuvajPorudzbine(contextPath);
		}
	}
}

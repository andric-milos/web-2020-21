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

import beans.Dostavljac;
import beans.Porudzbina;
import beans.StatusPorudzbine;
import dto.PorudzbinaDTO;

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
	
	public List<PorudzbinaDTO> findAllPorudzbineByKupac(String kupac) {
		List<PorudzbinaDTO> porudzbine = new ArrayList<PorudzbinaDTO>();
		
		for (Porudzbina p : this.porudzbine.values()) {
			if (p.getKupac().equals(kupac)) {
				PorudzbinaDTO dto = new PorudzbinaDTO(p);
				porudzbine.add(dto);
			}
		}
		
		return porudzbine;
	}
	
	public Porudzbina getPorudzbinaByItsId(String id) {
		return this.porudzbine.get(id);	// it returns null if map contains no mapping for the key
	}
	
	public boolean otkaziPorudzbinu(Porudzbina porudzbina) {
		if (porudzbina.getStatus().equals(StatusPorudzbine.OBRADA)) {
			porudzbina.setStatus(StatusPorudzbine.OTKAZANA);
			sacuvajPorudzbine(contextPath);
			
			return true;
		}
		
		return false;
	}
	
	public List<PorudzbinaDTO> findAllPorudzbineByRestoran(String restoran) {
		List<PorudzbinaDTO> porudzbine = new ArrayList<PorudzbinaDTO>();
		
		for (Porudzbina p : this.porudzbine.values()) {
			if (p.getRestoran().equals(restoran)) {
				PorudzbinaDTO dto = new PorudzbinaDTO(p);
				porudzbine.add(dto);
			}
		}
		
		return porudzbine;
	}
	
	public boolean pripremanjePorudzbine(Porudzbina porudzbina) {
		if (porudzbina.getStatus().equals(StatusPorudzbine.OBRADA)) {
			porudzbina.setStatus(StatusPorudzbine.U_PRIPREMI);
			sacuvajPorudzbine(contextPath);
			
			return true;
		}
		
		return false;
	}
	
	public boolean porudzbinaPripremljena(Porudzbina porudzbina) {
		if (porudzbina.getStatus().equals(StatusPorudzbine.U_PRIPREMI)) {
			porudzbina.setStatus(StatusPorudzbine.CEKA_DOSTAVLJACA);
			sacuvajPorudzbine(contextPath);
			
			return true;
		}
		
		return false;
	}
	
	public List<PorudzbinaDTO> findAllPorudzbineToShowToDostavljac(Dostavljac dostavljac) {
		List<PorudzbinaDTO> porudzbine = new ArrayList<PorudzbinaDTO>();
		
		for (Porudzbina p : dostavljac.getPorudzbine()) {
			porudzbine.add(new PorudzbinaDTO(p));
		}
		
		for (Porudzbina p : this.porudzbine.values()) {
			if (p.getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA)) {
				PorudzbinaDTO dto = new PorudzbinaDTO(p);
				porudzbine.add(dto);
			}
		}
		
		return porudzbine;
	}
	
	public boolean transportovanjePorudzbine(Porudzbina porudzbina) {
		if (porudzbina.getStatus().equals(StatusPorudzbine.CEKA_DOSTAVLJACA)) {
			porudzbina.setStatus(StatusPorudzbine.U_TRANSPORTU);
			sacuvajPorudzbine(contextPath);
			
			return true;
		}
		
		return false;
	}
	
	public boolean porudzbinaDostavljena(Porudzbina porudzbina) {
		if (porudzbina.getStatus().equals(StatusPorudzbine.U_TRANSPORTU)) {
			porudzbina.setStatus(StatusPorudzbine.DOSTAVLJENA);
			sacuvajPorudzbine(contextPath);
			
			return true;
		}
		
		return false;
	}
}

package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Porudzbina {
	private String id; // 10 karaktera
	private List<Artikal> poruceniArtikli;
	private Restoran restoran; // restoran iz kojeg je poruceno
	private Date vremePorudzbine;
	private int cena;
	private Kupac kupac;
	private StatusPorudzbine status;
	
	public Porudzbina() {
		this.poruceniArtikli = new ArrayList<>();
	}
	
	public Porudzbina(String id, List<Artikal> poruceniArtikli, 
					  Restoran restoran, 
					  Date vremePorudzbine, 
					  int cena,
					  Kupac kupac, 
					  StatusPorudzbine status) {
		this.id = id;
		this.poruceniArtikli = poruceniArtikli;
		this.restoran = restoran;
		this.vremePorudzbine = vremePorudzbine;
		this.cena = cena;
		this.kupac = kupac;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Artikal> getPoruceniArtikli() {
		return poruceniArtikli;
	}

	public void setPoruceniArtikli(List<Artikal> poruceniArtikli) {
		this.poruceniArtikli = poruceniArtikli;
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}

	public Date getVremePorudzbine() {
		return vremePorudzbine;
	}

	public void setVremePorudzbine(Date vremePorudzbine) {
		this.vremePorudzbine = vremePorudzbine;
	}

	public int getCena() {
		return cena;
	}

	public void setCena(int cena) {
		this.cena = cena;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public StatusPorudzbine getStatus() {
		return status;
	}

	public void setStatus(StatusPorudzbine status) {
		this.status = status;
	}
}

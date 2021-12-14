package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Porudzbina {
	private String id; // 10 karaktera
	private List<ArtikalSaKolicinom> poruceniArtikli;
	private String restoran; // naziv restorana iz kojeg je poruceno
	private Date vremePorudzbine;	// datum i vreme porudzbine
	private float cena;
	private String kupac;	// naziv kupca
	private StatusPorudzbine status;
	
	public Porudzbina() {
		this.poruceniArtikli = new ArrayList<>();
	}
	
	public Porudzbina(String id, 
					  List<ArtikalSaKolicinom> poruceniArtikli, 
					  String restoran, 
					  Date vremePorudzbine, 
					  float cena,
					  String kupac, 
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

	public List<ArtikalSaKolicinom> getPoruceniArtikli() {
		return poruceniArtikli;
	}

	public void setPoruceniArtikli(List<ArtikalSaKolicinom> poruceniArtikli) {
		this.poruceniArtikli = poruceniArtikli;
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}

	public Date getVremePorudzbine() {
		return vremePorudzbine;
	}

	public void setVremePorudzbine(Date vremePorudzbine) {
		this.vremePorudzbine = vremePorudzbine;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public StatusPorudzbine getStatus() {
		return status;
	}

	public void setStatus(StatusPorudzbine status) {
		this.status = status;
	}
}

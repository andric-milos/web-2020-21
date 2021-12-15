package dto;

import java.util.List;

import beans.ArtikalSaKolicinom;
import beans.Porudzbina;
import beans.StatusPorudzbine;

public class PorudzbinaDTO {
	private String id; // 10 karaktera
	private List<ArtikalSaKolicinom> poruceniArtikli;
	private String restoran; // naziv restorana iz kojeg je poruceno
	private long vremePorudzbine;	// datum i vreme porudzbine izrazeno u milisekundama
	private float cena;
	private String kupac;	// naziv kupca
	private StatusPorudzbine status;
	
	public PorudzbinaDTO() {
		
	}
	
	public PorudzbinaDTO(Porudzbina p) {
		this.id = p.getId();
		this.poruceniArtikli = p.getPoruceniArtikli();
		this.restoran = p.getRestoran();
		this.vremePorudzbine = p.getVremePorudzbine().getTime();
		this.cena = p.getCena();
		this.kupac = p.getKupac();
		this.status = p.getStatus();
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

	public long getVremePorudzbine() {
		return vremePorudzbine;
	}

	public void setVremePorudzbine(long vremePorudzbine) {
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

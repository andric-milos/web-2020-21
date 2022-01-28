package dto;

import enums.StatusPorudzbine;

public class ZahtevZaDostavuDTO {
	private String id_porudzbine;
	private String restoran;
	private long datumPorudzbine;
	private long datumZahteva;
	private String dostavljac;	// korisnicko ime dostavljaca
	private float cena;
	private String kupac;	// korisnicko ime kupca
	private StatusPorudzbine status;
	
	public ZahtevZaDostavuDTO() {
		
	}

	public String getId_porudzbine() {
		return id_porudzbine;
	}

	public void setId_porudzbine(String id_porudzbine) {
		this.id_porudzbine = id_porudzbine;
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}

	public long getDatumPorudzbine() {
		return datumPorudzbine;
	}

	public void setDatumPorudzbine(long datumPorudzbine) {
		this.datumPorudzbine = datumPorudzbine;
	}

	public long getDatumZahteva() {
		return datumZahteva;
	}

	public void setDatumZahteva(long datumZahteva) {
		this.datumZahteva = datumZahteva;
	}

	public String getDostavljac() {
		return dostavljac;
	}

	public void setDostavljac(String dostavljac) {
		this.dostavljac = dostavljac;
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

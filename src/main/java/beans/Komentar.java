package beans;

import enums.StatusKomentara;

public class Komentar {
	private String kupac; // naziv kupac porudzbine koji je ostavio komentar
	private String restoran; // naziv restorana na koji se komentar odnosi
	private String tekst;
	private int ocena; // na skali od 1 do 5
	private StatusKomentara status;
	
	public Komentar() {
	}

	public Komentar(String kupac, 
					String restoran, 
					String tekst, 
					int ocena,
					StatusKomentara status) {
		this.kupac = kupac;
		this.restoran = restoran;
		this.tekst = tekst;
		this.ocena = ocena;
		this.status = status;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public StatusKomentara getStatus() {
		return status;
	}

	public void setStatus(StatusKomentara status) {
		this.status = status;
	}
}

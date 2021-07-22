package beans;

import java.util.ArrayList;
import java.util.List;

public class Korpa {
	private List<ArtikalSaKolicinom> artikli; // lista objekata koji u sebi sadrže objekat klase Artikal i kolicinu
	private String kupac; // naziv kupca cija je korpa
	private int cena;
	
	public Korpa() {
		this.artikli = new ArrayList<>();
	}

	public Korpa(List<ArtikalSaKolicinom> artikli, String kupac, int cena) {
		this.artikli = artikli;
		this.kupac = kupac;
		this.cena = cena;
	}
	
	public Korpa(String kupac) {
		this.kupac = kupac;
		this.artikli = new ArrayList<ArtikalSaKolicinom>();
		this.cena = 0;
	}

	public List<ArtikalSaKolicinom> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<ArtikalSaKolicinom> artikli) {
		this.artikli = artikli;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public int getCena() {
		return cena;
	}

	public void setCena(int cena) {
		this.cena = cena;
	}
}

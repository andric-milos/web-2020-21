package beans;

import java.util.ArrayList;
import java.util.List;

public class Korpa {
	private List<ArtikalSaKolicinom> artikli; // lista objekata koji u sebi sadrže objekat klase Artikal i kolicinu
	private Kupac korisnik; // korisnik cija je korpa
	private int cena;
	
	public Korpa() {
		this.artikli = new ArrayList<>();
	}

	public Korpa(List<ArtikalSaKolicinom> artikli, Kupac korisnik, int cena) {
		this.artikli = artikli;
		this.korisnik = korisnik;
		this.cena = cena;
	}

	public List<ArtikalSaKolicinom> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<ArtikalSaKolicinom> artikli) {
		this.artikli = artikli;
	}

	public Kupac getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Kupac korisnik) {
		this.korisnik = korisnik;
	}

	public int getCena() {
		return cena;
	}

	public void setCena(int cena) {
		this.cena = cena;
	}
}

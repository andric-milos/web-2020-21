package beans;

import java.io.File;

import enums.TipArtikla;

public class Artikal {
	private String naziv;
	private float cena;
	private TipArtikla tip;
	private String restoran; // naziv restorana kojem pripada
	private int kolicina; // u gramima ili mililitrima
	private String opis;
	private transient File slika;
	
	public Artikal() {
	}
	
	public Artikal(String naziv, 
				   float cena, 
				   TipArtikla tip, 
				   String restoran, 
				   int kolicina, 
				   String opis,
				   File slika) {
		this.naziv = naziv;
		this.cena = cena;
		this.tip = tip;
		this.restoran = restoran;
		this.kolicina = kolicina;
		this.opis = opis;
		this.slika = slika;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	public TipArtikla getTip() {
		return tip;
	}

	public void setTip(TipArtikla tip) {
		this.tip = tip;
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public File getSlika() {
		return slika;
	}

	public void setSlika(File slika) {
		this.slika = slika;
	}
}

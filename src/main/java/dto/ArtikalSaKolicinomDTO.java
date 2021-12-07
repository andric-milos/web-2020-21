package dto;

public class ArtikalSaKolicinomDTO {
	private String naziv;
	private float cena;
	private String tip;
	private String restoran;
	private int kolicina;	// grami ili mililitri
	private String opis;
	private int koliko;		// koliko puta je dodat u korpu
	
	public ArtikalSaKolicinomDTO() {
		
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

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
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

	public int getKoliko() {
		return koliko;
	}

	public void setKoliko(int koliko) {
		this.koliko = koliko;
	}
}

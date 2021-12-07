package beans;

public class ArtikalSaKolicinom {
	private Artikal artikal;
	private int koliko;	// koliko je puta artikal dodat u korpu
	
	public ArtikalSaKolicinom() {
	}

	public ArtikalSaKolicinom(Artikal artikal, int koliko) {
		this.artikal = artikal;
		this.koliko = koliko;
	}

	public Artikal getArtikal() {
		return artikal;
	}

	public void setArtikal(Artikal artikal) {
		this.artikal = artikal;
	}

	public int getKoliko() {
		return koliko;
	}

	public void setKoliko(int koliko) {
		this.koliko = koliko;
	}
}

package beans;

public class ArtikalSaKolicinom {
	private Artikal artikal;
	private int kolicina;
	
	public ArtikalSaKolicinom() {
	}

	public ArtikalSaKolicinom(Artikal artikal, int kolicina) {
		this.artikal = artikal;
		this.kolicina = kolicina;
	}

	public Artikal getArtikal() {
		return artikal;
	}

	public void setArtikal(Artikal artikal) {
		this.artikal = artikal;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
}

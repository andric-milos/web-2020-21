package beans;

public class TipKupca {
	private String ime; // Zlatni, Srebrni ili Bronzani
	private int popust; // procenat
	private int brojBodova; // trazeni broj bodova za odredjenu kategoriju - npr. bronzani 20, srebrni 40, zlatni 60 ...
	
	public TipKupca() {
	}

	public TipKupca(String ime, int popust, int brojBodova) {
		this.ime = ime;
		this.popust = popust;
		this.brojBodova = brojBodova;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public int getPopust() {
		return popust;
	}

	public void setPopust(int popust) {
		this.popust = popust;
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}
}

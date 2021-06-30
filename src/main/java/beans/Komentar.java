package beans;

public class Komentar {
	private Kupac kupac; // kupac porudzbine koji je ostavio komentar
	private Restoran restoran; // restoran na koji se komentar odnosi
	private String tekst;
	private int ocena; // na skali od 1 do 5
	
	public Komentar() {
	}

	public Komentar(Kupac kupac, 
					Restoran restoran, 
					String tekst, 
					int ocena) {
		this.kupac = kupac;
		this.restoran = restoran;
		this.tekst = tekst;
		this.ocena = ocena;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
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
}

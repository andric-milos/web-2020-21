package beans;

public class Komentar {
	private String kupac; // naziv kupac porudzbine koji je ostavio komentar
	private String restoran; // naziv restorana na koji se komentar odnosi
	private String tekst;
	private int ocena; // na skali od 1 do 5
	
	public Komentar() {
	}

	public Komentar(String kupac, 
					String restoran, 
					String tekst, 
					int ocena) {
		this.kupac = kupac;
		this.restoran = restoran;
		this.tekst = tekst;
		this.ocena = ocena;
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
}

package beans;

import java.util.Date;

public class Menadzer extends Korisnik {
	private String restoran;	// naziv restorana kojim menadzerise
	
	public Menadzer() {
	}

	public Menadzer(String korisnickoIme, 
					String lozinka, 
					String ime, 
					String prezime, 
					Pol pol, 
					Date datumRodjenja,
					TipKorisnika tipKorisnika,
					Boolean obrisan,
					String restoran) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, tipKorisnika, obrisan);
		this.restoran = restoran;
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}
}

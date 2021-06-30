package beans;

import java.util.Date;

public class Menadzer extends Korisnik {
	private Restoran restoran;
	
	public Menadzer() {
	}

	public Menadzer(String korisnickoIme, 
					String lozinka, 
					String ime, 
					String prezime, 
					Pol pol, 
					Date datumRodjenja, 
					Restoran restoran) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);
		this.restoran = restoran;
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}
}

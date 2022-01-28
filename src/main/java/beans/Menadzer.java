package beans;

import java.util.Date;

import enums.Pol;
import enums.TipKorisnika;

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
	
	public Menadzer(Korisnik k) {
		super(k.getKorisnickoIme(),
			  k.getLozinka(),
			  k.getIme(),
			  k.getPrezime(),
			  k.getPol(),
			  k.getDatumRodjenja(),
			  k.getTipKorisnika(),
			  k.getObrisan());
		this.restoran = null;	// neka bude onda posle ako je menadzeru restoran null, onda trenutno ne menadzerise nijednim restoranom
	}

	public String getRestoran() {
		return restoran;
	}

	public void setRestoran(String restoran) {
		this.restoran = restoran;
	}
}

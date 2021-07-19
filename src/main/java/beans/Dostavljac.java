package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dostavljac extends Korisnik {
	private List<Porudzbina> porudzbine; // porudzbine koje treba da dostavi

	public Dostavljac() {
		this.porudzbine = new ArrayList<>();
	}

	public Dostavljac(String korisnickoIme, 
					  String lozinka, 
					  String ime, 
					  String prezime, 
					  Pol pol, 
					  Date datumRodjenja,
					  TipKorisnika tipKorisnika,
					  List<Porudzbina> porudzbine) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, tipKorisnika);
		this.porudzbine = porudzbine;
	}

	public List<Porudzbina> getPorudzbine() {
		return porudzbine;
	}

	public void setPorudzbine(List<Porudzbina> porudzbine) {
		this.porudzbine = porudzbine;
	}
}

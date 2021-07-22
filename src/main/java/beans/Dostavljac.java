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
					  Boolean obrisan,
					  List<Porudzbina> porudzbine) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, tipKorisnika, obrisan);
		this.porudzbine = porudzbine;
	}
	
	public Dostavljac(Korisnik k) {
		super(k.getKorisnickoIme(),
			  k.getLozinka(),
			  k.getIme(),
			  k.getPrezime(),
			  k.getPol(),
			  k.getDatumRodjenja(),
			  k.getTipKorisnika(),
			  k.getObrisan());
		this.porudzbine = new ArrayList<Porudzbina>();
	}

	public List<Porudzbina> getPorudzbine() {
		return porudzbine;
	}

	public void setPorudzbine(List<Porudzbina> porudzbine) {
		this.porudzbine = porudzbine;
	}
}

package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kupac extends Korisnik {
	private TipKupca tipKupca;
	private List<Porudzbina> svePorudzbine;
	private Korpa korpa;
	private int sakupljeniBodovi;
	
	public Kupac() {
		// this.tipKupca = 
		this.svePorudzbine = new ArrayList<>();
		this.korpa = new Korpa();
		this.sakupljeniBodovi = 0;
	}

	public Kupac(String korisnickoIme, 
			  	 String lozinka, 
			  	 String ime, 
			  	 String prezime, 
			  	 Pol pol, 
			  	 Date datumRodjenja,
			  	 TipKorisnika tipKorisnika,
			  	 Boolean obrisan,
			  	 TipKupca tipKupca, 
				 List<Porudzbina> svePorudzbine, 
				 Korpa korpa, 
				 int sakupljeniBodovi) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, tipKorisnika, obrisan);
		this.tipKupca = tipKupca;
		this.svePorudzbine = svePorudzbine;
		this.korpa = korpa;
		this.sakupljeniBodovi = sakupljeniBodovi;
	}

	public TipKupca getTipKupca() {
		return tipKupca;
	}

	public void setTipKupca(TipKupca tipKupca) {
		this.tipKupca = tipKupca;
	}

	public List<Porudzbina> getSvePorudzbine() {
		return svePorudzbine;
	}

	public void setSvePorudzbine(List<Porudzbina> svePorudzbine) {
		this.svePorudzbine = svePorudzbine;
	}

	public Korpa getKorpa() {
		return korpa;
	}

	public void setKorpa(Korpa korpa) {
		this.korpa = korpa;
	}

	public int getSakupljeniBodovi() {
		return sakupljeniBodovi;
	}

	public void setSakupljeniBodovi(int sakupljeniBodovi) {
		this.sakupljeniBodovi = sakupljeniBodovi;
	}
}

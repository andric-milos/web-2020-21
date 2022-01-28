package beans;

import java.util.Date;

import enums.Pol;
import enums.TipKorisnika;

public class Korisnik {
	private String korisnickoIme; // should be unique
	private String lozinka;
	private String ime;
	private String prezime;
	private Pol pol;
	private Date datumRodjenja;
	private TipKorisnika tipKorisnika;
	private Boolean obrisan; // logicko brisanje
	
	public Korisnik() {
	}
	
	public Korisnik(String korisnickoIme, 
					String lozinka, 
					String ime, 
					String prezime, 
					Pol pol, 
					Date datumRodjenja,
					TipKorisnika tipKorisnika,
					Boolean obrisan) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.tipKorisnika = tipKorisnika;
		this.obrisan = obrisan;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public Date getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public TipKorisnika getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(TipKorisnika tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}
}

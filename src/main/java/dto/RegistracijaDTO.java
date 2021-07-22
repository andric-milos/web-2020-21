package dto;

public class RegistracijaDTO {
	private String korisnickoIme;
	private String lozinka;
	private String potvrda_lozinke;
	private String ime;
	private String prezime;
	private String pol;
	private String datumRodjenja;
	
	public RegistracijaDTO() {
		
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

	public String getPotvrda_lozinke() {
		return potvrda_lozinke;
	}

	public void setPotvrda_lozinke(String potvrda_lozinke) {
		this.potvrda_lozinke = potvrda_lozinke;
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

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(String datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
}

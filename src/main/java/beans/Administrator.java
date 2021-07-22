package beans;

public class Administrator extends Korisnik {
	
	public Administrator() {
		
	}
	
	public Administrator(Korisnik k) {
		super(k.getKorisnickoIme(),
			  k.getLozinka(),
			  k.getIme(),
			  k.getPrezime(),
			  k.getPol(),
			  k.getDatumRodjenja(),
			  k.getTipKorisnika(),
			  k.getObrisan());
	}
}

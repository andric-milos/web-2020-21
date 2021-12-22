package beans;

import java.util.Date;

public class ZahtevZaDostavu {
	private String id_porudzbine;
	private String dostavljac;	// korisnicko ime dostavljaca
	private Date datum;
	
	public ZahtevZaDostavu() {
		
	}

	public String getId_porudzbine() {
		return id_porudzbine;
	}

	public void setId_porudzbine(String id_porudzbine) {
		this.id_porudzbine = id_porudzbine;
	}

	public String getDostavljac() {
		return dostavljac;
	}

	public void setDostavljac(String dostavljac) {
		this.dostavljac = dostavljac;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
}

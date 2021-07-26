package dto;

public class PromenaLozinkeDTO {
	private String lozinka;
	private String nova_lozinka;
	private String potvrda_nove_lozinke;
	
	public PromenaLozinkeDTO() {
		
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getNova_lozinka() {
		return nova_lozinka;
	}

	public void setNova_lozinka(String nova_lozinka) {
		this.nova_lozinka = nova_lozinka;
	}

	public String getPotvrda_nove_lozinke() {
		return potvrda_nove_lozinke;
	}

	public void setPotvrda_nove_lozinke(String potvrda_nove_lozinke) {
		this.potvrda_nove_lozinke = potvrda_nove_lozinke;
	}
}

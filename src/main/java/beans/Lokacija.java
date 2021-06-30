package beans;

public class Lokacija {
	private float geografskaDuzina;
	private float geografskaSirina;
	private Adresa adresa;
	
	public Lokacija() {
	}
	
	public Lokacija(float geografskaDuzina, 
					float geografskaSirina, 
					Adresa adresa) {
		this.geografskaDuzina = geografskaDuzina;
		this.geografskaSirina = geografskaSirina;
		this.adresa = adresa;
	}
	
	public float getGeografskaDuzina() {
		return geografskaDuzina;
	}
	
	public void setGeografskaDuzina(float geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}
	
	public float getGeografskaSirina() {
		return geografskaSirina;
	}
	
	public void setGeografskaSirina(float geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}
	
	public Adresa getAdresa() {
		return adresa;
	}
	
	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}
}

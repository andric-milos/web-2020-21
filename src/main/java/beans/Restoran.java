package beans;

import java.util.ArrayList;
import java.util.List;

public class Restoran {
	private String naziv;
	private TipRestorana tip;
	private List<Artikal> artikli; // artikli koje restoran ima u ponudi
	private StatusRestorana status;
	private Lokacija lokacija;
	// logo restorana (slika)
	
	public Restoran() {
		this.artikli = new ArrayList<Artikal>();
	}
	
	public Restoran(String naziv, 
					TipRestorana tip, 
					List<Artikal> artikli, 
					StatusRestorana status, 
					Lokacija lokacija) {
		this.naziv = naziv;
		this.tip = tip;
		this.artikli = artikli;
		this.status = status;
		this.lokacija = lokacija;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public TipRestorana getTip() {
		return tip;
	}

	public void setTip(TipRestorana tip) {
		this.tip = tip;
	}

	public List<Artikal> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<Artikal> artikli) {
		this.artikli = artikli;
	}

	public StatusRestorana getStatus() {
		return status;
	}

	public void setStatus(StatusRestorana status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}
}

package beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Restoran {
	private String naziv;	// trebalo bi da bude unique, jer na primer u klasi Porudzbina imamo polje koje predstavlja ime restorana
	private TipRestorana tip;
	private List<Artikal> artikli; // artikli koje restoran ima u ponudi
	private StatusRestorana status;
	private Lokacija lokacija;
	private transient File logo;
	private String menadzer;
	
	public Restoran() {
		this.artikli = new ArrayList<Artikal>();
	}
	
	public Restoran(String naziv, 
					TipRestorana tip, 
					StatusRestorana status, 
					Lokacija lokacija,
					File logo,
					String menadzer) {
		this.naziv = naziv;
		this.tip = tip;
		this.status = status;
		this.lokacija = lokacija;
		this.logo = logo;
		this.menadzer = menadzer;
		
		this.artikli = new ArrayList<Artikal>();
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

	public File getLogo() {
		return logo;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}

	public String getMenadzer() {
		return menadzer;
	}

	public void setMenadzer(String menadzer) {
		this.menadzer = menadzer;
	}
}

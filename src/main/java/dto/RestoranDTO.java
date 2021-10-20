package dto;

import java.util.List;

import beans.Lokacija;

public class RestoranDTO {
	private String naziv;
	private String tip;
	private List<ArtikalDTO> artikli;
	private String status;
	private Lokacija lokacija;
	private String menadzer;
	
	public RestoranDTO() {
		
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public List<ArtikalDTO> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<ArtikalDTO> artikli) {
		this.artikli = artikli;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public String getMenadzer() {
		return menadzer;
	}

	public void setMenadzer(String menadzer) {
		this.menadzer = menadzer;
	}
}

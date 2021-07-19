package beans;

public enum TipKorisnika {
	KUPAC,
	ADMINISTRATOR,
	DOSTAVLJAC,
	MENADZER;
	
	public static String tipKorisnikaToString(TipKorisnika tipKorisnika) {
		if (tipKorisnika == TipKorisnika.ADMINISTRATOR) 
			return "ADMINISTRATOR";
		else if (tipKorisnika == TipKorisnika.KUPAC)
			return "KUPAC";
		else if (tipKorisnika == TipKorisnika.DOSTAVLJAC)
			return "DOSTAVLJAC";
		else if (tipKorisnika == TipKorisnika.MENADZER)
			return "MENADZER";
		else
			return null;
	}
	
	public static TipKorisnika stringToTipKorisnika(String tipKorisnikaString) {
		if (tipKorisnikaString.equals("ADMINISTRATOR"))
			return TipKorisnika.ADMINISTRATOR;
		else if (tipKorisnikaString.equals("KUPAC"))
			return TipKorisnika.KUPAC;
		else if (tipKorisnikaString.equals("DOSTAVLJAC"))
			return TipKorisnika.DOSTAVLJAC;
		else if (tipKorisnikaString.equals("MENADZER"))
			return TipKorisnika.MENADZER;
		else
			return null;
	}
}

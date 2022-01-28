package enums;

public enum Pol {
	MUSKO,
	ZENSKO;
	
	public static String polToString(Pol pol) {
		if (pol == Pol.MUSKO)
			return "MUSKO";
		else
			return "ZENSKO";
	}
	
	public static Pol stringToPol(String polString) {
		if (polString.equals("MUSKO"))
			return Pol.MUSKO;
		else
			return Pol.ZENSKO;
	}
}

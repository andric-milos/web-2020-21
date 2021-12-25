package beans;

public class StaticMethods {
	
	public StaticMethods() {
		
	}
	
	public static String generateId() {
		char[] arrayOfChars = {
			'0', '1', '2', '3' ,'4' ,'5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'O', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};
		
		int max = arrayOfChars.length - 1;
		int min = 0;
		int range = max - min + 1;
		
		int lengthOfId = 10;
		String id = "";
		
		for (int i = 0; i < lengthOfId; i++) {
			id += arrayOfChars[(int)(Math.random() * range) + min];
		}
		
		return id;
	}

	public static String transformToNonDiacritical(String diacritical) {
		String nonDiacritical = diacritical;
		
		nonDiacritical = nonDiacritical.replace('š', 's');
		nonDiacritical = nonDiacritical.replace('ž', 'z');
		nonDiacritical = nonDiacritical.replace("ð", "dj");
		nonDiacritical = nonDiacritical.replace('è', 'c');
		nonDiacritical = nonDiacritical.replace('æ', 'c');
		
		nonDiacritical = nonDiacritical.replace('Š', 's');
		nonDiacritical = nonDiacritical.replace('Ž', 'z');
		nonDiacritical = nonDiacritical.replace("Ð", "dj");
		nonDiacritical = nonDiacritical.replace('È', 'c');
		nonDiacritical = nonDiacritical.replace('Æ', 'c');
		
		return nonDiacritical;
	}
}

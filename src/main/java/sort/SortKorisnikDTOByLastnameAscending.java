package sort;

import java.util.Comparator;

import dto.KorisnikDTO;

public class SortKorisnikDTOByLastnameAscending implements Comparator<KorisnikDTO> {

	@Override
	public int compare(KorisnikDTO o1, KorisnikDTO o2) {
		return o1.getPrezime().compareToIgnoreCase(o2.getPrezime());
	}

}
